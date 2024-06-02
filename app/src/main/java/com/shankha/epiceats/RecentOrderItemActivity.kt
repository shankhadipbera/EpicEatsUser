package com.shankha.epiceats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.shankha.epiceats.adapter.RecentBuyAdapter
import com.shankha.epiceats.databinding.ActivityRecentOrderItemBinding
import com.shankha.epiceats.model.OrderDetails

class RecentOrderItemActivity : AppCompatActivity() {

    private lateinit var allFoodNames : ArrayList<String>
    private lateinit var allFoodImages : ArrayList<String>
    private lateinit var allFoodPrices : ArrayList<String>
    private lateinit var allFoodQuantitys : ArrayList<Int>

    private val binding :ActivityRecentOrderItemBinding by lazy {
        ActivityRecentOrderItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener { finish() }
      /*  allFoodNames= intent.getStringArrayListExtra("MenuItemFoodName")!!
        allFoodImages=intent.getStringArrayListExtra("PendingOrderFoodPic")!!
        allFoodPrices=intent.getStringArrayListExtra("PendingOrderFoodPrice")!!
        allFoodQuantitys=intent.getIntegerArrayListExtra("PendingOrderFoodQuantity")!!*/
      // val recentOrderItems = intent.getSerializableExtra("PendingOrdersItem") as ArrayList<OrderDetails>
        val recentOrderItems = intent.getSerializableExtra("PendingOrdersItem") as OrderDetails
         recentOrderItems?.let { orderDetails ->
                allFoodNames= orderDetails.foodNames as ArrayList<String>
                allFoodPrices= orderDetails.foodPrice as ArrayList<String>
                allFoodImages= orderDetails.foodImage as ArrayList<String>
                allFoodQuantitys= orderDetails.foodQuantities as ArrayList<Int>

        }
        setAdapter()

    }

    private fun setAdapter() {
        val rv = binding.recentOrderItemRV
        rv.layoutManager =LinearLayoutManager(this)
        val adapter =RecentBuyAdapter(this,allFoodNames,allFoodImages,allFoodPrices,allFoodQuantitys)
        rv.adapter= adapter
    }
}