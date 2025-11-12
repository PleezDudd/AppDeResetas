/*
 * Integrantes: Mirko Pino, Gabriel Nercelles, Cristobal Paredes
 */
package com.example.appresenias.ui.recetadetalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appresenias.data.local.Resena
import com.example.appresenias.data.repository.RecipeDetail
import com.example.appresenias.data.repository.RecipeRepository
import com.example.appresenias.data.repository.ResenaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecetaDetalleUiState(
    val isLoading: Boolean = true,
    val receta: RecipeDetail? = null,
    val error: String? = null
)

@HiltViewModel
class RecetaDetalleViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecetaDetalleUiState())
    val uiState: StateFlow<RecetaDetalleUiState> = _uiState.asStateFlow()

    fun obtenerReceta(id: String) {
        if (id.isBlank()) {
            _uiState.update { it.copy(isLoading = false, error = "ID de receta no válido") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = recipeRepository.obtenerRecetaPorId(id)
                val recetaDetalle = result.meals?.firstOrNull()

                if (recetaDetalle != null) {
                    _uiState.update {
                        it.copy(isLoading = false, receta = recetaDetalle, error = null)
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, error = "No se encontró la receta.")
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Error al cargar la receta: ${e.message}")
                }
            }
        }
    }
}
