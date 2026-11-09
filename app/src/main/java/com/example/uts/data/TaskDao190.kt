package com.example.uts.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao190 {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask190(task: Task190): Long

    @Update
    fun updateTask190(task: Task190)

    @Delete
    fun deleteTask190(task: Task190)

    @Query("SELECT * FROM tasks_190 WHERE user_id = :userId ORDER BY isDone190 ASC, id190 DESC")
    fun getTasksByUserId190(userId: Int): List<Task190>

    @Query("SELECT * FROM tasks_190 WHERE user_id = :userId AND namaTugas190 LIKE '%' || :searchQuery || '%' ORDER BY isDone190 ASC, id190 DESC")
    fun searchTasks190(userId: Int, searchQuery: String): List<Task190>

    @Query("SELECT * FROM tasks_190 WHERE user_id = :userId ORDER BY CASE prioritas190 WHEN 'Tinggi' THEN 1 WHEN 'Sedang' THEN 2 WHEN 'Rendah' THEN 3 ELSE 4 END ASC, isDone190 ASC")
    fun getTasksSortedByPriority190(userId: Int): List<Task190>

    @Query("UPDATE tasks_190 SET isDone190 = :isDone WHERE id190 = :taskId")
    fun updateTaskStatus190(taskId: Int, isDone: Boolean)
}
