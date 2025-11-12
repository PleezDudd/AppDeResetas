package com.example.appresenias.ui.listaplatos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.appresenias.R
import com.example.appresenias.data.repository.RecipeSummary
import com.example.appresenias.databinding.ItemRecetaBinding

class RecipeAdapter(
    private val onItemClicked: (RecipeSummary) -> Unit
) : ListAdapter<RecipeSummary, RecipeAdapter.RecipeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecetaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class RecipeViewHolder(private val binding: ItemRecetaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(receta: RecipeSummary) {
            binding.tvRecipeName.text = receta.strMeal
            binding.ivRecipeThumbnail.load(receta.strMealThumb) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_background)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<RecipeSummary>() {
            override fun areItemsTheSame(oldItem: RecipeSummary, newItem: RecipeSummary): Boolean {
                return oldItem.idMeal == newItem.idMeal
            }

            override fun areContentsTheSame(oldItem: RecipeSummary, newItem: RecipeSummary): Boolean {
                return oldItem == newItem
            }
        }
    }
}
