/*
 * Integrantes: Mirko Pino, Gabriel Nercelles, Cristobal Paredes
 */
package com.example.appresenias.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Resena::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun resenaDao(): ResenaDao
}
