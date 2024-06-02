package com.shankha.epiceats.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shankha.epiceats.CheckOutActivity
import com.shankha.epiceats.R
import com.shankha.epiceats.adapter.CartAdapter
import com.shankha.epiceats.adapter.PopulerAdapter
import com.shankha.epiceats.databinding.CartItemBinding
import com.shankha.epiceats.databinding.FragmentCartBinding
import com.shankha.epiceats.model.CartItems

class CartFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodName: MutableList<String>
    private lateinit var foodPrice: MutableList<String>
    private lateinit var foodDescription: MutableList<String>
    private lateinit var foodIngredients: MutableList<String>
    private lateinit var foodImage: MutableList<String>
    private lateinit var foodQuantity: MutableList<Int>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var userId: String

    private lateinit var binding: FragmentCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        retriteveCartItems()
        binding.cartProced.setOnClickListener {
            getOrderItemDetails()
        }

        return binding.root
    }

    private fun getOrderItemDetails() {
        val orderIdReference = database.reference.child("Users").child(userId).child("CartItem")
        val FoodName = mutableListOf<String>()
        val FoodPrice = mutableListOf<String>()
        val FoodDescription = mutableListOf<String>()
        val FoodImage = mutableListOf<String>()
        val FoodIngredient = mutableListOf<String>()
        val FoodQuantities =mutableListOf<Int>()   // cartAdapter.getUpdatedOrderQuentities()

        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val orderItem: CartItems? = foodSnapshot.getValue(CartItems::class.java)
                    orderItem?.foodName?.let { FoodName.add(it) }
                    orderItem?.foodPrice?.let { FoodPrice.add(it) }
                    orderItem?.foodDescription?.let { FoodDescription.add(it) }
                    orderItem?.foodIngredient?.let { FoodIngredient.add(it) }
                    orderItem?.foodQuantity?.let { FoodQuantities.add(it) }
                    orderItem?.foodImage?.let { FoodImage.add(it) }
                }
                orderNow(
                    FoodName,
                    FoodPrice,
                    FoodDescription,
                    FoodImage,
                    FoodIngredient,
                    FoodQuantities
                )
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    context,
                    "Order Processing Failed Please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun orderNow(
        foodName: MutableList<String>,
        foodPrice: MutableList<String>,
        foodDescription: MutableList<String>,
        foodImage: MutableList<String>,
        foodIngredient: MutableList<String>,
        foodQuantities: MutableList<Int>
    ) {
        if(isAdded && context!=null){
            val intent =Intent(context,CheckOutActivity::class.java)
            intent.putExtra("FoodItemName",foodName as ArrayList<String>)
            intent.putExtra("FoodItemPrice",foodPrice as ArrayList<String>)
            intent.putExtra("FoodItemDescription",foodDescription as ArrayList<String>)
            intent.putExtra("FoodItemImage",foodImage as ArrayList<String>)
            intent.putExtra("FoodItemIngredient",foodIngredient as ArrayList<String>)
            intent.putExtra("FoodItemQuantities",foodQuantities as ArrayList<Int>)
            startActivity(intent)
        }

    }

    private fun retriteveCartItems() {
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""
        val foodRef = database.reference.child("Users").child(userId).child("CartItem")
        foodName = mutableListOf()
        foodPrice = mutableListOf()
        foodDescription = mutableListOf()
        foodIngredients = mutableListOf()
        foodImage = mutableListOf()
        foodQuantity = mutableListOf()
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val cartItems = foodSnapshot.getValue(CartItems::class.java)
                    cartItems?.foodName?.let { foodName.add(it) }
                    cartItems?.foodPrice?.let { foodPrice.add(it) }
                    cartItems?.foodDescription?.let { foodDescription.add(it) }
                    cartItems?.foodIngredient?.let { foodIngredients.add(it) }
                    cartItems?.foodImage?.let { foodImage.add(it) }
                    cartItems?.foodQuantity?.let { foodQuantity.add(it) }
                }
                setAdapters()
            }

            fun setAdapters() {
                cartAdapter = CartAdapter(
                    requireContext(),
                    foodName,
                    foodPrice,
                    foodDescription,
                    foodImage,
                    foodQuantity,
                    foodIngredients
                )
                binding.cartRecyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                binding.cartRecyclerView.adapter = cartAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Data not Fetch", Toast.LENGTH_SHORT).show()
            }

        })

    }

}