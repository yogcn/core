package com.yogcn.core.view

import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatImageView
import android.text.TextUtils
import android.util.AttributeSet
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.yogcn.core.util.DisplayUtil

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
            if (!TextUtils.isEmpty(url))
                Picasso.get().load(url).placeholder(place).error(error).fit().into(imageView)

        }

        @BindingAdapter(*arrayOf("res", "error", "place"))
        @JvmStatic
        fun loadImage(imageView: YogcnImageView, res: Int, error: Drawable, place: Drawable) {
            Picasso.get().load(res).placeholder(place).error(error).fit().into(imageView)

        }

        @BindingAdapter(*arrayOf("url"))
        @JvmStatic
        fun loadImage(imageView: YogcnImageView, url: String?) {
            if (!TextUtils.isEmpty(url))
                Picasso.get().load(url).fit().into(imageView)

        }

        @BindingAdapter("resId")
        @JvmStatic
        fun loadImage(imageView: YogcnImageView, resId: Int) {
            if (resId != 0) return
            Picasso.get().load(resId).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).fit().into(imageView)
        }

        @BindingAdapter(*arrayOf("url", "error", "place", "width", "height"))
        @JvmStatic
        fun loadImage(imageView: YogcnImageView, url: String?, error: Drawable, place: Drawable, width: Int, height: Int) {
            if (!TextUtils.isEmpty(url)) {
                var targetWidth = DisplayUtil.getInstance().scale(width)
                var targetHeight = DisplayUtil.getInstance().scale(height)
                Picasso.get().load(url).placeholder(place).error(error).resize(targetWidth, targetHeight).centerCrop().into(imageView)
            }

        }
    }
}