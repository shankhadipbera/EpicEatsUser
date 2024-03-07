package com.shankha.epiceats.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shankha.epiceats.databinding.CartItemBinding
import com.shankha.epiceats.databinding.PopulerItemBinding

class CartAdapter(
    private val context: Context,
    private val CartItemFood: MutableList<String>,
    private val CartItemPrice: MutableList<String>,
    private val CartItemDescription: MutableList<String>,
    private val CartItemImage: MutableList<String>,
    private val CartItemQuantity: MutableList<Int>,
    private val CartItemIngredient: MutableList<String>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val auth = FirebaseAuth.getInstance()
    private var itemQuantities: MutableList<Int> = mutableListOf()


    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        val cartItemNumber = CartItemFood.size
        itemQuantities = MutableList(cartItemNumber) { 1 }
        cartItemReference = database.reference.child("Users").child(userId).child("CartItem")


    }

    companion object {
      //  private var itemQuantities: IntArray = intArrayOf()
        private lateinit var cartItemReference: DatabaseReference
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return CartItemFood.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    fun getUpdatedOrderQuentities(): MutableList<Int> {
        val itemQuantity = mutableListOf<Int>()
        itemQuantity.addAll(CartItemQuantity)
        return itemQuantity

    }

    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                cartFoodname.text = CartItemFood[position]
                cartFoodname.isSelected = true
                cartfoodPrice.text = CartItemPrice[position]
                cartItemCount.text = quantity.toString()
                val uri = CartItemImage[position]
                val Uri = Uri.parse(uri)
                Glide.with(context).load(Uri).into(cartFoodPic)

                minusBtn.setOnClickListener {
                    decreseQuentity(adapterPosition) //position for three item
                }
                plusBtn.setOnClickListener {
                    increseQuentity(adapterPosition)
                }
                deleteBtn.setOnClickListener {
                    deleteItem(adapterPosition)
                }

            }

        }

        private fun decreseQuentity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
               // CartItemQuantity[position]= itemQuantities[position]
                updateCartItem(position)
                binding.cartItemCount.text = itemQuantities[position].toString()
            }
        }

        private fun increseQuentity(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
               // CartItemQuantity[position]= itemQuantities[position]
                updateCartItem(position)
                binding.cartItemCount.text = itemQuantities[position].toString()

            }
        }

        private fun deleteItem(position: Int) {
            val positionRetirieve = position
            getUniqueKeyOfPosition(positionRetirieve) { uniqueKey ->
                if (uniqueKey != null) {
                    removeItem(position, uniqueKey)
                }
            }

        }

        private fun updateCartItem(position: Int) {
            getUniqueKeyOfPosition(position) { uniqueKey ->
                if (uniqueKey != null) {
                    val updatedQuantity = itemQuantities[position]
                    cartItemReference.child(uniqueKey).child("foodQuantity").setValue(updatedQuantity)
                        .addOnSuccessListener {
                            // Handle success if needed
                        }
                        .addOnFailureListener {
                            // Handle failure if needed
                        }
                }
            }
        }

        private fun removeItem(position: Int, uniqueKey: String) {
            if (uniqueKey != null) {
                cartItemReference.child(uniqueKey).removeValue().addOnSuccessListener {
                    CartItemPrice.removeAt(position)
                    CartItemDescription.removeAt(position)
                    CartItemIngredient.removeAt(position)
                    CartItemImage.removeAt(position)
                    CartItemFood.removeAt(position)
                    CartItemQuantity.removeAt(position)
                    Toast.makeText(context, "Item remove from cart ", Toast.LENGTH_SHORT).show()
                   // itemQuantities = itemQuantities.filterIndexed { index, i -> index != position }.toIntArray()
                    itemQuantities.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, CartItemFood.size)
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed To Delete !!", Toast.LENGTH_SHORT).show()
                }
            }

        }

        private fun getUniqueKeyOfPosition(positionRetirieve: Int, onComplete: (String?) -> Unit) {
            cartItemReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey: String? = null
                    snapshot.children.forEachIndexed { index, dataSnapshot ->
                        if (index == positionRetirieve) {
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {
                    onComplete(null)
                }

            })

        }
    }


    }


