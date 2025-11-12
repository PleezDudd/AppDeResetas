/*
 * Integrantes: Mirko Pino, Gabriel Nercelles, Cristobal Paredes
 */
package com.example.appresenias.ui.resenadetalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appresenias.data.local.Resena
import com.example.appresenias.data.repository.ResenaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResenaDetalleViewModel @Inject constructor(
    private val resenaRepository: ResenaRepository
) : ViewModel() {

    private val _resena = MutableStateFlow<Resena?>(null)
    val resena: StateFlow<Resena?> = _resena.asStateFlow()

    fun cargarResena(id: Int) {
        viewModelScope.launch {
            resenaRepository.obtenerResenaPorId(id)
                .catch { e ->
                    e.printStackTrace()
                }
                .collect { resena ->
                    _resena.value = resena
                }
        }
    }
}
