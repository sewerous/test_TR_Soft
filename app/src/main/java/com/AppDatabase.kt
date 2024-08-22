package com

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.Password

@Database(entities = [Password::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun passwordDao(): PasswordDao
}