package com.shankha.epiceats

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shankha.epiceats.databinding.ActivityCongratsBinding

class CongratsActivity : AppCompatActivity() {
    private val binding :ActivityCongratsBinding by lazy {
        ActivityCongratsBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.goHomeBtn.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}