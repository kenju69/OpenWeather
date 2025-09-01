package com.openweather.exam.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.openweather.exam.data.AppDatabase
import com.openweather.exam.data.RetrofitInstance
import com.openweather.exam.data.WeatherRepository
import com.openweather.exam.databinding.ActivityRegisterBinding
import com.openweather.exam.viewmodel.AuthViewModel
import com.openweather.exam.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.flow.collectLatest

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: AuthViewModel by viewModels {
        val db = AppDatabase.getDatabase(applicationContext)
        AuthViewModelFactory(WeatherRepository(
            db.userDao(), db.weatherDao(),
            apiService = RetrofitInstance.api
        ))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString()
            val password = binding.etRegisterPassword.text.toString()
            viewModel.register(email, password)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.authResult.collectLatest { result ->
                if (result == true) {
                    Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                    finish() // return to login
                }
            }
        }
    }
}
