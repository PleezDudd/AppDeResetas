package com.example.appresenias.data.remote

import com.example.appresenias.data.repository.RecipeDetailResult
import com.example.appresenias.data.repository.RecipeSearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApiService {

    @GET("search.php")
    suspend fun searchRecipesByName(@Query("s") query: String): RecipeSearchResult

    @GET("lookup.php")
    suspend fun getRecipeById(@Query("i") id: String): RecipeDetailResult
}
