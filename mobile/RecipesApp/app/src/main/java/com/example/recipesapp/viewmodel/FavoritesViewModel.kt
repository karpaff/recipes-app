package com.example.recipesapp.viewmodel

import androidx.lifecycle.*
import com.example.recipesapp.network.SessionManager
import com.example.recipesapp.data.model.Recipe
import com.example.recipesapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _state = MutableLiveData<FavoritesState>()
    val state: LiveData<FavoritesState> = _state

    private var allFavorites: List<Recipe> = emptyList()

    fun fetchFavorites(token: String) {
        _state.value = FavoritesState.Loading
        viewModelScope.launch {
            try {
                val favorites = userRepository.getFavorites(token).map {
                    Recipe(
                        _id = it._id,
                        name = it.name,
                        description = it.description,
                        picture = it.picture,
                        isFavourite = true
                    )
                }
                allFavorites = favorites
                _state.value = FavoritesState.Success(favorites)
            } catch (e: Exception) {
                _state.value = FavoritesState.Error("Failed to fetch favorites: ${e.message}")
            }
        }
    }

    fun filterFavorites(query: String) {
        val filteredList = if (query.isEmpty()) {
            allFavorites
        } else {
            allFavorites.filter { recipe ->
                recipe.name.contains(query, ignoreCase = true) ||
                        recipe.description.contains(query, ignoreCase = true)
            }
        }
        _state.value = FavoritesState.Success(filteredList)
    }

}

sealed class FavoritesState {
    object Loading : FavoritesState()
    data class Success(val favorites: List<Recipe>) : FavoritesState()
    data class Error(val message: String) : FavoritesState()
}

