package com.example.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.todo.ui.HomeUiState
import kotlinx.coroutines.flow.*

class TaskViewModel(private val dao: TaskDao) : ViewModel() {

    // ---------------- USER STATE ----------------

    private val currentUser = MutableStateFlow<String?>(null)

    fun setUser(mobile: String) {
        currentUser.value = mobile
    }

    // ---------------- TASK FLOWS ----------------

    val todayTasks = currentUser.flatMapLatest { mobile ->
        if (mobile == null) flowOf(emptyList())
        else dao.todayTasks(mobile, System.currentTimeMillis())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedTasks = currentUser.flatMapLatest { mobile ->
        if (mobile == null) flowOf(emptyList())
        else dao.completedTasks(mobile)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ---------------- HOME UI STATE ----------------

    private val _homeState = MutableStateFlow(HomeUiState())
    val homeState = _homeState.asStateFlow()

    fun onTopicChange(value: String) {
        _homeState.value = _homeState.value.copy(
            topic = value,
            topicError = ""
        )
    }

    fun onHeadingChange(value: String) {
        _homeState.value = _homeState.value.copy(
            heading = value,
            headingError = ""
        )
    }

    fun onDateTimeChange(value: Long) {
        _homeState.value = _homeState.value.copy(
            dateTime = value,
            timeError = ""
        )
    }

    // ---------------- EVENTS ----------------

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    // ---------------- ACTIONS ----------------

    fun saveTask() = viewModelScope.launch {

        val s = _homeState.value

        var topicErr = ""
        var headingErr = ""
        var timeErr = ""

        if (s.topic.isBlank()) topicErr = "Topic cannot be empty"
        if (s.heading.isBlank()) headingErr = "Heading cannot be empty"
        if (s.dateTime <= System.currentTimeMillis()) timeErr = "Please select a future time"

        if (topicErr.isNotEmpty() || headingErr.isNotEmpty() || timeErr.isNotEmpty()) {
            _homeState.value = s.copy(
                topicError = topicErr,
                headingError = headingErr,
                timeError = timeErr
            )
            return@launch
        }

        currentUser.value?.let {
            dao.insert(
                Task(
                    userMobile = it,
                    topic = s.topic,
                    heading = s.heading,
                    dateTime = s.dateTime
                )
            )
        }

        _homeState.value = HomeUiState()
        _events.emit("Task added successfully ✅")
    }

    fun completeTask(task: Task) = viewModelScope.launch {
        dao.update(task.copy(isCompleted = true))
        _events.emit("Hurray! Task Completed 🎉")
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        dao.delete(task)
        _events.emit("Task deleted")
    }
    fun updateTask(task: Task) = viewModelScope.launch { dao.update(task)
        _events.emit("Task Updated")}
}
