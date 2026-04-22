package pl.wsei.pam.lab07.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import pl.wsei.pam.lab07.data.TodoTask
import pl.wsei.pam.lab07.data.TodoTaskRepository

class ListViewModel(val repository: TodoTaskRepository) : ViewModel() {
    val listUiState: StateFlow<ListUiState> = repository.getAllAsStream()
        .map { ListUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ListUiState()
        )
}

data class ListUiState(val items: List<TodoTask> = listOf())
