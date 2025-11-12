package com.example.appresenias.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ResenaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(resena: Resena)

    @Query("SELECT * FROM reseñas_tabla ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<Resena>>

    @Query("SELECT * FROM reseñas_tabla WHERE recipe_id = :recipeId")
    fun obtenerPorReceta(recipeId: String): Flow<List<Resena>>

    @Query("SELECT * FROM reseñas_tabla WHERE id = :id")
    fun obtenerPorId(id: Int): Flow<Resena?>
}
