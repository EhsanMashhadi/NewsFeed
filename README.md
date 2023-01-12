# NewsFeed

NewsFeed is a sample Android (100% Kotlin :hearts:) application with MVVM architecture using modern
development tools including but not limited to Hilt, Coroutines, Flow, Paging, Navigation, LiveData,
and Compose.

![screenshots-3](https://user-images.githubusercontent.com/6213824/212164694-56757204-a8c3-41e6-847e-3630db82d1ea.jpg)


## Download

TBD

## Ingredients

* [Retrofit](https://github.com/square/retrofit) and [OkHTTP](https://github.com/square/okhttp): Consuming REST APIs supporting caching and interceptors
* [Moshi](https://github.com/square/moshi): Fast JSON mapper that integrates with Retrofit seamlessly
* [Firebase realtime database](https://firebase.google.com/docs/database): Storing data remotely in a JSON format that also works in **offline** mode
* [Firebase authentication](https://firebase.google.com/docs/auth): Authenticating users with email and password and also sign-in by Google
* [Firebase crashlytics](https://firebase.google.com/docs/crashlytics): Reporting and monitoring crashes
* [Coroutines](https://developer.android.com/kotlin/coroutines): Writing async code like sync code (for sure, multi-threading and lifecycle handling)
* [Flow](https://developer.android.com/kotlin/flow): Streaming data that can be computed asynchronously (similar to Observable or Flowable if you are familiar with [RxJava](https://github.com/ReactiveX/RxJava))
* [HILT](https://developer.android.com/training/dependency-injection/hilt-android): Dependency injection library designed for Android (working in compile-time, powerful as [Dagger](https://dagger.dev/dev-guide), but simpler)
* SAMF: Single Activity Multiple Fragments [Single activity: Why, when, and how](https://www.youtube.com/watch?v=2k8x8V77CrU)
* [MVVM](https://developer.android.com/topic/libraries/architecture/viewmodel): Model, View , ViewModel
* [Repository Pattern](https://developer.android.com/topic/architecture#data-layer): Using as a single source of truth between a server and database
* [Navigation](https://developer.android.com/guide/navigation/navigation-getting-started): Navigating between fragments using navGraph
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata): Lifecycle-aware observable data holder used between view models and views (fragments)
* [Paging](https://developer.android.com/topic/libraries/architecture/paging/v3-overview): Loading small chunks of data from a large dataset (locally or over the network)
* [View Binding](https://developer.android.com/topic/architecture#data-layer): Getting rid of `findViewById` and also providing **null safety** and **type safety**
* [Glide](https://github.com/bumptech/glide): Simple and powerful for loading images, error handling, and caching
* [Material Design](https://developer.android.com/develop/ui/views/theming/look-and-feel)
* [ConstraintLayout](https://developer.android.com/develop/ui/views/layout/constraint-layout): Building complex layers with a flat hierarchy
* [Compose](https://developer.android.com/jetpack/compose): Just limited to the *SingUp* fragment (at least for now!)
* Multi themes: Supporting Day, Night, and System Mode (works instantly and no app restarting)
* [Shimmer](https://github.com/facebook/shimmer-android): Displaying shimmering effect (for loading state) in recycler views
* [Logger](https://github.com/orhanobut/logger)
* [Benchmark](https://developer.android.com/topic/performance/benchmarking/benchmarking-overview): Macro benchmarking of the app startup

## NewsAPI

NewsFeed uses [NewsAPI](https://newsapi.org) which locates articles and news headlines from various
sources across the web by providing JSON API. 

You can get your own API key (free for developers) and put it in the `API_KEY` section of the `build.gradle` file.

## Did you find this repo helpful?
I hope it is!
* Don't forget to star :star: the repo.
* Follow [me](https://github.com/EhsanMashhadi) on Github for more upcoming repo
* You can also buy me a coffee! <a href="https://www.buymeacoffee.com/ehsanmashhadi" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png" alt="Buy Me A Coffee" style="height: 41px !important;width: 174px !important;box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;" ></a>
