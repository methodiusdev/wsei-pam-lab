package pl.wsei.pam.lab07.data

import kotlinx.coroutines.flow.Flow

interface TodoTaskRepository {
    fun getAllAsStream(): Flow<List<TodoTask>>
    fun getItemAsStream(id: Int): Flow<TodoTask?>
    suspend fun insertItem(item: TodoTask)
    suspend fun deleteItem(item: TodoTask)
    suspend fun updateItem(item: TodoTask)
}

class DatabaseTodoTaskRepository(private val dao: TodoTaskDao) : TodoTaskRepository {
    override fun getAllAsStream(): Flow<List<TodoTask>> = dao.findAll()
    override fun getItemAsStream(id: Int): Flow<TodoTask?> = dao.find(id)
    override suspend fun insertItem(item: TodoTask) = dao.insert(item)
    override suspend fun deleteItem(item: TodoTask) = dao.delete(item)
    override suspend fun updateItem(item: TodoTask) = dao.update(item)
}
