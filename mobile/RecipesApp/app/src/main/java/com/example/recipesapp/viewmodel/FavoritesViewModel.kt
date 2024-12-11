package com.example.recipesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.data.Recipe
import com.example.recipesapp.network.RetrofitInstance
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {

    private val _favorites = MutableLiveData<List<Recipe>>()
    val favorites: LiveData<List<Recipe>> = _favorites

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var allFavorites: List<Recipe> = emptyList()

    fun fetchFavorites(jwtToken: String) {
        viewModelScope.launch {
            try {
                // Fetch favorites from the API
                val response = RetrofitInstance.api.getFavorites(jwtToken)

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        allFavorites = body.map { response ->
                            Recipe(
                                _id = response._id,
                                name = response.name,
                                description = response.description,
                                picture = response.picture
                            )
                        }
                        _favorites.value = allFavorites
                    } ?: run {
                        _favorites.value = emptyList()
                    }
                } else {
                    _error.value = "Failed to fetch favorites: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }

    fun filterFavorites(query: String) {
        val filteredList = allFavorites.filter { recipe ->
            recipe.name.contains(query, ignoreCase = true) || recipe.description.contains(query, ignoreCase = true)
        }
        _favorites.value = filteredList
    }
}
