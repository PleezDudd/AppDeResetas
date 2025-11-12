package com.example.appresenias

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appresenias.databinding.ActivityListaPlatosBinding
import com.example.appresenias.ui.recetas.ListaPlatosUiState
import com.example.appresenias.ui.recetas.ListaPlatosViewModel
import com.example.appresenias.ui.recetas.RecipeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class lista_platos : AppCompatActivity() {

    private lateinit var binding: ActivityListaPlatosBinding
    private val viewModel: ListaPlatosViewModel by viewModels()
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListaPlatosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        recipeAdapter = RecipeAdapter { recipeId ->
            Toast.makeText(this, "Receta seleccionada: $recipeId", Toast.LENGTH_SHORT).show()
        }

        binding.rvPlatos.apply {
            adapter = recipeAdapter
            layoutManager = LinearLayoutManager(this@lista_platos)
        }
    }

    private fun setupListeners() {
        binding.btSearch.setOnClickListener {
            buscarRecetas()
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                buscarRecetas()
                true
            } else {
                false
            }
        }

        binding.btVolver2.setOnClickListener {
            finish()
        }
    }

    private fun buscarRecetas() {
        val nombre = binding.etSearch.text.toString().trim()
        if (nombre.isNotEmpty()) {
            viewModel.buscarRecetas(nombre)
        } else {
            Toast.makeText(this, "Por favor ingresa un nombre de receta", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ListaPlatosUiState.Idle -> {
                            binding.progressBar.visibility = android.view.View.GONE
                        }
                        is ListaPlatosUiState.Loading -> {
                            binding.progressBar.visibility = android.view.View.VISIBLE
                            binding.rvPlatos.visibility = android.view.View.GONE
                        }
                        is ListaPlatosUiState.Success -> {
                            binding.progressBar.visibility = android.view.View.GONE
                            binding.rvPlatos.visibility = android.view.View.VISIBLE
                            recipeAdapter.submitList(state.recipes)
                        }
                        is ListaPlatosUiState.Error -> {
                            binding.progressBar.visibility = android.view.View.GONE
                            binding.rvPlatos.visibility = android.view.View.VISIBLE
                            Toast.makeText(this@lista_platos, state.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}