package com.example.appresenias

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appresenias.databinding.ActivityListaPlatosBinding
import com.example.appresenias.ui.listaplatos.ListaPlatosViewModel
import com.example.appresenias.ui.listaplatos.RecipeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListaPlatosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaPlatosBinding
    private val viewModel: ListaPlatosViewModel by viewModels()
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaPlatosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearchView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter { receta ->
            val intent = Intent(this, RecetaDetalleActivity::class.java).apply {
                putExtra(RecetaDetalleActivity.EXTRA_RECIPE_ID, receta.idMeal)
            }
            startActivity(intent)
        }
        binding.rvPlatos.apply {
            adapter = recipeAdapter
            layoutManager = LinearLayoutManager(this@ListaPlatosActivity)
        }
    }
    
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    viewModel.buscarRecetas(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.isLoading
                    recipeAdapter.submitList(state.recetas)
                    state.error?.let {
                        Toast.makeText(this@ListaPlatosActivity, it, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
