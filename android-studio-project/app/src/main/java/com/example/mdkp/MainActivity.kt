package com.example.mdkp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val but = findViewById<Button>(R.id.startedBut).setOnClickListener(){
            val intent = Intent(this, autorizing::class.java)
            startActivity(intent)
        }
    }
}


