package com.example.appresenias

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast

class subir_resenia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_subir_resenia)

        // Ajuste visual (bordes del sistema)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ✅ Este bloque debe ir FUERA del setOnApplyWindowInsetsListener
        val ratingBar = findViewById<RatingBar>(R.id.ratingBarPlato)
        val textViewCalificacion = findViewById<TextView>(R.id.textViewCalificacion)

        ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                textViewCalificacion.text = "Calificación: $rating estrellas"
                Toast.makeText(this, "Le diste $rating estrellas", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
