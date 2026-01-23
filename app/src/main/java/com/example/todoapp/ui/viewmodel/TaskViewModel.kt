package com.example.todoapp.ui.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.local.entity.TaskEntity
import com.example.todoapp.data.repository.TaskRepository
import com.example.todoapp.utils.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {
    private val _activeTasks =
        MutableStateFlow<UiState<List<TaskEntity>>>(UiState.Loading)
    val activeTasks: StateFlow<UiState<List<TaskEntity>>> = _activeTasks
    private val _completedTasks =
        MutableStateFlow<UiState<List<TaskEntity>>>(UiState.Loading)
    val completedTasks: StateFlow<UiState<List<TaskEntity>>> = _completedTasks
    fun loadActiveTasks(userId: Int) {
        viewModelScope.launch {
            repository.getActiveTasks(userId)
                .catch { _activeTasks.value = UiState.Error(it.message ?: "Error") }
                .collect { _activeTasks.value = UiState.Success(it) }
        }
    }
    fun loadCompletedTasks(userId: Int) {
        viewModelScope.launch {
            repository.getCompletedTasks(userId)
                .catch { _completedTasks.value = UiState.Error(it.message ?: "Error") }
                .collect { _completedTasks.value = UiState.Success(it) }
        }
    }
    fun addTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.addTask(task)
        }
    }
    fun markCompleted(taskId: Int) {
        viewModelScope.launch {
            repository.markTaskCompleted(taskId)
        }
    }
    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}
