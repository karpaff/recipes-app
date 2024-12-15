package com.example.recipesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipesapp.network.SessionManager
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.data.repository.UserRepository

class AppViewModelFactory(
    private val recipesRepository: RecipesRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RecipeDetailsViewModel::class.java) -> {
                RecipeDetailsViewModel(recipesRepository, userRepository, sessionManager) as T
            }
            modelClass.isAssignableFrom(RecipesViewModel::class.java) -> {
                RecipesViewModel(recipesRepository) as T
            }
            modelClass.isAssignableFrom(FavoritesViewModel::class.java) -> {
                FavoritesViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository, sessionManager) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
