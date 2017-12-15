package com.yogcn.core.view

import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatImageView
import android.text.TextUtils
import android.util.AttributeSet
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

/**
 * Created by lyndon on 2017/11/8.
 */
open class YogcnImageView : AppCompatImageView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    companion object {
        @BindingAdapter(*arrayOf("url", "error", "place"))
        @JvmStatic
        fun loadImage(imageView: YogcnImageView, url: String?, error: Drawable, place: Drawable) {
            if (TextUtils.isEmpty(url)) return
            var context = imageView.context
            Picasso.with(context).load(url).placeholder(place).error(error).fit().into(imageView)

        }

        @BindingAdapter(*arrayOf("url"))
        @JvmStatic
        fun loadImage(imageView: YogcnImageView, url: String?) {
            if (TextUtils.isEmpty(url)) return
            var context = imageView.context
            Picasso.with(context).load(url).fit().into(imageView)

        }

        @BindingAdapter("resId")
        @JvmStatic
        fun loadImage(imageView: YogcnImageView, resId: Int) {
            if (resId == 0) return
            var context = imageView.context
            Picasso.with(context).load(resId).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).fit().into(imageView)

        }
    }
}