// RecipeDetailsViewModel.kt
package com.example.recipesapp.viewmodel

import androidx.lifecycle.*
import com.example.recipesapp.data.RecipeDetails
import com.example.recipesapp.network.RetrofitInstance
import kotlinx.coroutines.launch

class RecipeDetailsViewModel : ViewModel() {

    private val _recipeDetails = MutableLiveData<RecipeDetails>()
    val recipeDetails: LiveData<RecipeDetails> = _recipeDetails

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchRecipeDetails(recipeId: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val details = RetrofitInstance.api.getRecipeDetails(recipeId)
                _recipeDetails.value = details
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки деталей рецепта: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}
