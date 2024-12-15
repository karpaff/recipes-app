package com.example.recipesapp.data.repository

import com.example.recipesapp.network.*

class UserRepository(private val api: RecipesApi) {

    suspend fun register(request: RegisterRequest): ApiResponse {
        val response = api.registerUser(request)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response")
        } else {
            throw Exception("Registration failed: ${response.message()}")
        }
    }

    suspend fun login(request: LoginRequest): LoginResponse {
        val response = api.login(request)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response")
        } else {
            throw Exception("Login failed: ${response.message()}")
        }
    }

    suspend fun getFavorites(token: String): List<RecipeResponse> {
        val response = api.getFavorites("Bearer $token")
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch favorites: ${response.message()}")
        }
    }

    suspend fun addToFavorites(token: String, recipeId: String) {
        val response = api.addToFavorites(
            token = "Bearer $token",
            body = AddToFavoritesRequest(recipeId)
        )
        if (!response.isSuccessful) {
            throw Exception("Failed to add to favorites: ${response.message()}")
        }
    }

    suspend fun removeFromFavorites(token: String, recipeId: String) {
        val response = api.removeFromFavorites(
            token = "Bearer $token",
            recipeId = recipeId
        )
        if (!response.isSuccessful) {
            throw Exception("Failed to remove from favorites: ${response.message()}")
        }
    }
}
