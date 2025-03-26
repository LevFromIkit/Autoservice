package com.example.mdkp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mdkp.databinding.ActivityForgotPasswordBinding

class forgotPassword : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cancelButton.setOnClickListener{
            finish()
        }
    }
}