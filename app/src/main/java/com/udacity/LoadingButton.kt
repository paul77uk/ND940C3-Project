package com.udacity

import android.animation.*
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.content_main.view.*
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0
    private var text = "Download"
    private var progress = 0

    private var valueAnimator = ValueAnimator.ofFloat()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 50f
        typeface = Typeface.create("", Typeface.NORMAL)
        color = Color.WHITE

    }

    private val paint2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.colorPrimary)
        setBackgroundColor(color)

    }

    private val paint3 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.colorPrimaryDark)
        setBackgroundColor(color)

    }

    private val paint4 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = resources.getColor(R.color.colorAccent)

    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Completed -> setBackgroundColor(Color.RED)
            ButtonState.Clicked -> {
                scaler()
            }

            ButtonState.Loading -> {
////                valueAnimator = ValueAnimator.ofInt(0, 360).setDuration(2000).apply {
////                    addUpdateListener {
////                        progress = it.animatedValue as Int
////                        invalidate()
////                    }
////                    repeatCount = ValueAnimator.INFINITE
////                    repeatMode = ValueAnimator.RESTART
////
////                    start()
//                }
//                loadingRect.set(0, 0, width * progress / 360, height)

            }

        }
    }

//    val customButton = findViewById<Button>(R.id.custom_button)


    private fun scaler() {

        valueAnimator = ValueAnimator.ofInt(0, 720).apply {
            addUpdateListener {
                progress = animatedValue as Int
                invalidate()
            }
            duration = 5000
            start()

//        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.1f)
//
//        val animator = ObjectAnimator.ofPropertyValuesHolder(
//            custom_button, scaleX
//        )
//        animator.duration = 5000
//        animator.repeatCount = 1
//        animator.repeatMode = ObjectAnimator.REVERSE
//        animator.disableViewDuringAnimation(custom_button)
//        animator.start()

        }
    }

    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        })
    }

    fun updateButtonState(state: ButtonState) {
        buttonState = state

    }
//    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 2f)
//    val animator = ObjectAnimator.ofPropertyValuesHolder(custom_button, scaleX)


//    init {
//        buttonState = ButtonState.Clicked
//    }


    override fun onDraw(canvas: Canvas?) {

        super.onDraw(canvas)
        canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint2)

        if (buttonState == ButtonState.Clicked) {
            canvas?.drawRect(0f, 0f, progress.toFloat() , heightSize.toFloat(), paint3)
//            canvas?.drawCircle(widthSize - 125f, heightSize / 2f + 5, 25f, paint4)

            canvas?.drawArc(widthSize - 145f,
                heightSize / 2 - 35f,
                widthSize - 75f,
                heightSize / 2 + 35f,
                0F,
                progress / 1.75f,
                true,
                paint4)
        }
        canvas?.drawText(buttonState.text, widthSize / 2f, heightSize / 2f + 18, paint)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            View.MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}