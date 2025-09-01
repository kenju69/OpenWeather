package com.openweather.exam.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.openweather.exam.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            // TODO: save user locally or via Room
            finish() // return to login
        }
    }
}
