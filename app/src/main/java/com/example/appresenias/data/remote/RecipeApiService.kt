package com.example.appresenias.data.remote

import com.example.appresenias.data.remote.dto.RecipeDto
import com.example.appresenias.data.remote.dto.RecipeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApiService {

    @GET("search.php")
    suspend fun buscarRecetasPorNombre(
        @Query("s") name: String
    ): RecipeSearchResponse

    @GET("lookup.php")
    suspend fun obtenerRecetaPorId(
        @Query("i") id: String
    ): RecipeSearchResponse
}

