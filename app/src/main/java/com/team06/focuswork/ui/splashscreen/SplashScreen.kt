package com.team06.focuswork.ui.splashscreen

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.team06.focuswork.MainActivity
import com.team06.focuswork.R
import com.team06.focuswork.ui.login.LoginActivity


class SplashScreen : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 2200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //taken from: https://stackoverflow.com/questions/27301586/repeat-pulse-animation/32619737#32619737
        val iv = findViewById<View>(R.id.logoImage) as ImageView

        val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
            iv,
            PropertyValuesHolder.ofFloat("scaleX", 1.2f),
            PropertyValuesHolder.ofFloat("scaleY", 1.2f)
        )
        scaleDown.duration = 310

        scaleDown.repeatCount = ObjectAnimator.INFINITE
        scaleDown.repeatMode = ObjectAnimator.REVERSE

        scaleDown.start()


        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        Handler(Looper.getMainLooper())
            .postDelayed({ /* Create an Intent that will start the Menu-Activity. */
                val mainIntent = Intent(this@SplashScreen, LoginActivity::class.java)
                this@SplashScreen.startActivity(mainIntent)
                finish()
            }, SPLASH_DISPLAY_LENGTH.toLong())
    }
}