package com.example.appresenias

import android.os.Bundle
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class resenia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resenia)

        val textViewTitulo = findViewById<TextView>(R.id.txt_nombrePlato)
        val ratingBarResenia = findViewById<RatingBar>(R.id.ratingBarResenia)
        val textViewComentario = findViewById<TextView>(R.id.txt_resenia1)

        // 🔹 Simulación de datos (luego puedes reemplazarlo por datos de la API)
        val nombrePlato = "Ceviche Mixto"
        val calificacion = 4.5f
        val comentario = "Excelente sabor y presentación. El pescado muy fresco y el ají en su punto."

        // 🔹 Mostrar los datos en la interfaz
        textViewTitulo.text = nombrePlato
        ratingBarResenia.rating = calificacion
        textViewComentario.text = comentario
    }
}
