package com.example

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "passwords")
data class Password(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val password: String,
    val entropy: Double,
    val charsetUsed: String,
    val folderName: String = "Default"
)