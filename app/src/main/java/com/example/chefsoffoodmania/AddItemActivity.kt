package com.example.chefsoffoodmania

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.chefsoffoodmania.databinding.ActivityAddItemBinding
import com.example.chefsoffoodmania.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddItemActivity : AppCompatActivity() {

    //food item details
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodIngredient: String
    private var foodImageUri: Uri? = null

    //firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Initialize firebase
        auth = FirebaseAuth.getInstance()
        //firebase database instaance initiaization
        database = FirebaseDatabase.getInstance()

        //save item detail button
        binding.addItemBtn.setOnClickListener {
            //get all the data from each field except image
            foodName = binding.foodName.text.toString().trim()
            foodPrice = binding.foodPrice.text.toString().trim()
            foodDescription = binding.description.text.toString().trim()
            foodIngredient = binding.ingredient.text.toString().trim()

            //checking if none of the field is null
            if (!(foodName.isBlank() || foodPrice.isBlank() || foodDescription.isBlank() || foodIngredient.isBlank())) {
                uploadData()
                Toast.makeText(this, "New Item is Added!!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Fill All The Details..", Toast.LENGTH_SHORT).show()
            }
        }
        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }


    //to pick the image for image food
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                binding.selectedImage.setImageURI(uri)
                foodImageUri = uri
            }
        }

    // Function to upload data to Firebase database
    private fun uploadData() {
        /*This function uploads data to Firebase, including an image.
        It first gets a reference to the database's 'menu' node, generates a unique key,
        and checks if an image is selected. If an image is selected,
        it uploads it to Firebase Storage and retrieves the download URL.
        Then, it creates a new menu item object with the downloaded image
        URL and adds it to the database. Finally, it displays success or failure messages accordingly.
         */


        // Get reference to the 'menu' node in the database
        val menuRef = database.getReference("menu")
        // Generate a unique key for the menu item
        val newItemKey = menuRef.push().key

        // Check if foodImageUri is not null
        if (foodImageUri != null) {
            // Get reference to Firebase Storage
            val storageRef = FirebaseStorage.getInstance().reference
            // Create a reference for the food image with a unique filename
            val imageRef = storageRef.child("menu_images/${newItemKey}.jpg")
            // Upload the image file to Firebase Storage
            val uploadTask = imageRef.putFile(foodImageUri!!)

            // Handle successful upload
            uploadTask.addOnSuccessListener {
                // Retrieve download URL for the uploaded image
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    // Create a new menu item object with the downloaded image URL
                    val newItem = AllMenu(
                        newItemKey,
                        foodName = foodName,
                        foodPrice = foodPrice,
                        foodDescription = foodDescription,
                        foodIngredient = foodIngredient,
                        foodImage = downloadUrl.toString(),
                    )
                    // Add the new menu item to the 'menu' node in the database
                    newItemKey?.let { key ->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            // Display success message if data upload is successful
                            Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                            .addOnFailureListener {
                                // Display failure message if data upload fails
                                Toast.makeText(this, "Data Upload Failed..", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                }
                    .addOnFailureListener {
                        // Display failure message if image upload fails
                        Toast.makeText(this, "Image Upload Failed..", Toast.LENGTH_SHORT)
                            .show()
                    }

            }

        } else {
            // Display message if no image is selected
            Toast.makeText(this, "Please Select An Image", Toast.LENGTH_SHORT)
                .show()
        }
    }

}