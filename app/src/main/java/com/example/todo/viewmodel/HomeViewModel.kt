package com.example.todo.viewmodel

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.todo.data.Task
import com.example.todo.data.TaskDao
import com.example.todo.states.HomeUiState
import com.example.todo.worker.ReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dao: TaskDao,
    savedStateHandle: SavedStateHandle,
    private val application: Application
) : ViewModel() {

    private val currentUser: StateFlow<String?> = savedStateHandle.getStateFlow("user", null)

    private val _homeState = MutableStateFlow(HomeUiState())
    val homeState = _homeState.asStateFlow()

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    fun onTopicChange(value: String) {
        _homeState.value = _homeState.value.copy(topic = value, topicError = "")
    }

    fun onHeadingChange(value: String) {
        _homeState.value = _homeState.value.copy(heading = value, headingError = "")
    }

    fun onDateTimeChange(value: Long) {
        _homeState.value = _homeState.value.copy(dateTime = value, timeError = "")
    }

    fun saveTask() = viewModelScope.launch {
        val s = _homeState.value

        var topicErr = ""
        var headingErr = ""
        var timeErr = ""

        if (s.topic.isBlank()) topicErr = "Topic cannot be empty"
        if (s.heading.isBlank()) headingErr = "Description cannot be empty"
        if (s.dateTime <= System.currentTimeMillis()) timeErr = "Please select a future time"

        if (topicErr.isNotEmpty() || headingErr.isNotEmpty() || timeErr.isNotEmpty()) {
            _homeState.value = s.copy(
                topicError = topicErr,
                headingError = headingErr,
                timeError = timeErr
            )
            return@launch
        }

        val userMobile = currentUser.value ?: return@launch

        val task = Task(
            userMobile = userMobile,
            topic = s.topic,
            heading = s.heading,
            dateTime = s.dateTime
        )

        dao.insert(task)
        scheduleReminder(task)

        _homeState.value = HomeUiState()
        _events.emit("Task added successfully !!")
    }

    private fun scheduleReminder(task: Task) {
        val reminderTime = task.dateTime - (5 * 60 * 1000)
        var delay = reminderTime - System.currentTimeMillis()
        if (reminderTime < System.currentTimeMillis()) {
            delay = 0
        }

        val data = workDataOf(
            "topic" to task.topic,
            "heading" to task.heading
        )

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(application).enqueue(request)
    }
}
