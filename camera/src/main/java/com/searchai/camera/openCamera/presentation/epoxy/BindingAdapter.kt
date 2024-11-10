package com.searchai.camera.openCamera.presentation.epoxy


import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


/**Setting up the ImageView.*/
@BindingAdapter("setImageView")
fun setImageView(view: ImageView?, url: String?) {
    if (view != null) {
        Glide.with(view.context).load(url).into(view)
    }
}