package com.sandip.locus

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sandip.locus.data.Input
import com.sandip.locus.databinding.ActivityMainBinding
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private lateinit var adapter: InputListAdapter

    private val inputList: MutableList<Input> = ArrayList()

    private val executors: AppExecutors by lazy { AppExecutors() }

    private var picturePath: String? = null
    private var photoFile: File? = null

    private var inputItem: Input? = null
    private var selectedPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (inputList.isEmpty()) {
            getInputDataList()?.let {
                inputList.addAll(it)
            }
        }

        if(!::adapter.isInitialized) {
            InputListAdapter(
                appExecutors = executors,
                clickCallback = this::handleAdapterClickCallback
            ).also {
                adapter = it
            }
        }

        binding?.recyclerView?.adapter = adapter
        inputList.let {
            adapter.submitList(it)
        }
    }

    private fun handleAdapterClickCallback(action: String, item: Input, position: Int) {
        when(action) {
            IMAGE_CAPTURE -> {
                selectedPosition = position
                inputItem = item
                captureImage()
            }

            IMAGE_REMOVE -> {
                item.value = null
                adapter.notifyItemChanged(position)
            }

            LOAD_FULL_IMAGE -> {
                val intent = Intent(this, FullScreenImage::class.java)
                intent.putExtra("image_url", item.value)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }


    private fun getInputDataList(): MutableList<Input>? {
        try {
            val assetManager = assets
            val ims: InputStream = assetManager.open("input.json")
            val gson = Gson()
            val reader: Reader = InputStreamReader(ims)
            return gson.fromJson(reader, object : TypeToken<List<Input?>?>() {}.type)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }


    private fun captureImage() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION
            )
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile()
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        val photoURI = FileProvider.getUriForFile(
                            this,
                            "com.sandip.locus.fileprovider",
                            photoFile!!
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                } catch (ex: Exception) {
                    // Error occurred while creating the File
                    displayMessage(baseContext, ex.message.toString())
                }

            } else {
                displayMessage(baseContext, "Null")
            }
        }

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        picturePath = image.absolutePath
        return image
    }

    private fun displayMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                inputItem?.value = picturePath
                adapter.notifyItemChanged(selectedPosition)
            } else {
                displayMessage(baseContext, "Request cancelled or something went wrong.")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_PERMISSION) {
            if (permissions.isNotEmpty() && permissions[0] == Manifest.permission.CAMERA) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    captureImage()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.submit-> {
                logAllValues()
                Toast.makeText(this, "submit", Toast.LENGTH_LONG).show()
            }
        }

        return true
    }

    private fun logAllValues() {
        if(inputList!=null && inputList.isNotEmpty()) {
            for(i in 0 until inputList.size) {
                Log.e("logAllValues", "value:  "+inputList[i].toString())
            }
        }
    }
}