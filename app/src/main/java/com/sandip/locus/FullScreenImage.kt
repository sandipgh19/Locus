package com.sandip.locus

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


class FullScreenImage: AppCompatActivity() {

    var myImage: ImageView? = null
    var url: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)
        url = intent.getStringExtra("image_url")
        myImage = findViewById(R.id.image)
        Glide.with(this).load(url)
            .placeholder(R.drawable.ic_baseline_camera_alt_24)
            .error(R.drawable.ic_baseline_camera_alt_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(myImage!!)
    }
}