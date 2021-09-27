# Cinema Operator Demo App
Simple Cinema Operator Demo App for Android platform

## Code Design Architecture
Code Design follows the following Design
- View
  - The UI Design of the Code.
  - Built using pure Jetpack Compose
  - Usage of (Accompanist Library)[https://google.github.io/accompanist/] for `SwipeRefresh`
  - Usage of (Coil Library)[https://coil-kt.github.io/coil/] for Image Loading & Caching
- ViewModel
  - The Presentation Logic of the View.
  - `ViewModel` class is used in the code logic
  - Coroutines are handled by `viewModelScope.launch` function
- Usecase
  - The Business Logic of the ViewModel
  - Extension of the ViewModel
- Model
  - `data class` object that is used to ferry data between all components
- DataAccess
  - Acts as a `Repository` class for API Call & Local Data Access
  - API Call is using (Retrofit Library)[https://square.github.io/retrofit/]

## Features

- [x] Use Kotlin Programming Language
- [x] Use Jetpack Compose to build the App. No XML Files development
- [X] Use Kotlin Coroutines
- [X] Use Dependency Injection (Simple Usage only)