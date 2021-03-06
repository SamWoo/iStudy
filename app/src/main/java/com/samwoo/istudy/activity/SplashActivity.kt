package com.samwoo.istudy.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.samwoo.istudy.App.Companion.context
import com.samwoo.istudy.R
import com.samwoo.istudy.constant.Constant
import com.samwoo.istudy.util.NetworkUtil
import com.samwoo.istudy.util.Preference
import com.samwoo.istudy.util.randomColor
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.uiThread
import java.net.URL
import kotlin.random.Random

class SplashActivity : AppCompatActivity() {
    private val SCALE_END: Float = 1.13f
    private val ANIMATION_DURATION: Long = 3 * 1000
    private val SPLASH_IMAGES = listOf(
        R.mipmap.splash0, R.mipmap.splash1, R.mipmap.splash2, R.mipmap.splash3,
        R.mipmap.splash4, R.mipmap.splash5
    )
    private lateinit var random: Random
    private lateinit var animatorX: ObjectAnimator
    private lateinit var animatorY: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        /*
        if (NetworkUtil.isNetworkConnected(context)) {
            doAsync {
                val slogan = URL(Constant.SPLASH_SLOGAN).readText()
                uiThread {
                    splash_slogan.text = slogan
                }
            }

            Glide.with(context)
                .load(Constant.SPLASH_PIC_URL)
                .apply(
                    RequestOptions().centerCrop()
                        .placeholder(R.mipmap.splash3)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                )
                .transition(DrawableTransitionOptions().crossFade())
                .into(iv_splash)
        } else {
            random = Random(SystemClock.elapsedRealtime())
            iv_splash.setImageResource(SPLASH_IMAGES[random.nextInt(SPLASH_IMAGES.size)])
        }
         */

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