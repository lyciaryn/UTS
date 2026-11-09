package com.example.uts.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User190::class, Task190::class], version = 1, exportSchema = false)
abstract class AppDatabase190 : RoomDatabase() {

    abstract fun userDao190(): UserDao190
    abstract fun taskDao190(): TaskDao190

    companion object {
        @Volatile
        private var INSTANCE190: AppDatabase190? = null

        fun getDatabase190(context: Context): AppDatabase190 {
            return INSTANCE190 ?: synchronized(this) {
                val instance190 = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase190::class.java,
                    "task_management_db_190"
                ).build()
                INSTANCE190 = instance190
                instance190
            }
        }
    }
}
