package com.example.recipesapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.recipesapp.R
import com.example.recipesapp.RecipesApp
import com.example.recipesapp.ui.dialog.LoginDialogFragment
import com.example.recipesapp.ui.dialog.RegisterDialogFragment
import com.example.recipesapp.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    private lateinit var logoutButton: Button
    private lateinit var avatarImage: ImageView
    private lateinit var userRole: TextView
    private lateinit var favoritesCount: TextView
    private lateinit var toolbarTitle: TextView

    private val viewModel: ProfileViewModel by viewModels {
        (requireActivity().application as RecipesApp).appViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        loginButton = view.findViewById(R.id.login_button)
        signupButton = view.findViewById(R.id.signup_button)
        logoutButton = view.findViewById(R.id.logout_button)
        avatarImage = view.findViewById(R.id.avatar_image)
        userRole = view.findViewById(R.id.user_role)
        favoritesCount = view.findViewById(R.id.favorites_count)
        toolbarTitle = view.findViewById(R.id.toolbar_title)

        setupListeners()
        observeViewModel()

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
            viewModel.logout()
        }
    }

    private fun showLoginDialog() {
        val loginDialog = LoginDialogFragment { username, password ->
            viewModel.login(username, password)
        }
        loginDialog.show(parentFragmentManager, "LoginDialogFragment")
    }

    private fun showRegisterDialog() {
        val registerDialog = RegisterDialogFragment { username, password ->
            viewModel.register(username, password)
        }
        registerDialog.show(parentFragmentManager, "RegisterDialogFragment")
    }

    private fun observeViewModel() {
        viewModel.userState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileViewModel.UserState.Guest -> {
                    showGuestState()
                }
                is ProfileViewModel.UserState.LoggedIn -> {
                    showLoggedInState(state.username, state.favoritesCount)
                }
            }
        }
    }

    private fun showGuestState() {
        loginButton.visibility = View.VISIBLE
        signupButton.visibility = View.VISIBLE
        logoutButton.visibility = View.GONE

        userRole.text = "Welcome, guest!"
        favoritesCount.visibility = View.GONE
    }

    private fun showLoggedInState(username: String, favoritesCountValue: Int) {
        loginButton.visibility = View.GONE
        signupButton.visibility = View.GONE
        logoutButton.visibility = View.VISIBLE

        userRole.text = "Welcome, $username!"
        favoritesCount.visibility = View.VISIBLE
        favoritesCount.text = "You have $favoritesCountValue favorite recipes. Let's use them to cook delicious dinner!"
    }
}
