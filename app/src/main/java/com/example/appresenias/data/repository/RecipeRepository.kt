package com.example.appresenias.data.repository

import com.example.appresenias.data.remote.RecipeApiService
import com.example.appresenias.data.remote.dto.RecipeDto
import com.example.appresenias.domain.model.Ingredient
import com.example.appresenias.domain.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(
    private val apiService: RecipeApiService
) {

    suspend fun buscarRecetas(nombre: String): Result<List<Recipe>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.buscarRecetasPorNombre(nombre)
                val recipes = response.meals?.mapNotNull { dto ->
                    dto.toDomainModel()
                } ?: emptyList()
                Result.success(recipes)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun obtenerRecetaPorId(id: String): Result<Recipe?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.obtenerRecetaPorId(id)
                val recipe = response.meals?.firstOrNull()?.toDomainModel()
                Result.success(recipe)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}

private fun RecipeDto.toDomainModel(): Recipe? {
    val id = idMeal ?: return null
    val name = strMeal ?: return null

    val ingredients = mutableListOf<Ingredient>()
    val ingredientFields = listOf(
        strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
        strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
        strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15,
        strIngredient16, strIngredient17, strIngredient18, strIngredient19, strIngredient20
    )
    val measureFields = listOf(
        strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
        strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
        strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15,
        strMeasure16, strMeasure17, strMeasure18, strMeasure19, strMeasure20
    )

    ingredientFields.forEachIndexed { index, ingredient ->
        if (!ingredient.isNullOrBlank()) {
            ingredients.add(
                Ingredient(
                    name = ingredient.trim(),
                    measure = measureFields[index]?.trim()
                )
            )
        }
    }

    return Recipe(
        id = id,
        name = name,
        imageUrl = strMealThumb,
        category = strCategory,
        area = strArea,
        instructions = strInstructions,
        ingredients = ingredients
    )
}

