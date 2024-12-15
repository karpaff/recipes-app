package com.example.recipesapp.viewmodel

import androidx.lifecycle.*
import com.example.recipesapp.data.model.Recipe
import com.example.recipesapp.data.repository.RecipesRepository
import kotlinx.coroutines.launch

class RecipesViewModel(private val repository: RecipesRepository) : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchRecipes() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val fetchedRecipes = repository.getRecipes()
                _recipes.value = fetchedRecipes
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to fetch recipes: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}
