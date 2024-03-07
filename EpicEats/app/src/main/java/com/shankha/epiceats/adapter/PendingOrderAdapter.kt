package com.shankha.epiceats.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.shankha.epiceats.DetailsActivity
import com.shankha.epiceats.RecentOrderItemActivity
import com.shankha.epiceats.databinding.PendingOrderBinding
import com.shankha.epiceats.model.OrderDetails

class PendingOrderAdapter(
    private var context: Context,
    private var listOfOrderItem: ArrayList<OrderDetails>
) : RecyclerView.Adapter<PendingOrderAdapter.PendingViewHolder>() {


    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PendingOrderAdapter.PendingViewHolder {
        return PendingViewHolder(
            PendingOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PendingOrderAdapter.PendingViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return listOfOrderItem.size
    }

    inner class PendingViewHolder(private val binding: PendingOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailsActivity(position)
                }
            }
        }

        private fun openDetailsActivity(position: Int) {
            val intent = Intent(context, RecentOrderItemActivity::class.java)
            intent.putExtra("PendingOrdersItem", listOfOrderItem[position])
            context.startActivity(intent)

        }

        fun bind(position: Int) {
            binding.apply {
                pendingFoodName.text = listOfOrderItem[position].foodNames.toString()  //.get(0)
                pendingFoodName.isSelected = true
                pendingFoodPrice.text = listOfOrderItem[position].foodPrice.toString()
                pendingFoodPrice.isSelected = true
                pendingFoodQuantity.text = listOfOrderItem[position].foodQuantities.toString()
                pendingFoodQuantity.isSelected = true
                var img = listOfOrderItem[position].foodImage?.get(0)
                var uri = Uri.parse(img.toString())
                Glide.with(context).load(uri).into(pendingOrderImage)

                val isOrderIsAccepted = listOfOrderItem[position].orderAccepted
                val isOrderDispatch = listOfOrderItem[position].orderDispatch
                val isPaymentReceived = listOfOrderItem[position].paymentReceived

                if (isOrderIsAccepted) {
                    pendingOrderAcceptedTV.text = "Accepted"
                    pendingOrderAcceptedTV.setTextColor(Color.BLUE)
                }
                if (isOrderDispatch) {
                    pendingOrderAcceptedTV.text = "Dispatch"
                    pendingOrderAcceptedTV.setTextColor(Color.GREEN)
                    pendingRecivedBtn.visibility = View.VISIBLE
                }
                if (isPaymentReceived) {
                    pendingOrderAcceptedTV.text = "Delivered"
                    pendingOrderAcceptedTV.setTextColor(Color.RED)
                    pendingRecivedBtn.visibility = View.INVISIBLE
                }

                pendingRecivedBtn.setOnClickListener {
                    auth = FirebaseAuth.getInstance()
                    database = FirebaseDatabase.getInstance()
                    userId = auth.currentUser?.uid.toString()
                    updateOrderStatus()
                    addOrderToHistory(listOfOrderItem[position])
                    removeItemsFromCart(listOfOrderItem[position])

                }


            }

        }


        private fun updateOrderStatus() {
            val itemPushKey = listOfOrderItem[position].itemPushKey
            val completeUserOrderReference =
                database.reference.child("Users").child(userId).child("BuyHistory")
                    .child("PendingOrder")
            completeUserOrderReference.child(itemPushKey!!).child("paymentReceived").setValue(true)
        }

        private fun addOrderToHistory(orderDetails: OrderDetails) {
            database.reference.child("Users").child(userId).child("BuyHistory")
                .child("DeliveredOrder").child(orderDetails.itemPushKey!!).setValue(orderDetails)
                .addOnSuccessListener {}
            database.reference.child("CompletedOrder").child(orderDetails.itemPushKey!!)
                .setValue(orderDetails).addOnSuccessListener {}
        }

        private fun removeItemsFromCart(orderDetails: OrderDetails) {
            database.reference.child("Users").child(userId).child("BuyHistory")
                .child("PendingOrder").child(orderDetails.itemPushKey!!).removeValue()
        }


    }
}










