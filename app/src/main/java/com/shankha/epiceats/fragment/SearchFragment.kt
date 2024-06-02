package com.shankha.epiceats.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shankha.epiceats.adapter.MenuAdapter
import com.shankha.epiceats.databinding.FragmentSearchBinding
import com.shankha.epiceats.model.MenuItem


class SearchFragment : Fragment() {
    private lateinit var binding : FragmentSearchBinding
    private lateinit var adapter:MenuAdapter
    private lateinit var auth:FirebaseAuth
    private lateinit var database :FirebaseDatabase
    private val originalMenuItems = mutableListOf<MenuItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentSearchBinding.inflate(inflater,container,false)

        retrieveMenuItem()

        setupSearchView()

        return binding.root
    }

    private fun retrieveMenuItem() {
        database=FirebaseDatabase.getInstance()
        val foodReference :DatabaseReference =database.reference.child("Menu")
        foodReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val menuItem =foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let {
                        originalMenuItems.add(it)
                    }
                }
                showAllMenu()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun showAllMenu() {
        val filteredMenuItem = ArrayList(originalMenuItems)
        setAdapter(filteredMenuItem)
    }

    private fun setAdapter(filteredMenuItem: List<MenuItem>) {
        adapter= MenuAdapter(filteredMenuItem,requireContext())
        binding.menuRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecycleView.adapter=adapter

    }

    private fun setupSearchView() {
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterMenuIitem(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               filterMenuIitem(newText)
                return true
            }

        })
    }

    private fun filterMenuIitem(query: String?) {
       val filteredMenuItems=originalMenuItems.filter {
           it.foodName?.contains(query.toString(), ignoreCase = true)==true
       }
        setAdapter(filteredMenuItems)
        }


    }

