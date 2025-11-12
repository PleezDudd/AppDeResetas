package com.example.appresenias.ui.listaplatos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appresenias.data.repository.RecipeRepository
import com.example.appresenias.data.repository.RecipeSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ListaPlatosUiState(
    val isLoading: Boolean = false,
    val recetas: List<RecipeSummary> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class ListaPlatosViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListaPlatosUiState())
    val uiState: StateFlow<ListaPlatosUiState> = _uiState.asStateFlow()

    init {
        buscarRecetas("Arrabiata")
    }

    fun buscarRecetas(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = recipeRepository.buscarRecetas(query)
                _uiState.update {
                    it.copy(isLoading = false, recetas = result.meals ?: emptyList(), error = null)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Error al buscar recetas: ${e.message}")
                }
            }
        }
    }
}
