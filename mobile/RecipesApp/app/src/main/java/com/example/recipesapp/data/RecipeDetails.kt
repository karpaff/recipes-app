package com.example.recipesapp.data

data class RecipeDetails(
    val _id: String,
    val picture: String,
    val name: String,
    val description: String,
    val createdAt: String,
    val timeToPrepare: Int,
    val ingredients: List<Ingredient>,
    val instructions: List<String>,
    val difficulty: String
)