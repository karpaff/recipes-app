package com.example.recipesapp.network

import com.example.recipesapp.data.model.Recipe
import com.example.recipesapp.data.model.RecipeDetails
import com.example.recipesapp.data.model.User
import retrofit2.Response
import retrofit2.http.*

interface RecipesApi {

    // Получение списка рецептов
    @GET("recipes/")
    suspend fun getRecipes(): Response<List<Recipe>>

    // Получение деталей рецепта по id
    @GET("recipes/{id}")
    suspend fun getRecipeDetails(
        @Header("Authorization") token: String?,
        @Path("id") recipeId: String
    ): Response<RecipeDetails>

    // Регистрация нового пользователя
    @POST("user/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<ApiResponse>

    // Вход пользователя
    @POST("user/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // Добавление рецепта в избранное
    @POST("user/favourites")
    suspend fun addToFavorites(
        @Header("Authorization") token: String,
        @Body body: AddToFavoritesRequest
    ): Response<Unit>

    // Удаление рецепта из избранного
    @DELETE("user/favourites")
    suspend fun removeFromFavorites(
        @Header("Authorization") token: String,
        @Query("recipeId") recipeId: String
    ): Response<Unit>

    // Получение списка избранных рецептов
    @GET("user/favourites/")
    suspend fun getFavorites(@Header("Authorization") token: String): Response<List<RecipeResponse>>
}

data class LoginRequest(
    val login: String,
    val password: String
)

data class RecipeResponse(
    val _id: String,
    val name: String,
    val description: String,
    val picture: String
)

data class ApiResponse(
    val message: String
)

data class LoginResponse(
    val token: String,
    val user: User
)

data class AddToFavoritesRequest(
    val recipeId: String
)

data class RegisterRequest(
    val login: String,
    val password: String
)
