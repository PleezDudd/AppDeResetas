package com.example.appresenias

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.appresenias.data.local.Resena
import com.example.appresenias.databinding.ActivitySubirReseniaBinding
import com.example.appresenias.ui.resena.ResenaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubirReseniaActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RECIPE_ID = "extra_recipe_id"
    }

    private lateinit var binding: ActivitySubirReseniaBinding
    private val viewModel: ResenaViewModel by viewModels()
    private var currentRecipeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubirReseniaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Crear Nueva Reseña"
        
        currentRecipeId = intent.getStringExtra(EXTRA_RECIPE_ID)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btSubirResenia.setOnClickListener {
            val comment = binding.etComment.text.toString().trim()
            val rating = binding.ratingBarPlato.rating.toInt()

            if (comment.isNotEmpty() && rating > 0 && !currentRecipeId.isNullOrBlank()) {
                viewModel.insertarNuevaResena(comment, rating, currentRecipeId!!)
                Toast.makeText(this, "Reseña guardada", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
