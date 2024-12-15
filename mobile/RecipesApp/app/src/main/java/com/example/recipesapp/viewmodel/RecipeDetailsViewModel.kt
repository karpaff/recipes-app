package com.example.recipesapp.viewmodel

import androidx.lifecycle.*
import com.example.recipesapp.network.SessionManager
import com.example.recipesapp.data.model.RecipeDetails
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.data.repository.UserRepository
import kotlinx.coroutines.launch

sealed class RecipeDetailsState {
    object Loading : RecipeDetailsState()
    data class Success(val recipe: RecipeDetails) : RecipeDetailsState()
    data class Error(val message: String) : RecipeDetailsState()
}

class RecipeDetailsViewModel(
    private val recipesRepository: RecipesRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _recipeDetailsState = MutableLiveData<RecipeDetailsState>()
    val recipeDetailsState: LiveData<RecipeDetailsState> = _recipeDetailsState

    private val _favoriteState = MutableLiveData<Boolean>()
    val favoriteState: LiveData<Boolean> = _favoriteState


    fun fetchRecipeDetails(recipeId: String) {
        _recipeDetailsState.value = RecipeDetailsState.Loading
        viewModelScope.launch {
            try {
                val token = sessionManager.fetchAuthToken()
                val recipe = recipesRepository.getRecipeDetails(token, recipeId)
                _recipeDetailsState.value = RecipeDetailsState.Success(recipe)
                _favoriteState.value = recipe.isFavourite
            } catch (e: Exception) {
                _recipeDetailsState.value = RecipeDetailsState.Error("Error: ${e.message}")
            }
        }
    }

    fun addToFavorites(token: String, recipeId: String) {
        viewModelScope.launch {
            try {
                userRepository.addToFavorites(token, recipeId)
                _favoriteState.value = true
            } catch (e: Exception) {
                _recipeDetailsState.value = RecipeDetailsState.Error("Failed to add to favorites: ${e.message}")
            }
        }
    }

    fun removeFromFavorites(token: String, recipeId: String) {
        viewModelScope.launch {
            try {
                userRepository.removeFromFavorites(token, recipeId)
                _favoriteState.value = false
            } catch (e: Exception) {
                _recipeDetailsState.value = RecipeDetailsState.Error("Failed to remove from favorites: ${e.message}")
            }
        }
    }
}
