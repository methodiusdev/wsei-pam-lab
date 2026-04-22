package pl.wsei.pam.lab07

import android.content.Context
import pl.wsei.pam.lab07.data.*

interface AppContainer {
    val todoTaskRepository: TodoTaskRepository
    val currentDateProvider: CurrentDateProvider
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val todoTaskRepository: TodoTaskRepository by lazy {
        DatabaseTodoTaskRepository(AppDatabase.getInstance(context).taskDao())
    }
    
    override val currentDateProvider: CurrentDateProvider by lazy {
        RealCurrentDateProvider()
    }
}
