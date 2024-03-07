package com.shankha.epiceats.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shankha.epiceats.databinding.NotificationItemBinding

class NotificationAdapter(private val notification :ArrayList<String>,private val notificationImage : ArrayList<Int>):RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(NotificationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return notification.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
       holder.bind(position)
    }

   inner class NotificationViewHolder(private val binding: NotificationItemBinding):RecyclerView.ViewHolder(binding.root) {
       fun bind(position: Int) {
           binding.notificationImage.setImageResource(notificationImage[position])
           binding.notificationText.text=notification[position]

       }

   }
}