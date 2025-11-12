package com.example.appresenias

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.example.appresenias.databinding.ActivityReseniaBinding
import com.example.appresenias.ui.resenadetalle.ResenaDetalleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReseniaDetalleActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RESENA_ID = "extra_resena_id"
    }

    private lateinit var binding: ActivityReseniaBinding
    private val viewModel: ResenaDetalleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReseniaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val resenaId = intent.getIntExtra(EXTRA_RESENA_ID, -1)

        if (resenaId == -1) {
            Toast.makeText(this, "Error: ID de reseña no válido.", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        
        viewModel.cargarResena(resenaId)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resena.collect { resena ->
                    resena?.let {
                        binding.ivReseniaPhoto.load(it.photoUri) {
                            crossfade(true)
                            placeholder(R.drawable.ic_launcher_background)
                            error(R.drawable.ic_launcher_background)
                        }
                        binding.rbReseniaRating.rating = it.rating.toFloat()
                        binding.tvReseniaComment.text = it.comment
                    }
                }
            }
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
