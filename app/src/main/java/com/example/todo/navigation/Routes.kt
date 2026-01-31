package com.example.todo.navigation

enum class Routes(val route: String) {
    SPLASH("splash"),
    LOGIN("login"),
    REGISTER("register"),
    MAIN("main/{user}"),
    HOME("home"),
    ACTIVE("tasks"),
    HISTORY("history");

    fun withUser(user: String): String {
        return "main/$user"
    }
}
