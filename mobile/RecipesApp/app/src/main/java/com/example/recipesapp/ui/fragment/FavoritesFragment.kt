package com.example.recipesapp.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.RecipesApp
import com.example.recipesapp.ui.adapter.FavoritesAdapter
import com.example.recipesapp.databinding.FragmentFavoritesBinding
import com.example.recipesapp.viewmodel.FavoritesState
import com.example.recipesapp.viewmodel.FavoritesViewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels {
        (requireActivity().application as RecipesApp).appViewModelFactory
    }

    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        setupObservers()

        val token = (requireActivity().application as RecipesApp).sessionManager.fetchAuthToken()
        if (!token.isNullOrEmpty()) {
            viewModel.fetchFavorites(token)
        } else {
            showEmptyState()
        }
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
                viewModel.filterFavorites(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoritesState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.favoritesList.visibility = View.GONE
                    binding.emptyFavoritesText.visibility = View.GONE
                }
                is FavoritesState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (state.favorites.isEmpty()) {
                        binding.emptyFavoritesText.visibility = View.VISIBLE
                        binding.favoritesList.visibility = View.GONE
                    } else {
                        binding.emptyFavoritesText.visibility = View.GONE
                        binding.favoritesList.visibility = View.VISIBLE
                        favoritesAdapter.submitList(state.favorites)
                    }
                }
                is FavoritesState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.emptyFavoritesText.visibility = View.VISIBLE
                    binding.favoritesList.visibility = View.GONE
                }
            }
        }
    }

    private fun showEmptyState() {
        binding.emptyFavoritesText.visibility = View.VISIBLE
        binding.favoritesList.visibility = View.GONE
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
