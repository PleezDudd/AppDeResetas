package com.example.appresenias.ui.resena

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appresenias.data.local.Resena
import com.example.appresenias.data.repository.ResenaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResenaViewModel @Inject constructor(
    private val repository: ResenaRepository
) : ViewModel() {

    val resenas: StateFlow<List<Resena>> = repository.obtenerTodas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun insertarNuevaResena(comment: String, rating: Int, recipeId: String, photoUri: String) {
        viewModelScope.launch {
            val nuevaResena = Resena(
                recipeId = recipeId,
                photoUri = photoUri,
                rating = rating,
                comment = comment
            )
            repository.insertar(nuevaResena)
        }
    }
}
