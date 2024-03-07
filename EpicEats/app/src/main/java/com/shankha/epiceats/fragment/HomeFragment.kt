package com.shankha.epiceats.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView.ScaleType
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shankha.epiceats.OfferDetailsActivity
import com.shankha.epiceats.R
import com.shankha.epiceats.adapter.MenuAdapter
import com.shankha.epiceats.adapter.PopulerAdapter
import com.shankha.epiceats.databinding.FragmentHomeBinding
import com.shankha.epiceats.model.MenuItem
import com.shankha.epiceats.model.Offers


class HomeFragment : Fragment() {
    private lateinit var database:FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>
    private lateinit var binding : FragmentHomeBinding
    private var offerItems: ArrayList<Offers> = ArrayList()
    private var imageLists = ArrayList<SlideModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        }
    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentHomeBinding .inflate(layoutInflater,container,false)

        binding.viewMenu.setOnClickListener {
            val bottomSheetDialog= MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager,"test")
        }

        retriveAndDisplayPopulerItems()

        return binding.root
    }

    private fun retriveAndDisplayPopulerItems() {
        database=FirebaseDatabase.getInstance()
        val foodRef=database.reference.child("Menu")
        menuItems = mutableListOf()
        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val menuItem= foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                randomPopulerItem()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun randomPopulerItem() {
        val index = menuItems.indices.toList().shuffled()
        val numItemShow= menuItems.size/2
        val subsetMenuItems = index.take(numItemShow).map { menuItems[it] }
        setPopularItemAdapter(subsetMenuItems)
    }

    private fun setPopularItemAdapter(subsetMenuItems: List<MenuItem>) {
        val adapter=MenuAdapter(subsetMenuItems,requireContext())

        binding.homeRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.homeRecyclerView.adapter=adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            database= FirebaseDatabase.getInstance()
            val offerRef =database.reference.child("Offer")
            offerRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    offerItems.clear()
                    for( offerSnapshot in snapshot.children){
                        val offerItem = offerSnapshot.getValue(Offers::class.java)
                        offerItem?.let {
                            offerItems.add(it)

                        }
                    }
                    for(x in offerItems){
                        imageLists.add(SlideModel(x.offerImage.toString(), ScaleTypes.FIT))
                    }
                    // binding.imageSlider.setImageList(imageLists)
                    binding.imageSlider.setImageList(imageLists, ScaleTypes.FIT)
                    binding.imageSlider.setItemClickListener(object :ItemClickListener{
                        override fun doubleClick(position: Int) {

                        }

                        override fun onItemSelected(position: Int) {
                            val intent2 =Intent(requireContext(),OfferDetailsActivity::class.java)
                            intent2.putExtra("Otitle",offerItems[position].offerTitle)
                            intent2.putExtra("Odesc",offerItems[position].offerDetails)
                            intent2.putExtra("Oimg",offerItems[position].offerImage)
                            startActivity(intent2)
                        }

                    })

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

    }
}



