package com.yogcn.core.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.yogcn.core.R


/**
 * @author luoxiaoyong
 * @version 1.3
 * @date 2015/1/14 14:33
 * @company 成都极速帮网络科技有限公司
 */
class YogcnRatingBar : LinearLayout {
    private var secondProgress: Drawable? = null
    private var progress: Drawable? = null
    private var horizontal: Int = 20
    private var numberRating = 5
    private var rating = 1
    private var unTouch = false
    private var onRatingBarChangeListener: OnRatingChangeListener? = null

    interface OnRatingChangeListener {
        /**
         * @param bar
         * @param rating
         * @param fromUser
         */
        fun onRatingChanged(bar: YogcnRatingBar, rating: Float, fromUser: Boolean)

    }

    fun setOnRatingBarChangeListener(onRatingBarChangeListener: OnRatingChangeListener) {
        this.onRatingBarChangeListener = onRatingBarChangeListener
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RatingBar)

        horizontal = a.getDimensionPixelOffset(R.styleable.RatingBar_horizontal_space, HORIZONTAL_SPACE)
        numberRating = a.getInt(R.styleable.RatingBar_number_rating, NUMBER_RATING)
        unTouch = a.getBoolean(R.styleable.RatingBar_un_touch, false)
        rating = a.getInt(R.styleable.RatingBar_rating, DEFAULT_RATING)

        secondProgress = if (a.hasValue(R.styleable.RatingBar_secondProgress))
            a.getDrawable(R.styleable.RatingBar_secondProgress)
        else
            context.resources.getDrawable(DEFAULT_SECOND_PROGRESS)

        progress = if (a.hasValue(R.styleable.RatingBar_progress))
            a.getDrawable(R.styleable.RatingBar_progress)
        else
            context.resources.getDrawable(DEFAULT_PROGRESS)
        init(context)
    }


    private fun init(context: Context) {
        this.orientation = LinearLayout.HORIZONTAL
        this.gravity = Gravity.CENTER
        for (i in 0 until numberRating) {
            val ratingBar = ImageView(context)
            ratingBar.tag = i + 1
            ratingBar.scaleType = ImageView.ScaleType.FIT_CENTER
            ratingBar.setOnClickListener { setRating(ratingBar.tag as Int) }
            ratingBar.setImageDrawable(secondProgress)
            val params = LinearLayout.LayoutParams(60, 60)
            if (i != 0)
                params.leftMargin = horizontal
            this.addView(ratingBar, params)
        }
        setRating(rating)
        if (!unTouch) {
            this.setOnTouchListener(object : View.OnTouchListener {
                private var startX: Float = 0.toFloat()

                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> startX = event.x
                        MotionEvent.ACTION_MOVE -> {
                            val distance = event.x - startX
                            if (distance > 0) {
                                setRating((numberRating * distance / width).toInt())
                            } else {
                                setRating((numberRating * (1 + distance / width)).toInt())
                            }
                        }
                        MotionEvent.ACTION_UP -> {
                            val distance1 = event.x - startX
                            if (distance1 > 0) {
                                setRating((numberRating * distance1 / width).toInt())
                            } else {
                                setRating((numberRating * (1 + distance1 / width)).toInt())
                            }
                        }
                    }
                    return true
                }
            })
        }
    }

    /**
     * 最低从1开始
     *
     * @return
     */
    fun setRating(rating: Int) {
        var rating = rating
        if (rating > numberRating) {
            rating = numberRating
        } else if (rating < 0)
            rating = 0

        for (i in 0 until numberRating) {
            val ratingBar = this.getChildAt(i) as ImageView
            ratingBar.setImageDrawable(secondProgress)
            if (i < rating)
                ratingBar.setImageDrawable(progress)
        }
        this.rating = rating

        if (onRatingBarChangeListener != null) {
            onRatingBarChangeListener!!.onRatingChanged(this, rating.toFloat(), false)
        }
    }


    /**
     * 最低从1开始
     *
     * @return
     */
    fun getRating(): Int {
        return rating
    }

    companion object {
        val HORIZONTAL_SPACE = 6
        val NUMBER_RATING = 5
        val DEFAULT_RATING = 0
        val DEFAULT_SECOND_PROGRESS = R.drawable.bar_grey
        val DEFAULT_PROGRESS = R.drawable.bar_red
    }


}
