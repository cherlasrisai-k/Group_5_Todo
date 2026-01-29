package com.example.todo.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.todo.data.AppDatabase
import com.example.todo.data.Task
import com.example.todo.data.TaskDao
import com.example.todo.worker.ReminderWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

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
class TaskViewModel(private val dao: TaskDao,private val context: Context) : ViewModel() {

    private val currentUser = MutableStateFlow<String?>(null)

    val todayTasks = currentUser.flatMapLatest { mobile ->
        if (mobile == null) flowOf(emptyList())
        else dao.todayTasks(mobile)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val completedTasks = currentUser.flatMapLatest { mobile ->
        if (mobile == null) flowOf(emptyList())
        else dao.completedTasks(mobile)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun setUser(mobile: String) {
        currentUser.value = mobile
    }

//    fun addTask(topic: String, heading: String, dateTime: Long) =
//        viewModelScope.launch {
//            currentUser.value?.let {
//                dao.insert(Task(userMobile = it, topic = topic, heading = heading, dateTime = dateTime))
//            }
//        }
fun addTask(topic: String, heading: String, dateTime: Long) =
    viewModelScope.launch {

        currentUser.value?.let { mobile ->

            val task = Task(
                userMobile = mobile,
                topic = topic,
                heading = heading,
                dateTime = dateTime
            )

            dao.insert(task)

            scheduleReminder(task)
        }
    }
    private fun scheduleReminder(task: Task) {
        //5 minutes before the task 5 min = 300000 ms
        val reminderTime=task.dateTime-(5*60*1000)
        //calculate delay time
        val delay =reminderTime - System.currentTimeMillis()
        //If task is already passes, don't schedule
        if (delay <= 0) return // skip past tasks
        //worker needs to know what to show in notification. Task topic and task heading
        val data = workDataOf(
            "topic" to task.topic,
            "heading" to task.heading
        )
       //create one-time reminder request
        val request = OneTimeWorkRequestBuilder<ReminderWorker>()//runs only once at scheduled time
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)//Exact timings
            .setInputData(data)//Task details passed
            .build()
        //enqueue work in workmanager
        WorkManager.getInstance(context)//WorkManager will remember this task and execute ReminderWorker exactly after delay even if app is closed
            .enqueue(request)
    }


    fun updateTask(task: Task) = viewModelScope.launch { dao.update(task) }
    fun deleteTask(task: Task) = viewModelScope.launch { dao.delete(task) }
    fun completeTask(task: Task) =
        viewModelScope.launch { dao.update(task.copy(isCompleted = true)) }
}

