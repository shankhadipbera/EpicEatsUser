package com.shankha.epiceats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shankha.epiceats.adapter.NotificationAdapter
import com.shankha.epiceats.databinding.FragmentNotificationBottomBinding

class NotificationBottomFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBottomBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentNotificationBottomBinding.inflate(inflater,container,false)

        val notification= listOf("asdfghjkl","zxcvbnm","qwertyuiop")
        val notificationImage=listOf(R.drawable.oplaced, R.drawable.facebook_round, R.drawable.sademoji)
        val adapter=NotificationAdapter(ArrayList(notification),ArrayList(notificationImage))
        binding.notificationRecycleView.layoutManager=LinearLayoutManager(requireContext())
        binding.notificationRecycleView.adapter=adapter



        return binding.root
    }

    companion object {

    }
}