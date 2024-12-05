package com.example.recipesapp.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.viewmodel.RecipesViewModel
import com.example.recipesapp.adapter.RecipesAdapter
import com.example.recipesapp.data.Recipe
import com.example.recipesapp.databinding.FragmentRecipesBinding

class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecipesAdapter
    private var allRecipes: List<Recipe> = emptyList() // Хранит все рецепты для фильтрации

    private val viewModel: RecipesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupSearch()

        // Загружаем данные
        viewModel.fetchRecipes()
    }

    private fun setupRecyclerView() {
        adapter = RecipesAdapter { recipe ->
            navigateToRecipeDetails(recipe._id)
        }

        binding.recipesList.layoutManager = LinearLayoutManager(requireContext())
        binding.recipesList.adapter = adapter
    }

    private fun setupObservers() {
        // Наблюдаем за списком рецептов
        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            allRecipes = recipes
            adapter.setRecipes(recipes)
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

    private fun setupSearch() {
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterRecipes(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterRecipes(query: String) {
        val filtered = if (query.isEmpty()) {
            allRecipes
        } else {
            allRecipes.filter { recipe ->
                recipe.name.contains(query, ignoreCase = true) ||
                        recipe.description.contains(query, ignoreCase = true)
            }
        }
        adapter.setRecipes(filtered)
    }

    private fun navigateToRecipeDetails(recipeId: String) {
        val action =
            RecipesFragmentDirections.actionRecipesFragmentToRecipeDetailsFragment(recipeId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
