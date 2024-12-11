package com.example.recipesapp.network

import com.example.recipesapp.data.Recipe
import com.example.recipesapp.data.RecipeDetails
import com.example.recipesapp.data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipesApi {
    @GET("recipes/")
    suspend fun getRecipes(): List<Recipe>

    @GET("/recipes/{id}")
    suspend fun getRecipeDetails(@Path("id") recipeId: String): RecipeDetails

    @POST("/user/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<ApiResponse>

    @POST("/user/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/user/favourites")
    suspend fun addToFavorites(
        @Header("Authorization") token: String,
        @Body body: AddToFavoritesRequest
    ): Response<Unit>

    @DELETE("/user/favourites")
    suspend fun removeFromFavorites(
        @Header("Authorization") token: String,
        @Query("recipeId") recipeId: String
    ): Response<Unit>

    @GET("/user/favourites/")
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
