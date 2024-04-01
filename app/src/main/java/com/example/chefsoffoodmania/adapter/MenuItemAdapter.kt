package com.example.chefsoffoodmania.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chefsoffoodmania.databinding.ItemItemBinding
import com.example.chefsoffoodmania.model.AllMenu
import com.google.firebase.database.DatabaseReference

class MenuItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListner: (position: Int) -> Unit
): RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>() {
    private val itemQuantity = IntArray(menuList.size){1}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AddItemViewHolder(binding)
    }

    override fun getItemCount(): Int = menuList.size

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }


    inner class AddItemViewHolder(private val binding: ItemItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {

                val quantity = itemQuantity[position]
                val menuItem = menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)

                foodNameTextView.text = menuItem.foodName
                priceTextView.text = menuItem.foodPrice

                //using glide to extract image from the database
                Glide.with(context).load(uri).into(foodImageView)

                quanityTextView.text = quantity.toString()

                minusButton.setOnClickListener {
                    decreaseQuantity(position)
                }
                plusBtn.setOnClickListener {
                    increaseQuantity(position)
                }
                deleteButton.setOnClickListener {
                    onDeleteClickListner(position)
                }
            }

        }
        private fun decreaseQuantity(position: Int) {
            if (itemQuantity[position] > 0){
                itemQuantity[position]--
                binding.quanityTextView.text = itemQuantity[position].toString()
            }

        }
        private fun increaseQuantity(position: Int) {
            if (itemQuantity[position] < 10){
                itemQuantity[position]++
                binding.quanityTextView.text = itemQuantity[position].toString()
            }
        }
        private fun deleteQuantity(position: Int) {
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuList.size)

        }


    }


}