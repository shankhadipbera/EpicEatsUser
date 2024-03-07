package com.shankha.epiceats.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.shankha.epiceats.DetailsActivity
import com.shankha.epiceats.databinding.MenuItemBinding
import com.shankha.epiceats.model.CartItems
import com.shankha.epiceats.model.MenuItem

class MenuAdapter(
    private val menuItems: List<MenuItem>,
    private val requireContext: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(MenuItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MenuViewHolder(private val binding: MenuItemBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position =adapterPosition
                if(position!=RecyclerView.NO_POSITION){
                   openDetailsActivity(position)
                }
            }
        }

        private fun openDetailsActivity(position: Int) {
            val menuItem= menuItems[position]
            val intent =Intent(requireContext,DetailsActivity::class.java)
            intent.putExtra("MenuItemFoodName",menuItem.foodName)
            intent.putExtra("MenuItemFoodPic",menuItem.foodImage)
            intent.putExtra("MenuItemFoodDescription",menuItem.foodDescription)
            intent.putExtra("MenuItemFoodIngredient",menuItem.foodIngredient)
            intent.putExtra("MenuItemFoodPrice",menuItem.foodPrice)

            requireContext.startActivity(intent)

        }

        fun bind(position: Int) {
            val menuItem= menuItems[position]
            binding.menuFoodName.text= menuItem.foodName
            binding.menuFoodName.isSelected = true
            binding.menuFoodPrice.text=menuItem.foodPrice
            val uri= Uri.parse(menuItem.foodImage)
            Glide.with(requireContext).load(uri).into(binding.menuFoodPic)

            binding .menuAddtocart.setOnClickListener {
                //Toast.makeText(requireContext,"Hello",Toast.LENGTH_SHORT).show()
                addItemToCart(menuItem)
            }

        }


    }




    private fun addItemToCart(menuItem: MenuItem) {
        val database= FirebaseDatabase.getInstance().reference
        val auth =FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid?:""
        val foodName= menuItem.foodName
        val foodPrice= menuItem.foodPrice
        val foodDescription= menuItem.foodDescription
        val foodImage= menuItem.foodImage
        val foodIngredients= menuItem.foodIngredient
        val cartItem= CartItems(foodName.toString(),foodPrice.toString(),foodDescription.toString(),foodImage.toString(),1,foodIngredients.toString())

        database.child("Users").child(userId).child("CartItem").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(requireContext,"Item added to cart Successfully",Toast.LENGTH_SHORT).show()
           // finish()
        }.addOnFailureListener {
            Toast.makeText(requireContext,"Failed to add item to cart\n Please try again",Toast.LENGTH_SHORT).show()
        }

    }

}



