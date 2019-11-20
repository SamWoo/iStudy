package com.samwoo.istudy.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.samwoo.istudy.R
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.intentFor
import kotlin.random.Random

class SplashActivity : AppCompatActivity() {
    private val SCALE_END: Float = 1.13f
    private val ANIMATION_DURATION: Long = 3 * 1000
    private val SPLASH_IMAGES = listOf<Int>(
        R.mipmap.splash0, R.mipmap.splash1, R.mipmap.splash2,
        R.mipmap.splash3, R.mipmap.splash4, R.mipmap.splash5,
        R.mipmap.splash6
    )
    private lateinit var random: Random
    private lateinit var animatorX: ObjectAnimator
    private lateinit var animatorY: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        random = Random(SystemClock.elapsedRealtime())
        iv_splash.setImageResource(SPLASH_IMAGES[random.nextInt(SPLASH_IMAGES.size)])
        animateImage()
    }

    private fun animateImage() {
        animatorX = ObjectAnimator.ofFloat(iv_splash, "scaleX", 1f, SCALE_END)
        animatorY = ObjectAnimator.ofFloat(iv_splash, "scaleY", 1f, SCALE_END)
        val animatorSet = AnimatorSet()
        animatorSet.setDuration(ANIMATION_DURATION).play(animatorX).with(animatorY)
        animatorSet.start()

        animatorSet.addListener(animator)
    }

    private val animator = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            animation?.cancel()
            val intent = intentFor<MainActivity>()
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }
}