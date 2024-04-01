package com.example.chefsoffoodmania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chefsoffoodmania.databinding.ActivityAdminProfileBinding
import com.example.chefsoffoodmania.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var databse: FirebaseDatabase
    private lateinit var adminReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databse = FirebaseDatabase.getInstance()
        adminReference = databse.reference.child("ChefUser")

        binding.name.isEnabled = false
        binding.email.isEnabled = false
        binding.address.isEnabled = false
        binding.phone.isEnabled = false
        binding.password.isEnabled = false

        binding.saveProfileBtn.isEnabled = false

        var isEnable = false
        binding.editProfile.setOnClickListener {
            isEnable = !isEnable

            binding.name.isEnabled = isEnable
            binding.email.isEnabled = isEnable
            binding.address.isEnabled = isEnable
            binding.phone.isEnabled = isEnable
            binding.password.isEnabled = isEnable

            if (isEnable) {
                binding.name.requestFocus()
            }
            binding.saveProfileBtn.isEnabled = isEnable

        }

        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.saveProfileBtn.setOnClickListener {
            updateUserData()
        }


        retriveUserData()
    }


    private fun retriveUserData() {
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            val userReference = adminReference.child(currentUserUid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var ownerName = snapshot.child("name").getValue()
                        var email = snapshot.child("email").getValue()
                        var address = snapshot.child("address").getValue()
                        var phone = snapshot.child("phone").getValue()
                        var password = snapshot.child("password").getValue()

                        setDatatoTextView(ownerName, email, address, phone, password)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

    }

    private fun setDatatoTextView(
        ownerName: Any?,
        email: Any?,
        address: Any?,
        phone: Any?,
        password: Any?
    ) {
        binding.name.setText(ownerName.toString())
        binding.email.setText(email.toString())
        binding.address.setText(address.toString())
        binding.phone.setText(phone.toString())
        binding.password.setText(password.toString())
    }


    private fun updateUserData() {
        var updateName = binding.name.text.toString()
        var updateEmail = binding.email.text.toString()
        var updateAddress = binding.address.text.toString()
        var updatePhone = binding.phone.text.toString()
        var updatePassword = binding.password.text.toString()

        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            val userReference = adminReference.child(currentUserUid)
            userReference.child("name").setValue(updateName)
            userReference.child("email").setValue(updateEmail)
            userReference.child("password").setValue(updatePassword)
            userReference.child("phone").setValue(updatePhone)
            userReference.child("address").setValue(updateAddress)


            Toast.makeText(this, "Profile is updated", Toast.LENGTH_SHORT).show()
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)
        } else {
            Toast.makeText(this, "Profile is not updated", Toast.LENGTH_SHORT).show()
        }
    }
}