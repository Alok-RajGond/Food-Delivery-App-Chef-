 package com.example.chefsoffoodmania

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.chefsoffoodmania.databinding.ActivityMainBinding
import com.example.chefsoffoodmania.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

 class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

     private lateinit var auth: FirebaseAuth
     private lateinit var database: FirebaseDatabase
     private lateinit var completedOrderReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.addMenu.setOnClickListener{
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }

        binding.allItemMenu.setOnClickListener{
            val intent = Intent(this, AllItemActivity::class.java)
            startActivity(intent)
        }

        binding.outForDeliveryButton.setOnClickListener {
            val intent = Intent(this, OutForDeliveryActivity::class.java)
            startActivity(intent)
        }

        binding.profile.setOnClickListener {
            val intent = Intent(this, AdminProfileActivity::class.java)
            startActivity(intent)
        }

        binding.createUser.setOnClickListener {
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }
        binding.pendingOrderTextView.setOnClickListener {
            val intent = Intent(this, PendingOrderActivity::class.java)
            startActivity(intent)
        }
        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        pendingOrders()

        completedOrders()

        wholeTimeEarning()
    }

     private fun wholeTimeEarning() {
         val listOfTotalPayment = mutableListOf<Int>()
         completedOrderReference = FirebaseDatabase.getInstance().reference.child("CompletedOrder")
         completedOrderReference.addListenerForSingleValueEvent(object: ValueEventListener{
             override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children){
                    var completedOrder = orderSnapshot.getValue(OrderDetails::class.java)

                    completedOrder?.totalPrice?.replace("₹", "")?.toIntOrNull()?.let { i -> listOfTotalPayment.add(i) }
                }
                 binding.totalEarning.text ="₹" + listOfTotalPayment.sum().toString()
             }

             override fun onCancelled(error: DatabaseError) {

             }

         })
     }

     private fun completedOrders() {

         val completedOrderReference =
             database.reference.child("CompletedOrder")
         var completedOrderItemCount = 0
         completedOrderReference.addListenerForSingleValueEvent(object: ValueEventListener{
             override fun onDataChange(snapshot: DataSnapshot) {
                 completedOrderItemCount = snapshot.childrenCount.toInt()
                 binding.completedOrder.text = completedOrderItemCount.toString()
             }

             override fun onCancelled(error: DatabaseError) {

             }

         })
     }

     private fun pendingOrders() {
         database = FirebaseDatabase.getInstance()
         val pendingOrderReference = database.reference.child("OrderDetails")
         var pendingOrderItemCount = 0
         pendingOrderReference.addListenerForSingleValueEvent(object: ValueEventListener{
             override fun onDataChange(snapshot: DataSnapshot) {
                 pendingOrderItemCount = snapshot.childrenCount.toInt()
                 binding.pendingOrder.text = pendingOrderItemCount.toString()
             }

             override fun onCancelled(error: DatabaseError) {

             }

         })
     }
 }