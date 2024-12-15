package com.example.recipesapp

import android.app.Application
import com.example.recipesapp.network.RetrofitInstance
import com.example.recipesapp.data.repository.RecipesRepository
import com.example.recipesapp.data.repository.UserRepository
import com.example.recipesapp.network.SessionManager
import com.example.recipesapp.viewmodel.AppViewModelFactory

class RecipesApp : Application() {

    lateinit var instance: RecipesApp
        private set

    lateinit var appViewModelFactory: AppViewModelFactory
        private set

    lateinit var sessionManager: SessionManager
        private set


    override fun onCreate() {
        super.onCreate()

        instance = this

        val recipesRepository = RecipesRepository(RetrofitInstance.api)
        val userRepository = UserRepository(RetrofitInstance.api)

        sessionManager = SessionManager(this)
        appViewModelFactory = AppViewModelFactory(recipesRepository, userRepository, sessionManager)
    }
}
