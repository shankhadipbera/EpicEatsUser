package com.shankha.epiceats

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.shankha.epiceats.databinding.ActivityOfferDetailsBinding

class OfferDetailsActivity : AppCompatActivity() {
    private val binding: ActivityOfferDetailsBinding by lazy{
        ActivityOfferDetailsBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener { finish() }
        val OfferT = intent.getStringExtra("Otitle")
        val OfferD = intent.getStringExtra("Odesc")
        val Oimg = intent.getStringExtra("Oimg")

        binding.offerDetails.text=OfferD
        binding.offerTitle.text=OfferT
        Glide.with(this).load(Uri.parse(Oimg)).into(binding.offerPic)

    }
}