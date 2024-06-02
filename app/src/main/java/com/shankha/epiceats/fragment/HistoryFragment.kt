package com.shankha.epiceats.fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shankha.epiceats.RecentOrderItemActivity
import com.shankha.epiceats.adapter.BuyAgainAdapter
import com.shankha.epiceats.adapter.PendingOrderAdapter
import com.shankha.epiceats.databinding.FragmentHistoryBinding
import com.shankha.epiceats.model.CartItems
import com.shankha.epiceats.model.OrderDetails


class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private lateinit var pendingOrderAdapter: PendingOrderAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItem: ArrayList<OrderDetails> = arrayListOf()
    private var listOfDeliveryItem: ArrayList<OrderDetails> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        retrievePendingOrderHistory()
        retrievePreviousOrderHistory()

        return binding.root
    }

    private fun retrievePendingOrderHistory() {
        userId = auth.currentUser?.uid ?: ""
        val pendingOrderRef = database.reference.child("Users").child(userId).child("BuyHistory")
            .child("PendingOrder")
        val sortingQuery = pendingOrderRef.orderByChild("currentTime")
        sortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val pendingOrderItems = foodSnapshot.getValue(OrderDetails::class.java)
                    pendingOrderItems?.let { listOfOrderItem.add(it) }
                }
                setAdapters()
            }

            fun setAdapters() {
                pendingOrderAdapter = PendingOrderAdapter(
                    requireContext(),
                    listOfOrderItem
                )
                binding.recentOrderRecycleView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                binding.recentOrderRecycleView.adapter = pendingOrderAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Data not Fetch", Toast.LENGTH_SHORT).show()
            }

        })


    }

    private fun retrievePreviousOrderHistory() {

        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""
        val deliveredOrderRef = database.reference.child("Users").child(userId).child("BuyHistory")
            .child("DeliveredOrder")
        val sortingQuery = deliveredOrderRef.orderByChild("currentTime")
        sortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val pendingOrderItems = foodSnapshot.getValue(OrderDetails::class.java)
                    pendingOrderItems?.let { listOfDeliveryItem.add(it) }
                }
                setAdapters()
            }

            fun setAdapters() {
                buyAgainAdapter =
                    BuyAgainAdapter(listOfDeliveryItem,requireContext())
                binding.historyRecycleView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                binding.historyRecycleView.adapter = buyAgainAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Data not Fetch", Toast.LENGTH_SHORT).show()
            }

        })

    }
}




























