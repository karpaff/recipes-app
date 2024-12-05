package com.example.recipesapp.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.SessionManager
import com.example.recipesapp.adapter.FavoritesAdapter
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.network.RecipeResponse
import com.example.recipesapp.network.RetrofitInstance
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoritesAdapter: FavoritesAdapter
    private var allFavorites: List<RecipeResponse> = emptyList() // Для фильтрации
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        setupRecyclerView()
        setupSearch()
        fetchFavorites()
    }

    private fun setupRecyclerView() {
        favoritesAdapter = FavoritesAdapter { recipeId ->
            openRecipeDetails(recipeId)
        }
        binding.favoritesList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoritesAdapter
        }
    }

    private fun setupSearch() {
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterFavorites(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterFavorites(query: String) {
        val filtered = if (query.isEmpty()) {
            allFavorites
        } else {
            allFavorites.filter { favorite ->
                favorite.name.contains(query, ignoreCase = true) ||
                        favorite.description.contains(query, ignoreCase = true)
            }
        }
        favoritesAdapter.submitList(filtered)
    }

    private fun fetchFavorites() {
        val token = sessionManager.fetchAuthToken()
        if (token.isNullOrEmpty()) {
            binding.emptyFavoritesText.visibility = View.VISIBLE
            binding.favoritesList.visibility = View.GONE
            return
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getFavorites("Bearer $token")
                if (response.isSuccessful) {
                    val favorites = response.body()
                    if (favorites.isNullOrEmpty()) {
                        binding.emptyFavoritesText.visibility = View.VISIBLE
                        binding.favoritesList.visibility = View.GONE
                    } else {
                        allFavorites = favorites
                        favoritesAdapter.submitList(favorites)
                        binding.emptyFavoritesText.visibility = View.GONE
                        binding.favoritesList.visibility = View.VISIBLE
                    }
                } else {
                    binding.emptyFavoritesText.visibility = View.VISIBLE
                    binding.favoritesList.visibility = View.GONE
                }
            } catch (e: Exception) {
                binding.emptyFavoritesText.visibility = View.VISIBLE
                binding.favoritesList.visibility = View.GONE
            }
        }
    }

    private fun openRecipeDetails(recipeId: String) {
        val action =
            FavoritesFragmentDirections.actionFavoritesFragmentToRecipeDetailsFragment(recipeId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
