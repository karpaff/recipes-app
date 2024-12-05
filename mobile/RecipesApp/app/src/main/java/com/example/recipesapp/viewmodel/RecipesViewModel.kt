package com.example.recipesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.Recipe
import com.example.recipesapp.network.RetrofitInstance
import kotlinx.coroutines.launch

class RecipesViewModel : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        fetchRecipes()
    }

    fun fetchRecipes() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val fetchedRecipes = RetrofitInstance.api.getRecipes()
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
