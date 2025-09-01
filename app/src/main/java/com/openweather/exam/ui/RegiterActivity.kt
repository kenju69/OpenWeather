package com.openweather.exam.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.openweather.exam.data.AppDatabase
import com.openweather.exam.data.WeatherRepository
import com.openweather.exam.databinding.ActivityRegisterBinding
import com.openweather.exam.viewmodel.AuthViewModel
import com.openweather.exam.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.flow.collectLatest

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: AuthViewModel by viewModels {
        val db = AppDatabase.getDatabase(applicationContext)
        AuthViewModelFactory(WeatherRepository(db.userDao(), db.weatherDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.register(username, password)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.authResult.collectLatest { result ->
                if (result == true) {
                    Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                    finish() // go back to login
                }
            }
        }
    }
}
