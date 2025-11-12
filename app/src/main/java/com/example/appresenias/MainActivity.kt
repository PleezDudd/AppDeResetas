package com.example.appresenias

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Redirigir inmediatamente a ListaPlatosActivity
        val intent = Intent(this, ListaPlatosActivity::class.java)
        startActivity(intent)
        finish()
    }
}
