package com

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.Password

@Dao
interface PasswordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(password: Password)

    @Query("SELECT * FROM passwords WHERE folderName = :folderName")
    suspend fun getPasswordsInFolder(folderName: String): List<Password>

    @Query("SELECT * FROM passwords")
    suspend fun getAllPasswords(): List<Password>

    @Delete
    suspend fun delete(password: Password)
}