package com.example.appresenias.ui.recetas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appresenias.domain.model.Recipe
import com.example.appresenias.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ListaPlatosUiState {
    object Idle : ListaPlatosUiState()
    object Loading : ListaPlatosUiState()
    data class Success(val recipes: List<Recipe>) : ListaPlatosUiState()
    data class Error(val message: String) : ListaPlatosUiState()
}

@HiltViewModel
class ListaPlatosViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListaPlatosUiState>(ListaPlatosUiState.Idle)
    val uiState: StateFlow<ListaPlatosUiState> = _uiState.asStateFlow()

    fun buscarRecetas(nombre: String) {
        if (nombre.isBlank()) {
            _uiState.value = ListaPlatosUiState.Error("Por favor ingresa un nombre de receta")
            return
        }

        _uiState.value = ListaPlatosUiState.Loading

        viewModelScope.launch {
            val result = repository.buscarRecetas(nombre)

            result.fold(
                onSuccess = { recipes ->
                    if (recipes.isEmpty()) {
                        _uiState.value = ListaPlatosUiState.Error("No se encontraron recetas")
                    } else {
                        _uiState.value = ListaPlatosUiState.Success(recipes)
                    }
                },
                onFailure = { exception ->
                    _uiState.value = ListaPlatosUiState.Error(
                        "Error al buscar recetas: ${exception.message ?: "Error desconocido"}"
                    )
                }
            )
        }
    }

    fun limpiarEstado() {
        _uiState.value = ListaPlatosUiState.Idle
    }
}

