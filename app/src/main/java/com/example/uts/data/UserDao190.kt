package com.example.uts.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao190 {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser190(user: User190): Long

    @Query("SELECT * FROM users_190 WHERE username190 = :username LIMIT 1")
    fun getUserByUsername190(username: String): User190?

    @Query("SELECT * FROM users_190 WHERE username190 = :username AND password190 = :password LIMIT 1")
    fun loginUser190(username: String, password: String): User190?
}
