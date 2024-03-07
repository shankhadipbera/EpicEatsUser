package com.shankha.epiceats

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shankha.epiceats.databinding.ActivityFeedbackBinding
import com.shankha.epiceats.databinding.ActivityLoginBinding
import com.shankha.epiceats.model.Feedback
import com.shankha.epiceats.model.OrderDetails
import com.shankha.epiceats.model.UserModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FeedbackActivity : AppCompatActivity() {

    private val binding: ActivityFeedbackBinding by lazy {
        ActivityFeedbackBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBck.setOnClickListener {
            finish()
        }

        val feedBackOrder = intent.getSerializableExtra("OrderDetails") as OrderDetails
        binding.feedbackUserName.text = feedBackOrder.userName
        binding.feedBackOrderId.text = feedBackOrder.itemPushKey
        val date = Date(feedBackOrder.currentTime)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        binding.feedBackOrderDate.text = formattedDate
        auth = Firebase.auth
        userId = auth.currentUser?.uid ?: ""
        database = FirebaseDatabase.getInstance()

        if (userId != null) {
            val feedBackRef =
                database.reference.child("Feedback").child(feedBackOrder.itemPushKey!!)
            feedBackRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val feedbackProfile = snapshot.getValue(Feedback::class.java)
                        if (feedbackProfile != null) {
                            binding.feedbackText.setText(feedbackProfile.feedbackText)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

            binding.submitButton.setOnClickListener {
                val feedbackTexts = binding.feedbackText.text.toString()
                if (!feedbackTexts.isEmpty()) {
                    val feedback = Feedback(
                        feedBackOrder.userName,
                        binding.feedBackOrderDate.text.toString(),
                        feedBackOrder.itemPushKey,
                        feedbackTexts
                    )
                    database.reference.child("Feedback").child(feedBackOrder.itemPushKey!!)
                        .setValue(feedback).addOnSuccessListener {
                        Toast.makeText(this, "Feedback Uploded Sucessfully", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Failed to Uploded Feedback \n Please Try Again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }


        }
    }
}