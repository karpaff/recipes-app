package com.example.recipesapp.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.data.Recipe
import com.example.recipesapp.databinding.ItemRecipeBinding

class RecipesAdapter(
    private val onRecipeClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    private val recipes = mutableListOf<Recipe>()

    class RecipeViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        val recipeImage: ImageView = itemView.findViewById(R.id.recipeImage)
        val recipeName: TextView = itemView.findViewById(R.id.recipe_name)
        val recipeDescription: TextView = itemView.findViewById(R.id.recipe_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]

        holder.binding.recipeName.text = recipe.name
        holder.binding.recipeDescription.text = recipe.description
        // Загрузка изображения с помощью Glide
        Glide.with(holder.itemView.context)
            .load(recipe.picture) // URL картинки
            .placeholder(R.drawable.ic_placeholder) // Заглушка на время загрузки
            .error(R.drawable.ic_placeholder) // Заглушка при ошибке
            .into(holder.recipeImage) // ID ImageView

        holder.itemView.setOnClickListener { onRecipeClick(recipe) }

    }

    override fun getItemCount(): Int = recipes.size

    fun setRecipes(newRecipes: List<Recipe>) {
        recipes.clear()
        recipes.addAll(newRecipes)
        notifyDataSetChanged()
    }
}
