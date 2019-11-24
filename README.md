# Architecture Components Testing Example

The main focus of this codebase is to show examples of automated tests using Architecture Components with the MVVM architecture. 
All layers of the architecture are covered by automated tests.
   
To focus exclusively on Architecture Components, I tried to use as few dependencies as possible. In this case we are not going to see any RxJava, Dagger2 or Koin here.

## App Description

The user can do 4 operations in the app:
* Change the user's phone just on the database
* Fetch an user stored on the database
* Fetch an user from a remote server and store in the database
* Clear all users from the database

I used [jsonplaceholder](https://jsonplaceholder.typicode.com/) to provide me a fake REST API.

![Layers](files/AppExample.png "App Main Activity")

## Libraries used
* Data Binding
* ViewModel
* LiveData
* Room
* Lifecycle

![Layers](files/ArchCompLayers.png "Android Jetpack Components")
