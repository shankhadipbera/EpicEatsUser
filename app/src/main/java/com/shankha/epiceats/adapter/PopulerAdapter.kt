package com.shankha.epiceats.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shankha.epiceats.DetailsActivity
import com.shankha.epiceats.databinding.PopulerItemBinding

class PopulerAdapter(private val items:List<String>,private val prices:List<String>,private val images:List<Int>,private val requireContext: Context) :
    RecyclerView.Adapter<PopulerAdapter.PopulerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopulerViewHolder {
        return PopulerViewHolder(PopulerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return  items.size
    }

    override fun onBindViewHolder(holder: PopulerViewHolder, position: Int) {
       val item =items[position]
        val price=prices[position]
        val image= images[position]
        holder.bind(item,price,image)

        holder.itemView.setOnClickListener{
            val intent = Intent(requireContext, DetailsActivity::class.java)
            intent.putExtra("MenuItemFoodName",item)
            intent.putExtra("MenuItemFoodPic",image)
            requireContext.startActivity(intent)
        }
    }

  inner  class PopulerViewHolder( private  val binding : PopulerItemBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, price: String, image: Int) {
                binding.populerFoodName.text=item
            binding.populerFoodName.isSelected = true
            binding.populerFoodPrice.text=price
            binding.populerFoodPic.setImageResource(image)
        }

    }
}