package com.example.chefsoffoodmania

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.chefsoffoodmania.databinding.ActivityLoginBinding
import com.example.chefsoffoodmania.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {

    private var userName: String? = null
    private var nameOfRestaurant: String? = null
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //by this app show pop-up to sign in with google
        //creating google sign in option variable
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        //Initialize firebase authentication
        auth = Firebase.auth

        //initialize Firebase Database
        database = Firebase.database.reference

        //initialize google signin
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)


        //for login Button
        binding.loginBtn.setOnClickListener {

            //geting text from edit text
            email = binding.emailInput.text.toString().trim()
            password = binding.passInput.text.toString().trim()

            //checking if any of the filled is empty
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please Fill All The Details", Toast.LENGTH_SHORT).show()
            } else {
                /* IMPORTANT:: ❗ ❗  if the have no accountand try to login with funciton
                 that crated below it can automatically create the account*/
                createUserAccount(email, password)
            }
        }

        //for Google loginBtn
        binding.googleLoginBtn.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }

        //for don't have an acc. text
        binding.dontHaveAcc.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

        }
    }

    private fun createUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //here we checking that current iuser is exist then send user to the main Activity
                val user: FirebaseUser? = auth.currentUser
                Toast.makeText(this, "Login Succesful", Toast.LENGTH_SHORT).show()
                updateUi(user)
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser? = auth.currentUser
                        Toast.makeText(
                            this,
                            "New User Created And\nSuccessful login",
                            Toast.LENGTH_SHORT
                        ).show()
                        saveUserData(email, password)
                        updateUi(user)
                    } else {
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                        Log.d("Account", "createUserAccount: Authentication Failed", task.exception)
                    }
                }
            }
        }
    }

    // if user is new this function helps to save the user data
    private fun saveUserData(email: String, password: String) {
        this.email = binding.emailInput.text.toString().trim()
        this.password = binding.passInput.text.toString().trim()
        //to give the path like primary key
        val user = UserModel(userName, nameOfRestaurant, email, password)
        val userId: String? = FirebaseAuth.getInstance().currentUser?.uid
        //to save the data in database
        userId?.let {
            database.child("ChefUser").child(it).setValue(user)
        }
    }



    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // Check if the activity result is RESULT_OK
            if (result.resultCode == Activity.RESULT_OK) {
                // Get the signed-in account from the intent
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    // Get the GoogleSignInAccount
                    val account: GoogleSignInAccount = task.result
                    // Get the credential using the GoogleSignInAccount's idToken
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    // Sign in with the credential
                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        // Successful sign-in process with google
                        if (authTask.isSuccessful) {
                            Toast.makeText(this, "Login Succesful using google", Toast.LENGTH_SHORT)
                                .show()
                            updateUi(authTask.result?.user)
                        } else {
                            Toast.makeText(this, "Login Failed using google", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Login Failed using google", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                // Handle the case where the activity result is not RESULT_OK
                Toast.makeText(this, "Activity not completed successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    //this function is use to move to the main Activity after successful login
    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}