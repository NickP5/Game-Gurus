package com.example.theapp

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.animation.Animation
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.theapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    lateinit var ue_input: EditText
    lateinit var pass_input: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ue_input = binding.root.findViewById<EditText>(R.id.loginUE)
        pass_input = binding.root.findViewById<EditText>(R.id.loginPass)

        binding.loginButton.setOnClickListener {
            val ue = ue_input.text.toString()
            val pass = pass_input.text.toString()

            //eventually only run this is ue/pass check works
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // code to animate background
        val drawable : AnimationDrawable = binding.root.background as AnimationDrawable

        drawable.setEnterFadeDuration(1250)
        drawable.setExitFadeDuration(2500)
        drawable.start()
    }
}