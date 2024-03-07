package com.shankha.epiceats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.shankha.epiceats.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding :ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var navController= findNavController(R.id.fragmentContainerView)
        binding.bottomNavigationView.setupWithNavController(navController)

        binding.notificationIcon.setOnClickListener {
            val bottomSheetDialog =NotificationBottomFragment()
            bottomSheetDialog.show(supportFragmentManager,"tests")
        }

    }
}