package com.shankha.epiceats.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shankha.epiceats.adapter.MenuAdapter
import com.shankha.epiceats.databinding.FragmentMenuBottomSheetBinding
import com.shankha.epiceats.model.MenuItem

class MenuBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var  binding: FragmentMenuBottomSheetBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var  database: FirebaseDatabase
    private lateinit var menuItems:MutableList<MenuItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentMenuBottomSheetBinding.inflate(inflater,container,false)

        retriveMenuItems()


        binding.bottomSheetBack.setOnClickListener {
            dismiss()
        }



        return binding.root
    }

    private fun retriveMenuItems() {
        database=FirebaseDatabase.getInstance()
        val foodRef= database.reference.child("Menu")
        menuItems= mutableListOf()
        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val menuItem= foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
               setAdapter()
            }


            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
    private fun setAdapter() {
        val adapter=MenuAdapter(menuItems,requireContext())
        binding.menuRecycleView.layoutManager=LinearLayoutManager(requireContext())
        binding.menuRecycleView.adapter=adapter
    }
}