package pl.wsei.pam.lab07.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

enum class Priority {
    High, Medium, Low
}

@Entity(tableName = "tasks")
data class TodoTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val deadline: LocalDate = LocalDate.now(),
    var isDone: Boolean = false,
    val priority: Priority = Priority.Low
)
