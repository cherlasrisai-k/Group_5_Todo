# Group_5_Todo

# About Project 

This project is an Android To-Do application built using Kotlin and Jetpack Compose. It allows users to create tasks with a topic, description, date, and time, and view active tasks for the current day. Users can update, delete, and mark tasks as completed, which are then moved to a history screen. The app sends automatic push notifications every one hour reminding users about the current day’s active tasks. It uses Room database for local storage, WorkManager for background reminders, and a bottom navigation bar for easy screen access.

# Project Structure

data/

    User.kt
    Task.kt
    UserDao.kt
    TaskDao.kt
    AppDatabase.kt

navigation/

    AppNavGraph.kt
    BottomNavItem.kt
    MainNavItem.kt
    Routes.kt
session/

    SessionManager.kt

States/

    ActiveTasksUiState.kt
    AddEditUiState.kt
    HomeScreenStates.kt
    LoginRegisterStates.kt
        
viewmodel/

    AuthViewModel.kt
    TaskViewModel.kt

worker/

    ReminderWorker.kt

ui/

    LoginScreen.kt
    RegisterScreen.kt
    HomeScreen.kt
    ActiveTasksScreen.kt
    HistoryScreen.kt
    AddEditDialog.kt
    BottomBar.kt
    
MainActivity.kt

MainScaffold.kt

