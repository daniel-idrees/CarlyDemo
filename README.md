# CarlyDemo
**Coding Challenge Task**

A simple vehicle management Android app that enables users to create, view,
and delete Vehicles and dynamically display features specific to the selected vehicle

The car data is retrieved from the csv file and selected cars are stored in the device storage.

#### Technologies and Tools 

- Kotlin 
- Model-View-ViewModel (MVVM) architectural pattern
- App Architecture as per [Google's Recommendation](https://developer.android.com/topic/architecture)
- Multi Module
- Hilt for dependency injection
- Coroutines for asynchronous operations
- Flows for handling data asynchronously
- Room for local storage 
- Jetpack Compose for building UI
- Junit/Mockito/Kotest for unit testing

#### Limitations 

- Only Protrait mode works. Landscape mode is not handled.
- Tablet or wider devices are not handled.
- Theme configuration can be improved.
- Errors are catched but not handled on UI.
- Loading state is not handled on UI
