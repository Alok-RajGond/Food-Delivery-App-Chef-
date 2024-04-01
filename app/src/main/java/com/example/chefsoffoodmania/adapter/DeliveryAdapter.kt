package com.example.chefsoffoodmania.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chefsoffoodmania.databinding.DeliveryItemBinding

class DeliveryAdapter(
    private val customerNames: MutableList<String>,
    private val paymentStatus: MutableList<Boolean>
) : RecyclerView.Adapter<DeliveryAdapter.DeliveryViewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewholder {
        val binding =
            DeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewholder(binding)
    }

    override fun onBindViewHolder(holder: DeliveryViewholder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size

    inner class DeliveryViewholder(private val binding: DeliveryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                //view id.text = variable[index]
                customerName.text = customerNames[position]
                if (paymentStatus[position] == true) {
                    paymentStatusReview.text = "Received"
                } else {
                    paymentStatusReview.text = "Not Received"
                }


                val colorMap = mapOf(
                    true to Color.GREEN,
                    false to Color.RED
                )

                paymentStatusReview.setTextColor(colorMap[paymentStatus[position]] ?: Color.BLACK)
                statusColor.backgroundTintList =
                    ColorStateList.valueOf(colorMap[paymentStatus[position]] ?: Color.BLACK)
            }

        }

    }
}