package com.example.scholateacher

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.scholateacher.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        var user = auth.currentUser

        if(user!=null){
            startActivity(Intent(this,MainActivity::class.java))
        }





        binding.loginbtn.setOnClickListener{

            var id = binding.editId.text.toString()
            var pass = binding.editPassword.text.toString()

            if(id.isEmpty() || pass.isEmpty()){
                Toast.makeText(this,"Please enter ID  and Password",Toast.LENGTH_SHORT).show()
            }else{
                var email = id+"@gmail.com"
                signIn(email,pass)
            }

        }









    }

    private fun signIn(email: String, pass: String) {
     auth.signInWithEmailAndPassword(email,pass)
         .addOnSuccessListener {
             Toast.makeText(this,"Login Successfull",Toast.LENGTH_SHORT).show()
             startActivity(Intent(this,MainActivity::class.java))

         }
         .addOnFailureListener{ error->
             Toast.makeText(this,"Error: ${error.message}",Toast.LENGTH_SHORT).show()
         }
    }
}