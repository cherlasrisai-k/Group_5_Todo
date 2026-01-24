package com.example.todo.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.AppDatabase
import com.example.todo.data.User
import com.example.todo.data.UserDao
import kotlinx.coroutines.launch

//class AuthViewModel(app: Application) : AndroidViewModel(app) {
//
//    private val dao = AppDatabase.get(app).userDao()
//
//    var user by mutableStateOf<User?>(null)
//        private set
//
//    fun login(mobile: String) = viewModelScope.launch {
//        user = dao.login(mobile)
//    }
//
//    fun register(name: String, mobile: String) = viewModelScope.launch {
//        dao.register(User(mobile, name))
//        user = dao.login(mobile)
//    }
//    fun logout() {
//        user = null
//    }
//}

class AuthViewModel(private val dao: UserDao) : ViewModel() {

    var user by mutableStateOf<User?>(null)
        private set

    fun login(mobile: String) = viewModelScope.launch {
        user = dao.login(mobile)
    }

    fun register(name: String, mobile: String) = viewModelScope.launch {
        dao.register(User(mobile, name))
        user = dao.login(mobile)
    }

    fun logout() {
        user = null
    }
}

