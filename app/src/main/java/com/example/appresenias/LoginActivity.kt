/*
 * Integrantes: Mirko Pino, Gabriel Nercelles, Cristobal Paredes
 */
package com.example.appresenias

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appresenias.databinding.LogeoBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LogeoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LogeoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnIngresar.setOnClickListener {
            val username = binding.etUsuario.text.toString()
            val password = binding.etPassword.text.toString()

            if (username == "admin" && password == "1234") {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
