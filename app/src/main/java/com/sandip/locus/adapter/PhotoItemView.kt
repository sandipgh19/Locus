package com.sandip.locus.adapter

import android.view.View
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sandip.locus.IMAGE_CAPTURE
import com.sandip.locus.IMAGE_REMOVE
import com.sandip.locus.LOAD_FULL_IMAGE
import com.sandip.locus.R
import com.sandip.locus.data.Input
import com.sandip.locus.databinding.LayoutPhotoTypeBinding

class PhotoItemView(private val photoItemView: LayoutPhotoTypeBinding,
                    private val clickCallback: ((String, Input, Int) -> Unit)?
): InputItemView<ViewBinding>(photoItemView) {


    fun bind(item: Input, hashMap: HashMap<String, String>, position: Int) = with(photoItemView) {
        photoItemView.title.text = item.title
        item.value = item.value ?: hashMap[item.id]
        if(item.value==null) {
            photoItemView.close.visibility = View.GONE
            photoItemView.image.setImageDrawable(photoItemView.root.context.getDrawable(R.drawable.ic_baseline_camera_alt_24))
        }else {
            photoItemView.close.visibility = View.VISIBLE
            Glide.with(photoItemView.root.context).load(item.value).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.5f).into(photoItemView.image);
        }


        photoItemView.image.setOnClickListener {
            if(item.value==null) clickCallback?.invoke(IMAGE_CAPTURE, item, position)
            else clickCallback?.invoke(LOAD_FULL_IMAGE, item, position)

        }

        photoItemView.close.setOnClickListener {
            item.value = null
            hashMap.remove(item.id)
            clickCallback?.invoke(IMAGE_REMOVE, item, position)
        }
    }

}