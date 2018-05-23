package com.example.youtubebuttonview

/**
 * Created by anweshmishra on 23/05/18.
 */
import android.app.Activity
import android.view.View
import android.view.MotionEvent
import android.graphics.*
import android.content.*

class YoutubeButtonView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val renderer : Renderer = Renderer(this)

    private var youtubeButtonListener : YoutubeButtonListener? = null
    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    fun addYoutubeButtonListener(onCompleteListener: () -> Unit) {
        youtubeButtonListener = YoutubeButtonListener(onCompleteListener)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State (var prevScale : Float = 0f, var dir : Float = 0f, var j : Int = 0) {

        val scales : Array<Float> = arrayOf(0f, 0f, 0f)

        fun update(stopcb : (Float) -> Unit) {
            scales[j] += dir * 0.1f
            if (Math.abs(scales[j] - prevScale) > 1) {
                scales[j]  = prevScale + dir
                j += dir.toInt()
                if (j == scales.size || j == -1) {
                    j -= dir.toInt()
                    dir = 0f
                    prevScale = scales[j]
                    stopcb(prevScale)
                }
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(updatecb : () -> Unit) {
            if (animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class YoutubeButton (var i : Int, val state : State = State()) {

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val size : Float = (Math.min(w, h) / 3) * state.scales[0]
            val triSize : Float = (Math.min(w, h)/9) * state.scales[1]
            canvas.save()
            canvas.translate(w/2, h/2)
            paint.color = Color.parseColor("#e53935")
            canvas.drawRoundRect(RectF(-size, -2 * size / 3, size, 2 * size / 3), size/4, size/4, paint)
            canvas.save()
            canvas.rotate(90f * state.scales[2])
            paint.color = Color.WHITE
            val path : Path = Path()
            path.moveTo(-triSize/2, triSize/2)
            path.lineTo(triSize/2, triSize/2)
            path.lineTo(0f, -triSize/2)
            path.lineTo(-triSize/2, triSize/2)
            canvas.drawPath(path, paint)
            canvas.restore()
            canvas.restore()
        }

        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }

    data class Renderer (var view : YoutubeButtonView) {

        val animator : Animator = Animator(view)

        val ytButton : YoutubeButton = YoutubeButton(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            ytButton.draw(canvas, paint)
            animator.animate {
                ytButton.update { scale ->
                    animator.stop()
                    takeIf { scale == 1f }?.apply {
                        view.youtubeButtonListener?.onCompleteListener?.invoke()
                    }
                }
            }
        }

        fun handleTap() {
            ytButton.startUpdating {
                animator.start()
            }
        }
    }
    companion object {
        fun create(activity : Activity) : YoutubeButtonView {
            val view : YoutubeButtonView = YoutubeButtonView(activity)
            activity.setContentView(view)
            return view
        }
    }

    data class YoutubeButtonListener(var onCompleteListener : () -> Unit)
}