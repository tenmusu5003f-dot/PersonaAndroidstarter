
package com.persona.androidstarter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import core.ui.HomeMenu

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 起動時に HomeMenu を呼び出す
        startActivity(Intent(this, HomeMenu::class.java))
        finish()
    }
}
