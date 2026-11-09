package com.example.uts.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_190")
data class User190(
    @PrimaryKey(autoGenerate = true)
    val id190: Int = 0,
    val username190: String,
    val password190: String
)
