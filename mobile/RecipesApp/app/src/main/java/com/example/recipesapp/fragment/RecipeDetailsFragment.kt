package com.example.recipesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.viewmodel.RecipeDetailsViewModel
import com.example.recipesapp.SessionManager
import com.example.recipesapp.adapter.IngredientAdapter
import com.example.recipesapp.adapter.StepAdapter
import com.example.recipesapp.data.RecipeDetails
import com.example.recipesapp.databinding.FragmentRecipeDetailsBinding
import com.example.recipesapp.network.AddToFavoritesRequest
import com.example.recipesapp.network.RetrofitInstance
import kotlinx.coroutines.launch

class RecipeDetailsFragment : Fragment() {

    private var _binding: FragmentRecipeDetailsBinding? = null
    private val binding get() = _binding!!
    private var isFavorite = false
    private val viewModel: RecipeDetailsViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        sessionManager = SessionManager(requireContext()) // Инициализируем SessionManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем recipeId из аргументов
        val recipeId = arguments?.getString("recipeId") ?: return

        // Настраиваем наблюдателей
        setupObservers()

        // Запрашиваем детали рецепта
        viewModel.fetchRecipeDetails(recipeId)

        // Обработка кнопки назад
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Обработка клика на звездочку
        binding.favoriteIcon.setOnClickListener {
            val recipeId = arguments?.getString("recipeId") ?: return@setOnClickListener
            if (isFavorite) {
                removeFromFavorites(recipeId)
            } else {
                addToFavorites(recipeId)
            }
        }
    }

    private fun addToFavorites(recipeId: String) {
        val token = sessionManager.fetchAuthToken()
        if (token.isNullOrEmpty()) {
            showToast("Please log in to add favorites.")
            return
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.addToFavorites(
                    "Bearer $token",
                    AddToFavoritesRequest(recipeId)
                )
                if (response.isSuccessful) {
                    isFavorite = true
                    updateFavoriteIcon()
                    Toast.makeText(requireContext(), "Added to favorites!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to add to favorites", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun removeFromFavorites(recipeId: String) {
        val token = sessionManager.fetchAuthToken()
        if (token.isNullOrEmpty()) {
            showToast("Please log in to remove favorites.")
            return
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.removeFromFavorites(
                    "Bearer $token",
                    recipeId
                )
                if (response.isSuccessful) {
                    isFavorite = false
                    updateFavoriteIcon()
                    Toast.makeText(requireContext(), "Removed from favorites!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to remove from favorites", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateFavoriteIcon() {
        val icon = if (isFavorite) {
            R.drawable.ic_star_filled // Заполненная звездочка
        } else {
            R.drawable.ic_star_border // Пустая звездочка
        }
        binding.favoriteIcon.setImageResource(icon)
    }

    private fun setupObservers() {
        // Наблюдаем за деталями рецепта
        viewModel.recipeDetails.observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                updateUIWithRecipeDetails(it)
            }
        }

        // Наблюдаем за состоянием загрузки
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Наблюдаем за ошибками
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUIWithRecipeDetails(recipe: RecipeDetails) {
        binding.recipeDetailsName.text = recipe.name
        binding.recipeDetailsDescription.text = recipe.description
        binding.recipeDetailsTime.text = "${recipe.timeToPrepare} mins"
        binding.recipeDetailsDifficulty.text = recipe.difficulty

        // Устанавливаем ингредиенты
        val ingredientAdapter = IngredientAdapter(recipe.ingredients.map { "${it.quantity} ${it.name}" })
        binding.ingredientsList.adapter = ingredientAdapter
        binding.ingredientsList.layoutManager = LinearLayoutManager(requireContext())

        // Устанавливаем шаги
        val stepAdapter = StepAdapter(recipe.instructions)
        binding.stepsList.adapter = stepAdapter
        binding.stepsList.layoutManager = LinearLayoutManager(requireContext())

        Glide.with(requireContext())
            .load(recipe.picture)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.recipeDetailsImage)
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
