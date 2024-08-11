# Object Management App

## Overview
Android application for managing objects with attributes and relationships. 
The app allows users to create, edit, delete, and search for objects. 
It follows a clean architecture with MVVM pattern and utilizes Jetpack Compose for the UI.

## Technologies Used
- **Kotlin**: As the primary language for Android development.
- **Jetpack Compose**: Provides a declarative and flexible way to build UIs.
- **Room**: Simplifies database operations and ensures data persistence.
- **Dagger Hilt**: Reduces boilerplate code for dependency injection. Sufficient for this project.
- **Flow**: Allows for efficient and reactive data handling, uses coroutines.

## Architecture
The application follows the **Clean Architecture** with the **MVVM (Model-View-ViewModel)** pattern:
- **Data Layer**: Manages data operations using Room for database interactions.
- **Domain Layer**: Contains business logic. Due to the small size of the project, the domain layer logic is located in ViewModels.
- **Presentation Layer**: Handles UI logic and user interactions using Jetpack Compose.

## Features
- **Create, Edit, and Delete Objects**
- **Create, Edit, and Delete Relationships**
- **Search objects by their attributes**
- **Persistent Storage**

## Building and Running the Application

### Prerequisites
- **SDK**: 
- **compileSdk**: 34
- **minSdk**: 26
- **targetSdk**: 34

### Steps to Build and Run
1. **Clone the Repository**:
   git clone https://github.com/Enwinkel/object-management-app.git
2. **Open the Project in Android Studio**
3. **Sync Project**
4. **Build the Project**
5. **Run the Application**:
    Connect an Android device or start an emulator.
    Run 'app'.
