package com.paweldylag.currencyconverter.repository.local

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wafel.skald.api.createLogger
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Created by Pawel Dylag
 */
@Database(
    entities = [
        CurrencyExchangeRateEntity::class,
        CurrencyConverterDataEntity::class,
        CurrencyOrderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun currencyExchangeRatesDao(): CurrencyExchangeRateDao
    abstract fun currencyConverterDataDao(): CurrencyConverterDataDao
    abstract fun currencyOrderDao(): CurrencyOrderDao

    companion object {

        private const val DB_NAME = "currency_converter_database"
        private val logger = createLogger(this::class.java)

        fun create(applicationContext: Context): AppDatabase {
            var provideRoomDb: () -> Single<AppDatabase> =
                { Single.error(Throwable("App database is not yet created")) }
            return Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                DB_NAME
            ).addCallback(object : RoomDatabase.Callback() {
                @SuppressLint("CheckResult")
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    provideRoomDb()
                        .prepopulateAppDefaultData()
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                            { logger.debug("Populated db with initial data.") },
                            { logger.error("Unable to populate db with initial data: ${it.message}") })
                }
            }).build()
                .also { provideRoomDb = { Single.just(it) } }

        }

        private fun Single<AppDatabase>.prepopulateAppDefaultData() =
            flatMapCompletable {
                it.currencyConverterDataDao().insert(CurrencyConverterDataEntity(0, "EUR", "1.0"))
            }

    }
}

