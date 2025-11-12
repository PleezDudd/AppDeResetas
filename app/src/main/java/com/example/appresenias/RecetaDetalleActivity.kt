/*
 * Integrantes: Mirko Pino, Gabriel Nercelles, Cristobal Paredes
 */
package com.example.appresenias

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.example.appresenias.data.repository.RecipeDetail
import com.example.appresenias.databinding.ActivityRecetasBinding
import com.example.appresenias.ui.recetadetalle.RecetaDetalleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecetaDetalleActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RECIPE_ID = "extra_recipe_id"
    }

    private lateinit var binding: ActivityRecetasBinding
    private val viewModel: RecetaDetalleViewModel by viewModels()
    private var currentRecipeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecetasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        currentRecipeId = intent.getStringExtra(EXTRA_RECIPE_ID)

        setupListeners()
        observeViewModel()

        viewModel.obtenerReceta(currentRecipeId ?: "")
    }

    private fun setupListeners() {
        binding.fabCreateReview.setOnClickListener {
            val intent = Intent(this, SubirReseniaActivity::class.java).apply {
                putExtra(SubirReseniaActivity.EXTRA_RECIPE_ID, currentRecipeId)
            }
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.isLoading
                    
                    state.receta?.let { receta ->
                        updateUi(receta)
                    }
                    
                    state.error?.let { error ->
                        Toast.makeText(this@RecetaDetalleActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun updateUi(receta: RecipeDetail) {
        binding.collapsingToolbarLayout.title = receta.strMeal
        binding.ivRecipeImage.load(receta.strMealThumb) {
            crossfade(true)
        }
        binding.tvRecipeCategory.text = receta.strCategory
        binding.tvRecipeArea.text = receta.strArea
        binding.tvInstructions.text = receta.strInstructions
        
        val ingredientsText = receta.getIngredientsWithMeasures()
            .joinToString("\n") { (ingredient, measure) ->
                "- $measure $ingredient".trim()
            }
        binding.tvIngredientsList.text = ingredientsText
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
