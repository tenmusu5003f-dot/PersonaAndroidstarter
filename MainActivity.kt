<activity android:name="com.persona.androidstarter.MainActivity" ...>

package com.persona.androidstarter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.persona.androidstarter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textView.text = "Hello Persona!"
    }
}
