package com.shankha.epiceats.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shankha.epiceats.R
import com.shankha.epiceats.databinding.FragmentUserBinding
import com.shankha.epiceats.model.UserModel

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private val auth: FirebaseAuth =FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentUserBinding.inflate(inflater,container,false)

        setUserData()

        binding.apply {
            name.isEnabled=false
            email.isEnabled=false
            address.isEnabled=false
            phoneNo.isEnabled=false
            saveBtn.isEnabled=false

        editBt.setOnClickListener {

            name.isEnabled = !name.isEnabled
           // email.isEnabled = !email.isEnabled
            address.isEnabled = !address.isEnabled
            phoneNo.isEnabled = !phoneNo.isEnabled
            saveBtn.isEnabled=!saveBtn.isEnabled
        }
        }

        binding.saveBtn.setOnClickListener {
            val Uname = binding.name.text.toString().trim()
            val Uemail = binding.email.text.toString().trim()
            val Uaddress = binding.address.text.toString().trim()
            val UphoneNo = binding.phoneNo.text.toString().trim()

            updateUserData(Uname,Uemail,Uaddress,UphoneNo)
            binding.apply {
                name.isEnabled=false
                email.isEnabled=false
                address.isEnabled=false
                phoneNo.isEnabled=false
                saveBtn.isEnabled=false}


        }

        return binding.root
    }

    private fun updateUserData(name: String, email: String, address: String, phoneNo: String) {
        val userId =auth.currentUser?.uid
        if(userId!=null){
            val userReference =database.getReference("Users").child(userId)
            val userData= hashMapOf(
                "userName" to name,
                "email" to email,
                "address" to address,
                "phoneNo" to phoneNo
            )
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(),"Profile Updated Successfully",Toast.LENGTH_SHORT).show()
               // auth.currentUser?.updateEmail(email)

            }.addOnFailureListener {
                Toast.makeText(requireContext(),"Failed to update Profile  \n Please try again",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setUserData() {
        val userId =auth.currentUser?.uid
        if(userId!=null){
            val userReference= database.getReference("Users").child(userId)
            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val userProfile =snapshot.getValue(UserModel::class.java)
                        if(userProfile!=null){
                            binding.name.setText(userProfile.userName)
                            binding.email.setText(userProfile.email)
                            binding.address.setText(userProfile.address)
                            binding.phoneNo.setText(userProfile.phoneNo)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }

}