package com.shankha.epiceats

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shankha.epiceats.databinding.ActivitySignUpBinding
import com.shankha.epiceats.model.UserModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var  auth:FirebaseAuth
    private lateinit var  database:DatabaseReference
    private lateinit var  userName: String
    private lateinit var  email:String
    private lateinit var  password :String
    private lateinit var  googleSignInClient : GoogleSignInClient

    private val binding :ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        auth=Firebase.auth
        database=Firebase.database.reference

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions )

        binding.signupSignup.setOnClickListener {
            userName=binding.signupName.text.toString().trim()
            email= binding.signupEmail.text.toString().trim()
            password=binding.signupPassword.text.toString().trim()
            if(userName.isBlank()||email.isBlank()||password.isBlank()){
                Toast.makeText(this,"Please fill all the details",Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password)
            }
        }

        binding.signupGoogle.setOnClickListener {
            val signInIntent= googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        binding.alredyHaveAccount.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }


    }

    private val launcher =registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if(result.resultCode==Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account : GoogleSignInAccount? =task.result
                val credential=GoogleAuthProvider.getCredential(account?.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener { task->
                    if(task.isSuccessful){
                        val currentUser=auth.currentUser
                        val user= UserModel(currentUser?.displayName,currentUser?.email,null)
                        database.child("Users").child(currentUser!!.uid).setValue(user)

                        Toast.makeText(this,"Google SignIn Successful 😊",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,LoginActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this,"Google SignIn Failed",Toast.LENGTH_SHORT).show()
                    }

                }

            }else{Toast.makeText(this,"Google SignIn Failed",Toast.LENGTH_SHORT).show()}
        }else{Toast.makeText(this,"Google authentication Failed",Toast.LENGTH_SHORT).show()}

    }

    private fun createAccount(email: String, password: String) {
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {task->
               if(task.isSuccessful){
                   Toast.makeText(this,"Account Created Successfully",Toast.LENGTH_SHORT).show()
                   saveUserData()
                   startActivity(Intent(this,LoginActivity::class.java))
                   finish()
               }else{
                   Toast.makeText(this,"Account Creation Failed",Toast.LENGTH_SHORT).show()
               }
            }
    }

    private fun saveUserData() {
        userName=binding.signupName.text.toString().trim()
        email= binding.signupEmail.text.toString().trim()
        password=binding.signupPassword.text.toString().trim()
        val user=UserModel(userName,email,password)
        val userId=FirebaseAuth.getInstance().currentUser!!.uid
        database.child("Users").child(userId).setValue(user)
    }
}