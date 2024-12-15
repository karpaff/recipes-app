package com.example.recipesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.network.SessionManager
import com.example.recipesapp.network.LoginRequest
import com.example.recipesapp.network.RegisterRequest
import com.example.recipesapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    sealed class UserState {
        object Guest : UserState()
        data class LoggedIn(val username: String, val favoritesCount: Int) : UserState()
    }

    private val _userState = MutableLiveData<UserState>()
    val userState: LiveData<UserState> = _userState

    init {
        updateUserState()
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.login(LoginRequest(username, password))
                sessionManager.saveAuthToken(response.token)
                sessionManager.saveLogin(response.user.login)
                updateUserState()
            } catch (e: Exception) {
                _userState.value = UserState.Guest
            }
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            try {
                userRepository.register(RegisterRequest(username, password))
                login(username, password)
            } catch (e: Exception) {
                _userState.value = UserState.Guest
            }
        }
    }

    fun logout() {
        sessionManager.clearAuthToken()
        updateUserState()
    }



    private fun updateUserState() {
        val token = sessionManager.fetchAuthToken()
        val username = sessionManager.fetchLogin()
        if (token.isNullOrEmpty() || username.isNullOrEmpty()) {
            _userState.value = UserState.Guest
        } else {
            viewModelScope.launch {
                try {
                    val favorites = userRepository.getFavorites(token)
                    _userState.value = UserState.LoggedIn(username, favorites.size)
                } catch (e: Exception) {
                    _userState.value = UserState.Guest
                }
            }
        }
    }
}
