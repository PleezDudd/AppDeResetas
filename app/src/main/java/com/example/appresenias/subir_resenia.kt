package com.example.appresenias

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.load
import com.example.appresenias.databinding.ActivitySubirReseniaBinding
import com.example.appresenias.ui.resena.ResenaViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class SubirReseniaActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RECIPE_ID = "extra_recipe_id"
    }

    private lateinit var binding: ActivitySubirReseniaBinding
    private val viewModel: ResenaViewModel by viewModels()
    private var currentRecipeId: String? = null
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubirReseniaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Crear Nueva Reseña"
        currentRecipeId = intent.getStringExtra(EXTRA_RECIPE_ID)
        setupListeners()
    }

    private fun setupListeners() {
        binding.cardAddPhoto.setOnClickListener { showPhotoOptionsDialog() }
        binding.btSubirResenia.setOnClickListener { saveReview() }
    }

    private fun saveReview() {
        val comment = binding.etComment.text.toString().trim()
        val rating = binding.ratingBarPlato.rating.toInt()
        val photoUriString = photoUri?.toString() ?: ""

        if (comment.isNotEmpty() && rating > 0 && !currentRecipeId.isNullOrBlank()) {
            viewModel.insertarNuevaResena(comment, rating, currentRecipeId!!, photoUriString)
            Toast.makeText(this, "Reseña guardada", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPhotoOptionsDialog() {
        val options = arrayOf("Tomar foto", "Elegir de la galería")
        AlertDialog.Builder(this)
            .setTitle("Añadir foto")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> checkCameraPermission()
                    1 -> openGallery()
                }
                dialog.dismiss()
            }.show()
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            photoUri = uri
            binding.ivReviewPhoto.load(photoUri)
        }
    }
    
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Verificamos si hay una app de cámara que pueda manejar el intent
        if (intent.resolveActivity(packageManager) == null) {
            Toast.makeText(this, "No se encontró una aplicación de cámara.", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val photoFile = createImageFile()
            photoUri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.provider", photoFile)
            
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            
            cameraLauncher.launch(intent)
        } catch (e: Exception) { // Capturamos cualquier excepción
            e.printStackTrace()
            Toast.makeText(this, "Error al intentar abrir la cámara: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            binding.ivReviewPhoto.load(photoUri)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "JPEG_${timeStamp}_"
        val storageDir: File? = getExternalFilesDir(null)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }
}
