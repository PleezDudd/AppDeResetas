package com.example.appresenias.domain.model

data class Recipe(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val category: String?,
    val area: String?,
    val instructions: String?,
    val ingredients: List<Ingredient>
)

data class Ingredient(
    val name: String,
    val measure: String?
)

