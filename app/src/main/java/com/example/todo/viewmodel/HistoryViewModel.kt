package com.example.todo.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.Task
import com.example.todo.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val dao: TaskDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val currentUser: StateFlow<String?> = savedStateHandle.getStateFlow("user", null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val completedTasks: StateFlow<List<Task>> = currentUser.flatMapLatest { mobile ->
        if (mobile == null) flowOf(emptyList())
        else dao.completedTasks(mobile)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun clearHistory() = viewModelScope.launch {
        currentUser.value?.let { dao.deleteCompletedTasks(it) }
    }
}
