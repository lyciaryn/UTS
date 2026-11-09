package com.example.uts.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks_190",
    foreignKeys = [
        ForeignKey(
            entity = User190::class,
            parentColumns = ["id190"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Task190(
    @PrimaryKey(autoGenerate = true)
    val id190: Int = 0,
    @ColumnInfo(name = "user_id")
    val userId190: Int,
    val namaTugas190: String,
    val prioritas190: String,
    val deadline190: String,
    val isDone190: Boolean = false
)
