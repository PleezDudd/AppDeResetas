package com.example.appresenias

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Redirigir directamente a lista_platos para testear la búsqueda de recetas
        val intent = Intent(this, lista_platos::class.java)
        startActivity(intent)
        finish()
    }
}
