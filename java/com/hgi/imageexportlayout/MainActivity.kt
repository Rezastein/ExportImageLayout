package com.hgi.imageexportlayout

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hgi.imageexportlayout.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var rl : RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        rl = binding.myLayout


        binding.btnSave.setOnClickListener{
            val bitmap = getImageOfView(rl)
            if (bitmap != null) {
                saveToStorage(bitmap)
            }
        }




    }

    @SuppressLint("Recycle")
    private fun saveToStorage(bitmap: Bitmap) {
        val imageName = "Eezzuutt${System.currentTimeMillis()}.jpg"
        var fos : OutputStream? = null
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            this.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME,imageName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "images/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri.let {
                    resolver.openOutputStream(it!!)
                }
            }
        }else{
            val imageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imageDirectory, imageName)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this, "Succes !", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getImageOfView(view: RelativeLayout?): Bitmap? {
        var image : Bitmap? = null
        try {
            if (view != null) {
                image = Bitmap.createBitmap(view.measuredWidth,view.measuredHeight,Bitmap.Config.ARGB_8888)
                val canvas = Canvas(image)
                view.draw(canvas)
            }

        }catch (e: Exception){
            Toast.makeText(this, "Eror$e", Toast.LENGTH_SHORT).show()
        }
        return image

    }


}