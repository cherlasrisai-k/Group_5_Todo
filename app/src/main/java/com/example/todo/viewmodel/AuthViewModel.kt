package com.example.todo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.User
import com.example.todo.data.UserDao
import kotlinx.coroutines.launch

 class AuthViewModel(private val dao: UserDao) : ViewModel() {

     var user by mutableStateOf<User?>(null)
        private set

     var loginError by mutableStateOf<String?>(null)
         private set

     var RegisterError by mutableStateOf<String?>(null)
         private set

    fun login(mobile: String) = viewModelScope.launch {
        val result = dao.login(mobile)
        if (result == null) {
            loginError = "User does not exist. Please register."
        } else {
            user = result
            loginError = null
        }
    }

     fun register(name: String, mobile: String) = viewModelScope.launch {
         RegisterError = null
         val existingUser = dao.login(mobile)

         if (existingUser == null) {
             // New user → insert
             dao.register(User(mobile, name))

             // Fetch again after insert
             user = dao.login(mobile)
             //Log.d("REGISTER_DEBUG", "New user created = $user")


         } else {
             // Already exists
             user = null
             RegisterError = "User already exists. Please login."
             //Log.d("REGISTER_DEBUG", "User already exists")
         }
     }


     fun logout() {
        user = null
    }

     fun validateAndLogin(mobile: String) {
         when {
             mobile.isBlank() -> loginError = "Enter mobile number"
             mobile.length != 10 -> loginError = "Enter valid 10 digit number"
             else -> login(mobile)
         }
     }


         fun validateAndRegister(name: String, mobile: String) {
             //Log.d("VM_TEST", "Register clicked: $name , $mobile")

             RegisterError = null
             //Log.d("REGISTER_DEBUG", "Clicked: $name , $mobile")

             when {
                 name.isBlank() -> RegisterError = "Name cannot be empty"
                 mobile.length != 10 -> RegisterError = "Enter a valid 10-digit mobile number"
                 else -> register(name, mobile)
             }
         }

     }



