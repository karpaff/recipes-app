package com.example.recipesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ItemRecipeBinding
import com.example.recipesapp.network.RecipeResponse

class FavoritesAdapter(
    private val onItemClick: (String) -> Unit
) : ListAdapter<RecipeResponse, FavoritesAdapter.FavoritesViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecipeResponse>() {
            override fun areItemsTheSame(oldItem: RecipeResponse, newItem: RecipeResponse): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: RecipeResponse, newItem: RecipeResponse): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe, onItemClick)
    }

    class FavoritesViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecipeResponse, onItemClick: (String) -> Unit) {
            binding.recipeName.text = recipe.name
            binding.recipeDescription.text = recipe.description
            Glide.with(binding.recipeImage.context)
                .load(recipe.picture)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.recipeImage)

            binding.root.setOnClickListener {
                onItemClick(recipe._id)
            }
        }
    }
}
