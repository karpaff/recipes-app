package com.example.recipesapp

import android.app.Application

class RecipesApp : Application() {
    companion object {
        lateinit var instance: RecipesApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
