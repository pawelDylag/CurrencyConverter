# Currency Converter
**Version:** 1.0.0
*Note: Git history and commit names are to be changed. For now I use just `WIP` messages.*
 
**This is a simple currency converter that allows user to choose base currency and base value.
All currency exchange rates are applied in realtime from the external API.**


## Architecture
This app is written using `MVVM` pattern. In case of a much bigger app, I'd rather extract `Interactors` or `UseCases`, but it's just a demo, so I used `CurrencyConverterViewModel` to wrap business logic. Every network/storage interaction is hidden behind `Repository` pattern, so it's independent from the rest of app.

Moreover I use **AndroidX + Jetifier** support libraries to get the most out of the newest Android. 

## App features
![App flow](images/app.gif)

### Full persistence
Currency list modifications and base value changes are saved to internal storage.

### Realtime exchange rates
App polls the server API for currency exchange rates `every 1 second` and uses them to transform user's input. 

### User selected currency slides to top
Yes, just as in the title. It looks awesome!

### Highly modularised and scallable code architecture
The app is divided into smaller modules talking with each other via interfaces. 
I use `Repository` pattern to handle all network/local storage operations. 
Dependency injection is provided by `Dagger 2` library.

### Asynchronously loaded images
For this demo I used `Picasso` library to asynchronously download and cache country flags from https://www.countryflags.io.
In normal case I'd use a proper backend API call to get custom flag circle icons, but here I just resize them and crop. 
They are of low resolution, but well, it's just a demo.

## Libraries I have used

### RxJava + RxAndroid
For asynchronous event processing in the whole cycle `Repository <-> ViewModel -> View`. 
It gives a nice handling of underlying repository changes, plus easier threading.

### Dagger 2 + Android extensions 
For dependency injections and object lifetime management. For such small project maybe it's an overkill, but it's super scallable now.

```Kotlin
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        RepositoryModule::class,
        ActivityModule::class
    ]
)
interface AppComponent { ... }
```
### Kotlin Gradle DSL
I just wanted ~~Kotlin everywhere~~ to have autocompletion in build files. I think it's still slower than Groovy, but I wanted to play with it :)
Oh, and I moved dependency management to `buildSrc` folder, so it will be easier to manage multiple modules' dependencies in the future.

```Kotlin
dependencies {
    implementation(Config.Dependencies.kotlinStdLib)
    implementation(Config.Dependencies.constraintLayout)
    implementation(Config.Dependencies.appCompat)
    implementation(Config.Dependencies.recyclerView)
    ...
    implementation(Config.Dependencies.retrofit)
    implementation(Config.Dependencies.retrofitRx)
    implementation(Config.Dependencies.retrofitGson)
```

### Android ViewModel 
The whole app is made using MVVM architecture, so I used the Google's `ViewModel` libraries to manage my `ViewModel` lifetimes.
I use `Dagger` to inject them into `Activities`.

```Kotlin
class CurrencyConverterViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {...}
```
### Room + RoomRx
Underlying database is build using Google's [Room](https://developer.android.com/topic/libraries/architecture/room) library. 
I use it to store user's interaction data such as value and chosen currency. The whole logic is hidden behind service:

```Kotlin 
interface CurrencyLocalStorageService {...}
```

### Retrofit + GSON
For network requests and JSON parsing. Logic is wrapped in a service:
```Kotlin 
interface CurrencyNetworkService {...}
```

### Skald
This is [my friend's logging library (to which I also contribute)](https://github.com/wafel82/Skald). It's easily extensible, and provides an awesome DSL:
It also allows me to lookup thread names to see if I didn't mess anything with Rx.
```Kotlin
 skald {
            writeSaga {
                toLogcat {
                    withLevel { LogLevel.TRACE }
                    withPath { "com.paweldylag" }
                    withPattern { "${it.simplePath}[${it.threadName}] -> ${it.message}" }
                }
            }
        }
```

## Mockk + JUnit
The `ViewModel` part is covered with robust testing to make sure business logic is OK. 
I use `Mockk` library to mock `Repository` interactions.
Normally I'd cover much more with tests, but it's just a demo :)

```Kotlin
@Test
    fun `changes base currency in repository when new item is selected`() {
        // given
        val repository = mockk<CurrencyRepository>()
        every { repository.setUserDefinedCurrency(EUR)} returns Completable.complete()
        every { repository.setUserDefinedCurrencyAmount(BigDecimal("1.0"))} returns Completable.complete()
        val viewModel = CurrencyConverterViewModel(repository)
        val item = newItem(EUR, "1.0")

        // when
        viewModel.onItemSelected(item)

        // then
        verify { repository.setUserDefinedCurrency(item.currencyModel) }
    }
```

## Possible future optimisations
1. `RecyclerView` list updates management can be optimised to add `paging` and lazy loading. 
2. Currency math operations might be extracted to a separate module (library?) for easy testing and management.
3. Nice and slick animations. 

I just haven't had much time to add everything (because it's just a quick demo) but it's all possible :)

## Feedback 
I'm super excited to get all the feedback available :) 
