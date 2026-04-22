package pl.wsei.pam.lab07.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import pl.wsei.pam.lab07.data.CurrentDateProvider
import pl.wsei.pam.lab07.data.Priority
import pl.wsei.pam.lab07.data.TodoTask
import pl.wsei.pam.lab07.data.TodoTaskRepository
import java.time.LocalDate

data class TodoTaskForm(
    val id: Int = 0,
    val title: String = "",
    val deadline: LocalDate = LocalDate.now(),
    val isDone: Boolean = false,
    val priority: Priority = Priority.Low
)

fun TodoTaskForm.toTodoTask(): TodoTask = TodoTask(
    id = id,
    title = title,
    deadline = deadline,
    isDone = isDone,
    priority = priority
)

data class TodoTaskUiState(
    val todoTask: TodoTaskForm = TodoTaskForm(),
    val isValid: Boolean = false
)

class FormViewModel(
    private val repository: TodoTaskRepository,
    private val currentDateProvider: CurrentDateProvider
) : ViewModel() {
    var todoTaskUiState by mutableStateOf(TodoTaskUiState())
        private set

    fun updateUiState(todoTaskForm: TodoTaskForm) {
        todoTaskUiState = TodoTaskUiState(
            todoTask = todoTaskForm,
            isValid = validate(todoTaskForm)
        )
    }

    suspend fun save() {
        if (validate()) {
            repository.insertItem(todoTaskUiState.todoTask.toTodoTask())
        }
    }

    private fun validate(uiState: TodoTaskForm = todoTaskUiState.todoTask): Boolean {
        return uiState.title.isNotBlank() && 
               !uiState.deadline.isBefore(currentDateProvider.now())
    }
}
