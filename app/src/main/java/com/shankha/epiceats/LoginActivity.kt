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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.shankha.epiceats.databinding.ActivityLoginBinding
import com.shankha.epiceats.model.UserModel

class LoginActivity : AppCompatActivity() {
    private lateinit var  auth: FirebaseAuth
    private lateinit var  database: DatabaseReference
    private lateinit var  email:String
    private lateinit var  password :String
    private lateinit var  googleSignInClient : GoogleSignInClient
    private val binding : ActivityLoginBinding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        auth= Firebase.auth
        database= Firebase.database.reference

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions )

        binding.loginGoogle.setOnClickListener {
            val signInIntent= googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        binding.login.setOnClickListener {
            email=binding.loginEmail.text.toString().trim()
            password=binding.loginPassword.text.toString().trim()
            if(email.isEmpty()|| password.isEmpty()){
                Toast.makeText(this,"Please Fill All The Details", Toast.LENGTH_SHORT).show()
            }else{

                createUser()

            }
        }

        binding.dontHaveAccount.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }
    }

    private fun createUser() {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if(task.isSuccessful){
                val user = auth.currentUser
                updateUi(user)
                Toast.makeText(this,"Login Successful", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this,MainActivity::class.java))
        finish()

    }

    private val launcher =registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if(result.resultCode== Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account : GoogleSignInAccount? =task.result
                val credential= GoogleAuthProvider.getCredential(account?.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener { task->
                    if(task.isSuccessful){
                        val currentUser=auth.currentUser
                        val user= UserModel(currentUser?.displayName,currentUser?.email,null)
                        database.child("Users").child(currentUser!!.uid).setValue(user)

                        Toast.makeText(this,"Google SignIn Successful 😊", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this,"Google SignIn Failed", Toast.LENGTH_SHORT).show()
                    }

                }

            }else{
                Toast.makeText(this,"Google SignIn Failed", Toast.LENGTH_SHORT).show()}
        }else{
            Toast.makeText(this,"Google authentication Failed", Toast.LENGTH_SHORT).show()}

    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}