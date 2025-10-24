package com.persona.androidstarter.entrance

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.persona.androidstarter.MainActivity
import com.persona.androidstarter.R

class TitleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title)

        val logo = findViewById<View>(R.id.logo)
        val tap  = findViewById<View>(R.id.tap)

        // フェードイン → パルス2回 → “Tap to Start”表示
        logo.animate()
            .alpha(1f)
            .setDuration(600)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction {
                pulse(logo) {
                    pulse(logo) {
                        tap.animate().alpha(1f).setDuration(400).start()
                    }
                }
            }.start()

        // どこをタップしてもホームへ
        findViewById<View>(R.id.root).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun pulse(view: View, onEnd: () -> Unit) {
        view.animate()
            .scaleX(1.08f).scaleY(1.08f).alpha(0.92f)
            .setDuration(260)
            .withEndAction {
                view.animate()
                    .scaleX(1f).scaleY(1f).alpha(1f)
                    .setDuration(260)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) = onEnd()
                    })
                    .start()
            }.start()
    }
}
