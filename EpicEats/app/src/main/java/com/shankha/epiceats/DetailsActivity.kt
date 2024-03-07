package com.shankha.epiceats

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.shankha.epiceats.databinding.ActivityDetailsBinding
import com.shankha.epiceats.model.CartItems

class DetailsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var foodName:String?=null
    private var foodPrice:String?=null
    private var foodDescription:String?=null
    private var foodIngredients:String?=null
    private var foodImage:String?=null
    private val binding:ActivityDetailsBinding by lazy{
        ActivityDetailsBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()

        foodName= intent.getStringExtra("MenuItemFoodName")
        foodPrice= intent.getStringExtra("MenuItemFoodPrice")
        foodDescription= intent.getStringExtra("MenuItemFoodDescription")
        foodIngredients= intent.getStringExtra("MenuItemFoodIngredient")
        foodImage=intent.getStringExtra("MenuItemFoodPic")

        binding.foodName.text=foodName
        binding.ingredents.text=foodIngredients
        binding.foodPrice.text= "Rupees : $foodPrice"
        binding.shortDescription.text=foodDescription
        val uri= Uri.parse(foodImage)
        Glide.with(this).load(uri).into(binding.foodImage)

        binding.btnBack.setOnClickListener { finish() }

        binding.addtoCartBtn.setOnClickListener {
            addItemToCart()
        }

    }

    private fun addItemToCart() {
        val database= FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""
        val cartItem= CartItems(foodName.toString(),foodPrice.toString(),foodDescription.toString(),foodImage.toString(),1,foodIngredients.toString())

        database.child("Users").child(userId).child("CartItem").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this,"Item added to cart Successfully",Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this,"Failed to add item to cart\n Please try again",Toast.LENGTH_SHORT).show()
        }

    }
}