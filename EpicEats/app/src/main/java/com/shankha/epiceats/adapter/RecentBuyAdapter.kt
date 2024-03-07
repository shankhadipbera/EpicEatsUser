package com.shankha.epiceats.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shankha.epiceats.databinding.RecentBuyItemBinding

class RecentBuyAdapter(
    private var context: Context,
    private var foodNameList: ArrayList<String>,
    private var foodImageList: ArrayList<String>,
    private var foodPriceList: ArrayList<String>,
    private var foodQuantityList: ArrayList<Int>

) : RecyclerView.Adapter<RecentBuyAdapter.RecentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
       return RecentViewHolder(RecentBuyItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return foodNameList.size
    }

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        holder.bind(position)
    }
    inner  class RecentViewHolder(private val binding :RecentBuyItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
                binding.apply {
                    recentBuyFoodname.text =foodNameList[position]
                    recentBuyPrice.text=foodPriceList[position]
                    recentBuyQuantity.text=foodQuantityList[position].toString()

                    var img= foodImageList[position]
                    var uri = Uri.parse(img)
                    Glide.with(context).load(uri).into(recentBuyFoodPic)
                }
        }

    }
}