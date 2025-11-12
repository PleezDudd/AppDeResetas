package com.example.appresenias.ui.resena

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appresenias.ReseniaDetalleActivity
import com.example.appresenias.databinding.ActivityReseniasBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReseniasFragment : Fragment() {

    private var _binding: ActivityReseniasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ResenaViewModel by viewModels()
    private lateinit var resenaAdapter: ResenaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityReseniasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        resenaAdapter = ResenaAdapter { resena ->
            val intent = Intent(requireContext(), ReseniaDetalleActivity::class.java).apply {
                putExtra(ReseniaDetalleActivity.EXTRA_RESENA_ID, resena.id)
            }
            startActivity(intent)
        }
        binding.rvResenas.apply {
            adapter = resenaAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupListeners() {
        binding.fabAgregarResena.setOnClickListener {
            Toast.makeText(requireContext(), "Crea una reseña desde el detalle de una receta", Toast.LENGTH_LONG).show()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resenas.collect { resenas ->
                    resenaAdapter.submitList(resenas)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
