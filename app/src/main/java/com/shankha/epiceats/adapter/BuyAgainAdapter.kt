package com.shankha.epiceats.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shankha.epiceats.FeedbackActivity
import com.shankha.epiceats.R
import com.shankha.epiceats.databinding.BuyAgainItemBinding
import com.shankha.epiceats.model.OrderDetails
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BuyAgainAdapter(
    private var listOfDeliveryItem: ArrayList<OrderDetails>,
    private val requireContext: Context
) :RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        return BuyAgainViewHolder(BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int { return listOfDeliveryItem.size}


    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        //holder.bind(buyAgainFoodNames[position],buyAgainFoodPrices[position],buyAgainFoodPics[position],buyAgainFoodDates[position],holder.itemView.context)
        holder.bind(position)
    }

  inner  class BuyAgainViewHolder(private val binding: BuyAgainItemBinding):RecyclerView.ViewHolder(binding.root) {


      fun bind(position: Int) {
          binding.buyAgainFoodName.text = listOfDeliveryItem[position].foodNames.toString()                                               //buyAgainFoodNames[position]
          binding.buyAgainFoodName.isSelected = true
          binding.buyAgainFoodPrice.text = listOfDeliveryItem[position].foodPrice.toString()
          binding.buyAgainFoodPrice.isSelected = true
          binding.feedBackBtn.setOnClickListener {
              val intent =Intent(requireContext,FeedbackActivity::class.java)
              intent.putExtra("OrderDetails",listOfDeliveryItem[position])
              requireContext.startActivity(intent)

          }

          val firstLink = extractFirstLink(listOfDeliveryItem[position].foodImage.toString() )

          if (!firstLink.isNullOrBlank()) {
              val uri = Uri.parse(firstLink)
              Glide.with(itemView.context)
                  .load(uri)
                  .placeholder(R.drawable.ic_launcher_background)
                  .error(R.drawable.sademoji)
                  .into(binding.buyAgainFoodImage)
          } else {
              binding.buyAgainFoodImage.setImageResource(R.drawable.sademoji)
          }

          val date = Date(listOfDeliveryItem[position].currentTime )
          val dateFormat = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault())
          val formattedDate = dateFormat.format(date)
          binding.dateTV.text = formattedDate
      }
      private fun extractFirstLink(linksString: String): String? {
          val trimmedString = linksString.trim()
          val trimmedWithoutBrackets = trimmedString.removeSurrounding("[", "]")
          val links = trimmedWithoutBrackets.split(", ")
          return links.firstOrNull()
      }


    }
}