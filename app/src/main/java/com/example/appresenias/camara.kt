package com.example.appresenias

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class camara : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private var photoUri: Uri? = null
    private var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camara)

        imageView = findViewById(R.id.imageViewFoto)
        val btnTomarFoto: Button = findViewById(R.id.btnTomarFoto)

        btnTomarFoto.setOnClickListener {
            checkPermissionAndOpenCamera()
        }
    }

    // 🔹 Verifica permiso antes de abrir la cámara
    private fun checkPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Toast.makeText(this, "Se necesita permiso para usar la cámara", Toast.LENGTH_SHORT)
                    .show()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // 🔹 Lanzador para pedir permiso
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) openCamera()
            else Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
        }

    // 🔹 Abre la cámara con FileProvider
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            try {
                photoFile = createImageFile()
                photoFile?.let {
                    photoUri = FileProvider.getUriForFile(
                        this,
                        "${applicationContext.packageName}.provider",
                        it
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    takePictureLauncher.launch(intent)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Error al crear el archivo de imagen", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // 🔹 Recibe el resultado de la cámara
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                imageView.setImageURI(photoUri)
                Toast.makeText(this, "Foto guardada correctamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Captura cancelada", Toast.LENGTH_SHORT).show()
            }
        }

    // 🔹 Crea archivo temporal donde se guardará la foto
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "JPEG_${timeStamp}_"
        val storageDir: File? = getExternalFilesDir(null)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }
}
