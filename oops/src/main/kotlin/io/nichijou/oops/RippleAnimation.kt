package io.nichijou.oops

/**
 * Created by wuyr on 3/15/18 5:23 PM.
 */

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

/**
 * Created by wuyr on 3/15/18 5:23 PM.
 * GitHub: https://github.com/wuyr/RippleAnimation
 */

@SuppressLint("ViewConstructor")
class RippleAnimation private constructor(
        context: Context,
        private val mStartX: Float,
        private val mStartY: Float,//扩散的起点
        private val mStartRadius: Int
) : View(context) {

    private var mBackground: Bitmap? = null//屏幕截图
    private var mPaint: Paint? = null
    private var mMaxRadius: Float = 0f
    private var mCurrentRadius: Int = 0
    private var isStarted: Boolean = false
    private var mDuration: Long = 0
    private var mRootView: ViewGroup? = null//DecorView
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
        //获取activity的根视图,用来添加本View
        mRootView = context.activity().window.decorView as ViewGroup
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        //设置为擦除模式
        mPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        updateMaxRadius()
        initListener()
    }

    /**
     * 开始播放动画
     */
    fun start() {
        if (!isStarted) {
            isStarted = true
            updateBackground()
            attachToRootView()
            animator.start()
        }
    }

    fun cancel() {
        if (isStarted) {
            animator.cancel()
        }
    }

    /**
     * 设置动画时长
     */
    fun setDuration(duration: Long): RippleAnimation {
        mDuration = duration
        return this
    }

    private fun initListener() {
        mAnimatorListener = object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (mOnAnimationEndListener != null) {
                    mOnAnimationEndListener!!.onAnimationEnd()
                }
                isStarted = false
                //动画播放完毕, 移除本View
                detachFromRootView()
            }
        }
        mAnimatorUpdateListener = ValueAnimator.AnimatorUpdateListener { animation ->
            //更新圆的半径
            mCurrentRadius = (animation.animatedValue as Float).toInt() + mStartRadius
            postInvalidate()
        }
    }

    /**
     * 根据起始点将屏幕分成4个小矩形,mMaxRadius就是取它们中最大的矩形的对角线长度
     * 这样的话, 无论起始点在屏幕中的哪一个位置上, 我们绘制的圆形总是能覆盖屏幕
     */
    private fun updateMaxRadius() {
        //将屏幕分成4个小矩形
        val leftTop = RectF(0f, 0f, mStartX + mStartRadius, mStartY + mStartRadius)
        val rightTop = RectF(leftTop.right, 0f, mRootView!!.right.toFloat(), leftTop.bottom)
        val leftBottom = RectF(0f, leftTop.bottom, leftTop.right, mRootView!!.bottom.toFloat())
        val rightBottom = RectF(leftBottom.right, leftTop.bottom, mRootView!!.right.toFloat(), leftBottom.bottom)
        //分别获取对角线长度
        val leftTopHypotenuse = Math.sqrt(Math.pow(leftTop.width().toDouble(), 2.0) + Math.pow(leftTop.height().toDouble(), 2.0))
        val rightTopHypotenuse = Math.sqrt(Math.pow(rightTop.width().toDouble(), 2.0) + Math.pow(rightTop.height().toDouble(), 2.0))
        val leftBottomHypotenuse = Math.sqrt(Math.pow(leftBottom.width().toDouble(), 2.0) + Math.pow(leftBottom.height().toDouble(), 2.0))
        val rightBottomHypotenuse = Math.sqrt(Math.pow(rightBottom.width().toDouble(), 2.0) + Math.pow(rightBottom.height().toDouble(), 2.0))
        //取最大值
        mMaxRadius = Math.max(Math.max(leftTopHypotenuse, rightTopHypotenuse), Math.max(leftBottomHypotenuse, rightBottomHypotenuse)).toFloat()
    }

    /**
     * 添加到根视图
     */
    private fun attachToRootView() {
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mRootView!!.addView(this)
    }

    /**
     * 从根视图中移除并释放资源
     */
    private fun detachFromRootView() {
        if (mRootView != null) {
            mRootView!!.removeView(this)
            mRootView = null
        }
        if (mBackground != null) {
            if (!mBackground!!.isRecycled) {
                mBackground!!.recycle()
            }
            mBackground = null
        }
        if (mPaint != null) {
            mPaint = null
        }
    }

    /**
     * 更新屏幕截图
     */
    private fun updateBackground() {
        if (mBackground != null && !mBackground!!.isRecycled) {
            mBackground!!.recycle()
        }
        mRootView!!.isDrawingCacheEnabled = true
        mBackground = mRootView!!.drawingCache
        mBackground = Bitmap.createBitmap(mBackground!!)
        mRootView!!.isDrawingCacheEnabled = false
    }

    override fun onDraw(canvas: Canvas) {
        //在新的图层上面绘制
        val layer: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        } else {
            canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        }
        canvas.drawBitmap(mBackground!!, 0f, 0f, null)
        canvas.drawCircle(mStartX, mStartY, mCurrentRadius.toFloat(), mPaint!!)
        canvas.restoreToCount(layer)
    }

    /**
     * 设置动画结束监听器
     */
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
            //计算起点位置
            val startX = onClickView.getAbsoluteX() + newWidth
            val startY = onClickView.getAbsoluteY() + newHeight
            //起始半径
            //因为我们要避免遮挡按钮
            val radius = Math.max(newWidth, newHeight)
            return RippleAnimation(context, startX, startY, radius)
        }

    }
}