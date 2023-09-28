# DogsBrowser
An app that allows users to browse dog breeds loaded from remote API and cached in the local Room database. Made with Android Jetpack and Views.

# About the app
The application is based on data downloaded from remote API with Retrofit. In case of poor Internet connection or no connection at all data from the API is cached in the local Room database.
Users can browse dog breed details and send them via SMS.

* Java version - 17
* Compile SDK - 34
* Minimum SDK - 26

In order to try the app just clone the repository and build the project with Android Studio.

<p align="center">
  <img src="./screenshots/Screenshot_list.png" width="250" title="list">
  <img src="./screenshots/Screenshot_details.png" width="250" title="details">
  <img src="./screenshots/Screenshot_sms.png" width="250" title="sms">
</p>

# Technology & Techniques
Used technology and programming techniques
* Android SDK
* Android Views
* View binding
* Data binding
* Kotlin
* Coroutines
* Navigation
* MVVM pattern
* Dependency Injection with Hilt
* Retrofit
* Coil (for image loading)
* Permissions
* Kotlin flows
* Room database
* SMS Manager

  # Credits
  [API](https://raw.githubusercontent.com/DevTides/DogsApi/master/dogs.json) used in this app. The idea for the app is based on [the Udemy course](https://www.udemy.com/course/androidjetpack/). I made several modifications to the application architecture, making it cleaner.
