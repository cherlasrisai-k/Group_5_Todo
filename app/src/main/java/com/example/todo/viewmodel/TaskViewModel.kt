package com.example.todo.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
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
import com.example.todo.States.HomeUiState
import com.example.todo.States.TasksUiState
import com.example.todo.States.AddEditUiState
import com.example.todo.worker.ReminderWorker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit

class TaskViewModel(
    private val dao: TaskDao,
    private val context: Context
) : ViewModel() {



    private val currentUser = MutableStateFlow<String?>(null)

    fun setUser(mobile: String) {
        currentUser.value = mobile
    }



    @OptIn(ExperimentalCoroutinesApi::class)
    val todayTasks :StateFlow<List<Task>>  = currentUser.flatMapLatest { mobile ->
        if (mobile == null) flowOf(emptyList())
        else dao.todayTasks(mobile)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val completedTasks = currentUser.flatMapLatest { mobile ->
        if (mobile == null) flowOf(emptyList())
        else dao.completedTasks(mobile)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())


    private val _homeState = MutableStateFlow(HomeUiState())
    val homeState = _homeState.asStateFlow()

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
        _events.emit("Task-${task.topic} added successfully ✅")
    }


    private var editingTask: Task? = null
    private val _addEditState = MutableStateFlow(AddEditUiState())
    val addEditState = _addEditState.asStateFlow()

    private val _activeUiState = MutableStateFlow(TasksUiState())
    val activeUiState = _activeUiState.asStateFlow()

    fun startEdit(task: Task) {
        editingTask = task
        _addEditState.value = AddEditUiState(
            topic = task.topic,
            heading = task.heading,
            dateTime = task.dateTime
        )
        _activeUiState.value = TasksUiState(showDialog = true, editingTask=task)
    }

    fun onEditTopicChange(value: String) {
        _addEditState.value = _addEditState.value.copy(topic = value, topicError = "")
    }

    fun onEditHeadingChange(value: String) {
        _addEditState.value = _addEditState.value.copy(heading = value, headingError = "")
    }

    fun onEditDateTimeChange(value: Long) {
        _addEditState.value = _addEditState.value.copy(dateTime = value, timeError = "")
    }

    fun updateEditedTask() = viewModelScope.launch {
        val editingTask = _activeUiState.value.editingTask ?: return@launch
        val s = _addEditState.value

        var topicErr = ""
        var headingErr = ""
        var timeErr = ""

        if (s.topic.isBlank()) topicErr = "Topic cannot be empty"
        if (s.heading.isBlank()) headingErr = "Heading cannot be empty"
        if (s.dateTime <= System.currentTimeMillis()) timeErr = "Please select a future time"

        if (topicErr.isNotEmpty() || headingErr.isNotEmpty() || timeErr.isNotEmpty()) {
            _addEditState.value = s.copy(
                topicError = topicErr,
                headingError = headingErr,
                timeError = timeErr
            )
            return@launch
        }

        val updatedTask = editingTask.copy(
            topic = s.topic,
            heading = s.heading,
            dateTime = s.dateTime
        )

        dao.update(updatedTask)
        _events.emit("Task-${updatedTask.topic} updated ✏️")

        _addEditState.value = AddEditUiState()
        onDialogDismiss()
    }

    fun onDialogDismiss() {
        editingTask = null
        _addEditState.value = AddEditUiState()
        _activeUiState.value = _activeUiState.value.copy(showDialog = false,editingTask=null)
    }


    fun completeTask(task: Task) = viewModelScope.launch {
        dao.update(task.copy(isCompleted = true))
        _events.emit("Hurray! Task-${task.topic} completed 🎉")
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        dao.delete(task)
        _events.emit("Task-${task.topic} deleted 🗑️")
    }



    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()



    private fun scheduleReminder(task: Task) {
        val reminderTime = task.dateTime - (5 * 60 * 1000)
        val delay = reminderTime - System.currentTimeMillis()
        if (delay <= 0) return

        val data = workDataOf(
            "topic" to task.topic,
            "heading" to task.heading
        )

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }
}


