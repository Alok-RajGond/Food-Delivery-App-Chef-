package com.example.chefsoffoodmania

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chefsoffoodmania.adapter.OrderDetailsAdapter
import com.example.chefsoffoodmania.databinding.ActivityOrderDetailsBinding
import com.example.chefsoffoodmania.model.OrderDetails

class OrderDetailsActivity : AppCompatActivity() {
    private val binding: ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }
    private var userName: String? = null
    private var phoneNumber: String? = null
    private var address: String? = null
    private var totalPrice: String? = null
    private var foodNames: ArrayList<String> = arrayListOf()
    private var foodPrice: ArrayList<String> = arrayListOf()
    private var foodQuantities: ArrayList<Int> = arrayListOf()
    private var foodImage: ArrayList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }
        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        val recievedOrderDetails = intent.getSerializableExtra("userOrderDetails") as OrderDetails
        recievedOrderDetails?.let {
            userName = recievedOrderDetails?.userName
            foodNames = recievedOrderDetails?.foodNames as ArrayList<String>
            foodImage = recievedOrderDetails.foodImages as ArrayList<String>
            foodPrice = recievedOrderDetails.foodPrices as ArrayList<String>
            foodQuantities = recievedOrderDetails.foodQuantities as ArrayList<Int>
            address = recievedOrderDetails.address
            phoneNumber = recievedOrderDetails.phoneNumber
            totalPrice = recievedOrderDetails.totalPrice

            setUserDetails()
            setAdapter()
        }


    }

    private fun setAdapter() {
        binding.orderDetailRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailsAdapter(this, foodNames, foodImage, foodPrice, foodQuantities)
        binding.orderDetailRecyclerView.adapter = adapter
    }

    private fun setUserDetails() {
        binding.editTextName.text = userName
        binding.editTextAddress.text = address
        binding.editTextPhoneNumber.text = phoneNumber
        binding.totalAmount.text = totalPrice

    }

}