package com.example.appresenias.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reseñas_tabla")
data class Resena(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "recipe_id")
    val recipeId: String,

    @ColumnInfo(name = "photo_uri")
    val photoUri: String,

    @ColumnInfo(name = "rating")
    val rating: Int,

    @ColumnInfo(name = "comment")
    val comment: String
)