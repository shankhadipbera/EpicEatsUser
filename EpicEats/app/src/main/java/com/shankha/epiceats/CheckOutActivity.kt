package com.shankha.epiceats

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shankha.epiceats.adapter.CartAdapter
import com.shankha.epiceats.databinding.ActivityCheckOutBinding
import com.shankha.epiceats.model.OrderDetails

class CheckOutActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var foodItemName: ArrayList<String>
    private lateinit var foodItemPrice: ArrayList<String>
    private lateinit var foodItemDescription: ArrayList<String>
    private lateinit var foodItemImage: ArrayList<String>
    private lateinit var foodItemIngredient: ArrayList<String>
    private lateinit var foodItemQuantities: ArrayList<Int>
    private lateinit var userId: String
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phoneNo: String
    private lateinit var totalAmount: String

    private val binding : ActivityCheckOutBinding by lazy{
       ActivityCheckOutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        auth= FirebaseAuth.getInstance()
        databaseReference=FirebaseDatabase.getInstance().reference
        saveUserData()

        val intent =intent
        foodItemName = intent.getStringArrayListExtra("FoodItemName") as ArrayList<String>
        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice") as ArrayList<String>
        foodItemDescription = intent.getStringArrayListExtra("FoodItemDescription") as ArrayList<String>
        foodItemIngredient = intent.getStringArrayListExtra("FoodItemIngredient") as ArrayList<String>
        foodItemImage = intent.getStringArrayListExtra("FoodItemImage") as ArrayList<String>
        foodItemQuantities = intent.getIntegerArrayListExtra("FoodItemQuantities") as ArrayList<Int>

        totalAmount ="₹"+calculateTotalAmount().toString()
        binding.totalAmount.setText(totalAmount)

        binding.placeOrderBtn.setOnClickListener {
           // val bottomSheetDialog= CongratsBottomSheetFragment()
          //  bottomSheetDialog.show(supportFragmentManager,"test")
          //  finish()

            name= binding.name.text.toString().trim()
            address=binding.address.text.toString().trim()
            phoneNo=binding.contactNo.text.toString().trim()
            if(name.isEmpty()||address.isEmpty()||phoneNo.isEmpty()){
                Toast.makeText(this,"Please fill all the details",Toast.LENGTH_SHORT).show()
            }else{
                placeOrder()
            }
        }
    }

    private fun placeOrder() {
        userId =auth.currentUser?.uid.toString()
        val time =System.currentTimeMillis()
        val itemPushKey =databaseReference.child("OrderDetails").push().key
        val orderDetails =OrderDetails(userId,name,foodItemName,foodItemImage,foodItemPrice,foodItemQuantities,address,totalAmount,phoneNo,false,false,itemPushKey,time,false)
        val orderReference =databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetails).addOnSuccessListener {
            startActivity(Intent(this,CongratsActivity::class.java))
            removeItemsFromCart()
            addOrderToHistory(orderDetails)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this,"Failed to placed Order",Toast.LENGTH_SHORT).show()
        }
    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("Users").child(userId).child("BuyHistory").child("PendingOrder").child(orderDetails.itemPushKey!!).setValue(orderDetails).addOnSuccessListener {

        }


    }

    private fun removeItemsFromCart() {
        val cartItemsReference =databaseReference.child("Users").child(userId).child("CartItem")
        cartItemsReference.removeValue()
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount =0
        for(i in 0 until foodItemPrice.size){
            var price =foodItemPrice[i]
            val lastChar = price.last()
            val priceIntValue = if(lastChar=='$'){
                price.dropLast(1).toInt()
            }else{
                price.toInt()
            }
            var quantities = foodItemQuantities[i]
            totalAmount += priceIntValue*quantities
        }
        return  totalAmount

    }

    private fun saveUserData() {
        val user= auth.currentUser
        if(user!=null){
            val userId= user.uid
            val userReference= databaseReference.child("Users").child(userId)
            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val Name= snapshot.child("userName").getValue(String::class.java)?:""
                        val Address= snapshot.child("address").getValue(String::class.java)?:""
                        val Phone = snapshot.child("phoneNo").getValue(String::class.java)?:""
                        binding.apply {
                            name.setText(if (Name.isNotEmpty()) Name else "")
                            address.setText(if (Address.isNotEmpty()) Address else "")
                            contactNo.setText(if (Phone.isNotEmpty()) Phone else "")
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}