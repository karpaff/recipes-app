package com.example.recipesapp.data.repository

import com.example.recipesapp.data.model.Recipe
import com.example.recipesapp.data.model.RecipeDetails
import com.example.recipesapp.network.RecipesApi

class RecipesRepository(private val api: RecipesApi) {

    suspend fun getRecipes(): List<Recipe> {
        val response = api.getRecipes()

        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch recipes: ${response.message()}")
        }
    }


    suspend fun getRecipeDetails(token: String?, recipeId: String): RecipeDetails {
        val response = api.getRecipeDetails("Bearer $token", recipeId)

        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Recipe details not found")
        } else {
            throw Exception("Failed to fetch recipe details: ${response.message()}")
        }
    }
}
