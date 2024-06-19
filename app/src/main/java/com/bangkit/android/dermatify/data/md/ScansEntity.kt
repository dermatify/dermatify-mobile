package com.bangkit.android.dermatify.data.md

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ScansHistory")
data class ScansEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageUri: String,
    val description: String,
    val timestamp: String,
    val diagnosis: String
)
