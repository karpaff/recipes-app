package com.example.recipesapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.RecipesApp
import com.example.recipesapp.network.SessionManager
import com.example.recipesapp.ui.adapter.IngredientAdapter
import com.example.recipesapp.ui.adapter.StepAdapter
import com.example.recipesapp.data.model.RecipeDetails
import com.example.recipesapp.databinding.FragmentRecipeDetailsBinding
import com.example.recipesapp.viewmodel.RecipeDetailsState
import com.example.recipesapp.viewmodel.RecipeDetailsViewModel

class RecipeDetailsFragment : Fragment() {

    private var _binding: FragmentRecipeDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipeDetailsViewModel by viewModels {
        (requireActivity().application as RecipesApp).appViewModelFactory
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeId = arguments?.getString("recipeId") ?: return
        val token = SessionManager(requireContext()).fetchAuthToken()

        if (token.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please log in to view recipe details.", Toast.LENGTH_SHORT).show()
            return
        }

        setupObservers()
        viewModel.fetchRecipeDetails(recipeId)

        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.favoriteIcon.setOnClickListener {
            if (binding.favoriteIcon.tag == true) {
                viewModel.removeFromFavorites(token, recipeId)
            } else {
                viewModel.addToFavorites(token, recipeId)
            }
        }
    }

    private fun setupObservers() {
        viewModel.recipeDetailsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is RecipeDetailsState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is RecipeDetailsState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    updateUIWithRecipeDetails(state.recipe)
                }
                is RecipeDetailsState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

            viewModel.favoriteState.observe(viewLifecycleOwner) { isFavorite ->
            binding.favoriteIcon.tag = isFavorite
            binding.favoriteIcon.setImageResource(
                if (isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_border
            )
        }
    }

    private fun updateUIWithRecipeDetails(recipe: RecipeDetails) {
        binding.recipeDetailsName.text = recipe.name
        binding.recipeDetailsDescription.text = recipe.description
        binding.recipeDetailsTime.text = "${recipe.timeToPrepare} mins"
        binding.recipeDetailsDifficulty.text = recipe.difficulty

        val ingredientAdapter = IngredientAdapter(recipe.ingredients.map { "${it.quantity} ${it.name}" })
        binding.ingredientsList.adapter = ingredientAdapter
        binding.ingredientsList.layoutManager = LinearLayoutManager(requireContext())

        val stepAdapter = StepAdapter(recipe.instructions)
        binding.stepsList.adapter = stepAdapter
        binding.stepsList.layoutManager = LinearLayoutManager(requireContext())

        Glide.with(requireContext())
            .load(recipe.picture)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.recipeDetailsImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
