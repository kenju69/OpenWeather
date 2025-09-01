package com.openweather.exam.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.openweather.exam.data.AppDatabase
import com.openweather.exam.data.WeatherRepository
import com.openweather.exam.databinding.ActivityLoginBinding
import com.openweather.exam.viewmodel.AuthViewModel
import com.openweather.exam.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.flow.collectLatest

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: AuthViewModel by viewModels {
        val db = AppDatabase.getDatabase(applicationContext)
        AuthViewModelFactory(WeatherRepository(db.userDao(), db.weatherDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(username, password)
        }

        binding.tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Observe login result
        lifecycleScope.launchWhenStarted {
            viewModel.authResult.collectLatest { result ->
                when (result) {
                    true -> {
                        startActivity(Intent(this@LoginActivity, WeatherSearchActivity::class.java))
                        finish()
                    }
                    false -> Toast.makeText(this@LoginActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    null -> {} // no action yet
                }
            }
        }
    }
}
