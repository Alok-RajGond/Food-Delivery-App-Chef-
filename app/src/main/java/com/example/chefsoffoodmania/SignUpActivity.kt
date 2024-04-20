package com.example.chefsoffoodmania


import android.R.layout.simple_list_item_1
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chefsoffoodmania.databinding.ActivitySignUpBinding
import com.example.chefsoffoodmania.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var nameOfRestaurant: String
    private lateinit var database: DatabaseReference

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Initialize firebase authentication
        auth = Firebase.auth

        //initialize Firebase Database
        database = Firebase.database.reference

        // for signUp Button
        binding.signUpBtn.setOnClickListener {
            //binding all the info of the user if he click signUp button after the fillup the details
            userName = binding.userName.text.toString().trim()
            nameOfRestaurant = binding.restaurantName.text.toString().trim()
            email = binding.emailOrPhone.text.toString().trim()
            password = binding.password.text.toString().trim()

            //warning if any of the above field is empty by user
            if (userName.isBlank() || nameOfRestaurant.isBlank() || email.isBlank() || password.isBlank()){
                Toast.makeText(this, "Please Fill All The Details", Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email, password)
            }
        }





        binding.alreadyHaveAcc.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }

    /*
    //To create the account After clicking the button we took the email and password to create account
    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(this, "Account is Created, Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                //after successful account creation move to login page to varify the user
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this, "Try Again\nAccount Creation Failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure", task.exception)
            }
        }
    }
//saving data into databse
    private fun saveUserData() {
        userName = binding.userName.text.toString().trim()
        nameOfRestaurant = binding.restaurantName.text.toString().trim()
        email = binding.emailOrPhone.text.toString().trim()
        password = binding.password.text.toString().trim()

        //with this we an store the data of the user . Here we creating a class(databse file) called UserModel where we can store the data
        val user = UserModel(userName, nameOfRestaurant, email, password)

        //here we creating user Id where each data is store for each user ( userId is just like primary key)
        val userId: String  = FirebaseAuth.getInstance().currentUser!!.uid
        //save user data into firebase database
        database.child("Chef user").child(userId).setValue(user)
    }*/


    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "SIGN UP IS COMPLETE!!", Toast.LENGTH_SHORT).show()
                saveUserData()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "SIGN UP IS failed!!", Toast.LENGTH_SHORT).show()
                Log.d("Account", "create Account: Failure", task.exception)
            }
        }
    }

    private fun saveUserData() {
        //retrive data from input filed and store in database
        userName = binding.userName.text.toString().trim()
        nameOfRestaurant = binding.restaurantName.text.toString().trim()
        email = binding.emailOrPhone.text.toString().trim()
        password = binding.password.text.toString().trim()

        val user = UserModel(userName, nameOfRestaurant, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        //here we save data in firebase database
        database.child("ChefUser").child(userId).setValue(user)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}