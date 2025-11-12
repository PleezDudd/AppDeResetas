package com.example.appresenias.data.repository

import com.example.appresenias.data.local.Resena
import com.example.appresenias.data.local.ResenaDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResenaRepository @Inject constructor(private val resenaDao: ResenaDao) {

    fun obtenerTodas(): Flow<List<Resena>> {
        return resenaDao.obtenerTodas()
    }

    suspend fun insertar(resena: Resena) {
        resenaDao.insertar(resena)
    }
}