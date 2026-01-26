package com.example.todo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.AppDatabase
import com.example.todo.data.Task
import com.example.todo.data.TaskDao
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

//class TaskViewModel(app: Application) : AndroidViewModel(app) {
//
//    private val dao = AppDatabase.get(app).taskDao()
//
//    val todayTasks = dao.todayTasks()
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
//
//    val completedTasks = dao.completedTasks()
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
//
//    fun addTask(topic: String, heading: String, dateTime: Long) =
//        viewModelScope.launch {
//            dao.insert(Task(topic = topic, heading = heading, dateTime = dateTime))
//        }
//
//    fun updateTask(task: Task) =
//        viewModelScope.launch { dao.update(task) }
//
//    fun deleteTask(task: Task) =
//        viewModelScope.launch { dao.delete(task) }
//
//    fun completeTask(task: Task) =
//        viewModelScope.launch { dao.update(task.copy(isCompleted = true)) }
//}
class TaskViewModel(private val dao: TaskDao) : ViewModel() {

    private val currentUser = MutableStateFlow<String?>(null)

    val todayTasks = currentUser.flatMapLatest { mobile ->
        if (mobile == null) flowOf(emptyList())
        else dao.todayTasks(mobile, System.currentTimeMillis())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val completedTasks = currentUser.flatMapLatest { mobile ->
        if (mobile == null) flowOf(emptyList())
        else dao.completedTasks(mobile)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun setUser(mobile: String) {
        currentUser.value = mobile
    }

    fun addTask(topic: String, heading: String, dateTime: Long) =
        viewModelScope.launch {
            currentUser.value?.let {
                dao.insert(Task(userMobile = it, topic = topic, heading = heading, dateTime = dateTime))
            }
        }

    fun updateTask(task: Task) = viewModelScope.launch { dao.update(task) }

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    fun completeTask(task: Task) = viewModelScope.launch {
        dao.update(task.copy(isCompleted = true))
        _events.emit("Hurray! Task Completed 🎉")
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        dao.delete(task)
        _events.emit("Task Deleted")
    }
}
