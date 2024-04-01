package com.example.chefsoffoodmania

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chefsoffoodmania.adapter.DeliveryAdapter
import com.example.chefsoffoodmania.databinding.ActivityOutForDeliveryBinding
import com.example.chefsoffoodmania.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OutForDeliveryActivity : AppCompatActivity() {

    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private var listOfCompleteOrder: ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

retriveCompleteOrderDetails()


        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun retriveCompleteOrderDetails() {
        database = FirebaseDatabase.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val completeOrderReference = database.reference.child("CompletedOrder")
            .orderByChild("currentTime")
        completeOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfCompleteOrder.clear()

                for (orderSnapshot in snapshot.children){
                    val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfCompleteOrder.add(it)
                    }
                }
                listOfCompleteOrder.reverse()

                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun setDataIntoRecyclerView() {
        val customerName = mutableListOf<String>()
        val paymentStatus = mutableListOf<Boolean>()

        for (order in listOfCompleteOrder){
            order.userName?.let {
                customerName.add(it)
            }
            paymentStatus.add(order.paymentRecieved)
        }

        val adapter = DeliveryAdapter(customerName, paymentStatus)
        binding.deliveryRecyclerView.adapter = adapter
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)


    }

}