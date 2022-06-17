package com.bugraburunguz.android.valetapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bugraburunguz.android.valetapp.R
import com.bugraburunguz.android.valetapp.model.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var name: TextView
    private lateinit var email: TextView
    private lateinit var contactNumber: TextView
    private lateinit var city: TextView
    private lateinit var edit: Button
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        name = findViewById(R.id.userProfileTvNameData)
        email = findViewById(R.id.userProfileTvEmailData)
        contactNumber = findViewById(R.id.userProfileTvContactNumberData)
        city = findViewById(R.id.userProfileTvCityData)
        edit = findViewById(R.id.profileBtnEdit)
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        val userId = user!!.uid
        Log.d("ProfileActivity", "userID: $userId")
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("User").child(userId)
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user1 = dataSnapshot.getValue(
                    UserEntity::class.java
                )
                Log.d("ProfileActivity", user1!!.email)
                name.text = user1.name
                email.text = user1.email
                contactNumber.text = user1.number
                city.text = user1.city
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        edit.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.profileBtnEdit -> {
                val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
                intent.putExtra("name", name.text.toString())
                intent.putExtra("email", email.text.toString())
                intent.putExtra("contactNumber", contactNumber.text.toString())
                intent.putExtra("city", city.text.toString())
                startActivity(intent)
            }
        }
    }
}
