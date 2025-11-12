package com.example.appresenias

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appresenias.databinding.ActivityReseniasBinding
import com.example.appresenias.ui.resena.ResenaAdapter
import com.example.appresenias.ui.resena.ResenaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReseniasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReseniasBinding
    private val viewModel: ResenaViewModel by viewModels()
    private lateinit var resenaAdapter: ResenaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReseniasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        resenaAdapter = ResenaAdapter { resena ->
            Toast.makeText(this, "Clic en reseña: ${resena.comment}", Toast.LENGTH_SHORT).show()
        }

        binding.rvResenas.apply {
            adapter = resenaAdapter
            layoutManager = LinearLayoutManager(this@ReseniasActivity)
        }
    }

    private fun setupListeners() {
        binding.fabAgregarResena.setOnClickListener {
            val intent = Intent(this, SubirReseniaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resenas.collect { resenas ->
                    resenaAdapter.submitList(resenas)
                }
            }
        }
    }
}
