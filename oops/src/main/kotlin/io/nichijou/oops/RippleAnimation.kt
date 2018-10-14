package io.nichijou.oops

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.getAbsoluteX
import io.nichijou.oops.ext.getAbsoluteY

@SuppressLint("ViewConstructor")
class RippleAnimation private constructor(
        context: Context,
        private val mStartX: Float,
        private val mStartY: Float,
        private val mStartRadius: Int
) : View(context) {

    private val mPaint: Paint = Paint()
    private val mRootView: ViewGroup = context.activity().window.decorView as ViewGroup
    private var mBackground: Bitmap? = null
    private var mMaxRadius: Float = 0f
    private var mCurrentRadius: Int = 0
    private var isStarted: Boolean = false
    private var mDuration: Long = 0
    private var mOnAnimationEndListener: OnAnimationEndListener? = null
    private var mAnimatorListener: Animator.AnimatorListener? = null
    private var mAnimatorUpdateListener: ValueAnimator.AnimatorUpdateListener? = null

    private val animator: ValueAnimator
        get() {
            val valueAnimator = ValueAnimator.ofFloat(0f, mMaxRadius).setDuration(mDuration)
            valueAnimator.addUpdateListener(mAnimatorUpdateListener)
            valueAnimator.addListener(mAnimatorListener)
            return valueAnimator
        }

    init {
        mPaint.isAntiAlias = true
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        updateRadius()
        initListener()
    }

    fun start() {
        if (!isStarted) {
            isStarted = true
            updateBackground()
            startAndAttach()
            animator.start()
        }
    }

    fun cancel() {
        if (isStarted) {
            animator.removeAllUpdateListeners()
            animator.removeListener(mAnimatorListener)
            animator.cancel()
        }
    }

    fun setDuration(duration: Long): RippleAnimation {
        mDuration = duration
        return this
    }

    private fun initListener() {
        mAnimatorListener = object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                endAndDetach()
                isStarted = false
            }
        }
        mAnimatorUpdateListener = ValueAnimator.AnimatorUpdateListener { animation ->
            mCurrentRadius = (animation.animatedValue as Float).toInt() + mStartRadius
            postInvalidate()
        }
    }

    private fun updateRadius() {
        val leftTop = RectF(0f, 0f, mStartX + mStartRadius, mStartY + mStartRadius)
        val rightTop = RectF(leftTop.right, 0f, mRootView.right.toFloat(), leftTop.bottom)
        val leftBottom = RectF(0f, leftTop.bottom, leftTop.right, mRootView.bottom.toFloat())
        val rightBottom = RectF(leftBottom.right, leftTop.bottom, mRootView.right.toFloat(), leftBottom.bottom)
        val leftTopHypotenuse = Math.sqrt(Math.pow(leftTop.width().toDouble(), 2.0) + Math.pow(leftTop.height().toDouble(), 2.0))
        val rightTopHypotenuse = Math.sqrt(Math.pow(rightTop.width().toDouble(), 2.0) + Math.pow(rightTop.height().toDouble(), 2.0))
        val leftBottomHypotenuse = Math.sqrt(Math.pow(leftBottom.width().toDouble(), 2.0) + Math.pow(leftBottom.height().toDouble(), 2.0))
        val rightBottomHypotenuse = Math.sqrt(Math.pow(rightBottom.width().toDouble(), 2.0) + Math.pow(rightBottom.height().toDouble(), 2.0))
        mMaxRadius = Math.max(Math.max(leftTopHypotenuse, rightTopHypotenuse), Math.max(leftBottomHypotenuse, rightBottomHypotenuse)).toFloat()
    }


    private fun startAndAttach() {
        this.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mRootView.addView(this)
    }


    private fun endAndDetach() {
        mOnAnimationEndListener?.onAnimationEnd()
        mRootView.removeView(this)
        mBackground = mBackground?.let {
            if (!it.isRecycled) it.recycle()
            null
        }
        mAnimatorListener = null
        mAnimatorUpdateListener = null
        mOnAnimationEndListener = null
    }


    private fun updateBackground() {
        mBackground?.let {
            if (!it.isRecycled) {
                it.recycle()
            }
        }
        mRootView.isDrawingCacheEnabled = true
        mBackground = mRootView.drawingCache
        mBackground = mBackground?.let {
            Bitmap.createBitmap(it)
        }
        mRootView.isDrawingCacheEnabled = false
    }

    override fun onDraw(canvas: Canvas) {
        if (mBackground == null) return
        val layer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        } else {
            canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        }
        canvas.drawBitmap(mBackground!!, 0f, 0f, null)
        canvas.drawCircle(mStartX, mStartY, mCurrentRadius.toFloat(), mPaint)
        canvas.restoreToCount(layer)
    }

    fun setOnAnimationEndListener(listener: OnAnimationEndListener): RippleAnimation {
        mOnAnimationEndListener = listener
        return this
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return true
    }

    interface OnAnimationEndListener {
        fun onAnimationEnd()
    }

    companion object {

        fun create(onClickView: View): RippleAnimation {
            val context = onClickView.context
            val newWidth = onClickView.width / 2
            val newHeight = onClickView.height / 2
            val startX = onClickView.getAbsoluteX() + newWidth
            val startY = onClickView.getAbsoluteY() + newHeight
            val radius = Math.max(newWidth, newHeight)
            return RippleAnimation(context, startX, startY, radius)
        }
    }
}