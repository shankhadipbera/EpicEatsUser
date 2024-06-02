package com.shankha.epiceats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.shankha.epiceats.databinding.ActivityLocationBinding

class LocationActivity : AppCompatActivity() {
    private val binding :ActivityLocationBinding by lazy {
        ActivityLocationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val locationList = arrayOf("Kolkata", " Kalyani", "Haldia", "Barasat","Durgachak","TownShip", "DumDum", "Asansole", "Durgapur", "Siliguri", "Kharagpur", "Barrackpure")
        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, locationList)
        val autoCompleteTextView =binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)
    }
}