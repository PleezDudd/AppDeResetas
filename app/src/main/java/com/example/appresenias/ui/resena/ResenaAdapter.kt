package com.example.appresenias.ui.resena

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appresenias.data.local.Resena
import com.example.appresenias.databinding.ItemResenaBinding

class ResenaAdapter(
    private val onItemClicked: (Resena) -> Unit
) : ListAdapter<Resena, ResenaAdapter.ResenaViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResenaViewHolder {
        val binding = ItemResenaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResenaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResenaViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class ResenaViewHolder(private var binding: ItemResenaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(resena: Resena) {
            binding.tvItemComment.text = resena.comment
            binding.tvItemRating.text = "Calificación: ${resena.rating} estrellas"
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Resena>() {
            override fun areItemsTheSame(oldItem: Resena, newItem: Resena): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Resena, newItem: Resena): Boolean {
                return oldItem == newItem
            }
        }
    }
}