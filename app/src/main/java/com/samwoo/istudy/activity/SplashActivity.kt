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
        R.drawable.splash0,
        R.drawable.splash1,
        R.drawable.splash2,
        R.drawable.splash3,
        R.drawable.splash4,
        R.drawable.splash5,
        R.drawable.splash6,
        R.drawable.splash7,
        R.drawable.splash8,
        R.drawable.splash9,
        R.drawable.splash10,
        R.drawable.splash11,
        R.drawable.splash12,
        R.drawable.splash13,
        R.drawable.splash14,
        R.drawable.splash15,
        R.drawable.splash16
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
            val intent = intentFor<MainActivity>()
            startActivity(intent)
            finish()
        }
    }
}