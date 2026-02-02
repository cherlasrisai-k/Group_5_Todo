package com.example.todo.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.Task
import com.example.todo.data.TaskDao
import com.example.todo.states.AddEditUiState
import com.example.todo.states.TasksUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val dao: TaskDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val currentUser: StateFlow<String?> = savedStateHandle.getStateFlow("user", null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val todayTasks: StateFlow<List<Task>> = currentUser.flatMapLatest { mobile ->
        if (mobile == null) flowOf(emptyList())
        else dao.todayTasks(mobile)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _addEditState = MutableStateFlow(AddEditUiState())
    val addEditState = _addEditState.asStateFlow()

    private val _activeUiState = MutableStateFlow(TasksUiState())
    val activeUiState = _activeUiState.asStateFlow()

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    fun startEdit(task: Task) {
        _addEditState.value = AddEditUiState(
            topic = task.topic,
            heading = task.heading,
            dateTime = task.dateTime
        )
        _activeUiState.value = TasksUiState(showDialog = true, editingTask = task)
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
        if (s.heading.isBlank()) headingErr = "Description cannot be empty"
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
        _events.emit("Task updated !!")

        _addEditState.value = AddEditUiState()
        onDialogDismiss()
    }

    fun onDialogDismiss() {
        _addEditState.value = AddEditUiState()
        _activeUiState.value = _activeUiState.value.copy(showDialog = false, editingTask = null)
    }

    fun completeTask(task: Task) = viewModelScope.launch {
        dao.update(task.copy(isCompleted = true, completionTime = System.currentTimeMillis()))
        _events.emit("Hurray! Task completed !!")
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        dao.delete(task)
        _events.emit("Task deleted !!")
    }
}
