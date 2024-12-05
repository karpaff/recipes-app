package com.example.recipesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.recipesapp.R
import com.example.recipesapp.SessionManager
import com.example.recipesapp.network.LoginRequest
import com.example.recipesapp.network.RegisterRequest
import com.example.recipesapp.network.RetrofitInstance
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var sessionManager: SessionManager
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    private lateinit var logoutButton: Button
    private lateinit var avatarImage: ImageView
    private lateinit var userLogin: TextView
    private lateinit var userRole: TextView
    private lateinit var favoritesCount: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        sessionManager = SessionManager(requireContext())

        loginButton = view.findViewById(R.id.login_button)
        signupButton = view.findViewById(R.id.signup_button)
        logoutButton = view.findViewById(R.id.logout_button)
        avatarImage = view.findViewById(R.id.avatar_image)
        userLogin = view.findViewById(R.id.user_login)
        userRole = view.findViewById(R.id.user_role)
        favoritesCount = view.findViewById(R.id.favorites_count)

        setupListeners()
        updateUI()

        return view
    }

    private fun setupListeners() {
        loginButton.setOnClickListener {
            showLoginDialog()
        }

        signupButton.setOnClickListener {
            showRegisterDialog()
        }

        logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun showLoginDialog() {
        val loginDialog = LoginDialogFragment { username, password ->
            login(username, password)
        }
        loginDialog.show(parentFragmentManager, "LoginDialogFragment")
    }

    private fun showRegisterDialog() {
        val registerDialog = RegisterDialogFragment { username, password ->
            register(username, password)
        }
        registerDialog.show(parentFragmentManager, "RegisterDialogFragment")
    }

    private fun register(username: String, password: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.registerUser(RegisterRequest(username, password))
                if (response.isSuccessful) {
                    // После успешной регистрации автоматически логиним пользователя
                    login(username, password)
                } else {
                    // Показываем ошибку (можно улучшить с обработкой сообщений)
                    showError("Registration failed: ${response.message()}")
                }
            } catch (e: Exception) {
                showError("Error: ${e.message}")
            }
        }
    }

    private fun login(username: String, password: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.login(LoginRequest(username, password))
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    sessionManager.saveAuthToken(loginResponse.token)
                    sessionManager.saveLogin(loginResponse.user.login)
                    updateUI()
                } else {
                    showError("Login failed: ${response.message()}")
                }
            } catch (e: Exception) {
                showError("Error: ${e.message}")
            }
        }
    }

    private fun logout() {
        sessionManager.clearAuthToken()
        updateUI()
    }

    private fun updateUI() {
        val token = sessionManager.fetchAuthToken()
        if (token.isNullOrEmpty()) {
            // Не залогинен
            loginButton.visibility = View.VISIBLE
            signupButton.visibility = View.VISIBLE
            logoutButton.visibility = View.GONE
            userLogin.visibility = View.GONE
            userRole.text = "Role: guest"
            favoritesCount.visibility = View.GONE
        } else {
            // Залогинен
            loginButton.visibility = View.GONE
            signupButton.visibility = View.GONE
            logoutButton.visibility = View.VISIBLE
            userLogin.visibility = View.VISIBLE
            userLogin.text = sessionManager.fetchLogin() ?: "Unknown"
            userRole.text = "Role: user"
            fetchFavoritesCount()
        }
    }

    private fun fetchFavoritesCount() {
        lifecycleScope.launch {
            try {
                val token = sessionManager.fetchAuthToken()
                val response = RetrofitInstance.api.getFavorites("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    val favorites = response.body()!!
                    favoritesCount.visibility = View.VISIBLE
                    favoritesCount.text = "Favorites: ${favorites.size}"
                }
            } catch (e: Exception) {
                favoritesCount.visibility = View.GONE
            }
        }
    }

    private fun showError(message: String) {
        // Метод для показа ошибок
        // Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
