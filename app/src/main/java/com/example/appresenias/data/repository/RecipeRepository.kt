/*
 * Integrantes: Mirko Pino, Gabriel Nercelles, Cristobal Paredes
 */
package com.example.appresenias.data.repository

import com.example.appresenias.data.remote.RecipeApiService
import com.squareup.moshi.JsonClass
import javax.inject.Inject
import javax.inject.Singleton

@JsonClass(generateAdapter = true)
data class RecipeSearchResult(val meals: List<RecipeSummary>?)

@JsonClass(generateAdapter = true)
data class RecipeSummary(val idMeal: String, val strMeal: String, val strMealThumb: String)

@JsonClass(generateAdapter = true)
data class RecipeDetailResult(val meals: List<RecipeDetail>?)

@JsonClass(generateAdapter = true)
data class RecipeDetail(
    val idMeal: String,
    val strMeal: String,
    val strCategory: String?,
    val strArea: String?,
    val strInstructions: String?,
    val strMealThumb: String?,
    val strIngredient1: String?, val strMeasure1: String?,
    val strIngredient2: String?, val strMeasure2: String?,
    val strIngredient3: String?, val strMeasure3: String?,
    val strIngredient4: String?, val strMeasure4: String?,
    val strIngredient5: String?, val strMeasure5: String?,
    val strIngredient6: String?, val strMeasure6: String?,
    val strIngredient7: String?, val strMeasure7: String?,
    val strIngredient8: String?, val strMeasure8: String?,
    val strIngredient9: String?, val strMeasure9: String?,
    val strIngredient10: String?, val strMeasure10: String?,
    val strIngredient11: String?, val strMeasure11: String?,
    val strIngredient12: String?, val strMeasure12: String?,
    val strIngredient13: String?, val strMeasure13: String?,
    val strIngredient14: String?, val strMeasure14: String?,
    val strIngredient15: String?, val strMeasure15: String?,
    val strIngredient16: String?, val strMeasure16: String?,
    val strIngredient17: String?, val strMeasure17: String?,
    val strIngredient18: String?, val strMeasure18: String?,
    val strIngredient19: String?, val strMeasure19: String?,
    val strIngredient20: String?, val strMeasure20: String?
) {
    fun getIngredientsWithMeasures(): List<Pair<String, String>> {
        val ingredients = mutableListOf<Pair<String, String>>()
        val properties = this::class.java.declaredFields

        for (i in 1..20) {
            val ingredientName = properties.find { it.name == "strIngredient$i" }?.get(this) as? String
            val measure = properties.find { it.name == "strMeasure$i" }?.get(this) as? String

            if (!ingredientName.isNullOrBlank()) {
                ingredients.add(Pair(ingredientName, measure.orEmpty()))
            }
        }
        return ingredients
    }
}

@Singleton
class RecipeRepository @Inject constructor(
    private val recipeApiService: RecipeApiService
) {

    suspend fun buscarRecetas(name: String): RecipeSearchResult {
        return recipeApiService.searchRecipesByName(name)
    }

    suspend fun obtenerRecetaPorId(id: String): RecipeDetailResult {
        return recipeApiService.getRecipeById(id)
    }
}
