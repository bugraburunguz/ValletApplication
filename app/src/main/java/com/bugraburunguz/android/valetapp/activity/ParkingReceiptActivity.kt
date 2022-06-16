package com.bugraburunguz.android.valetapp.activity

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bugraburunguz.android.valetapp.R
import com.bugraburunguz.android.valetapp.model.ReceiptEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ParkingReceiptActivity : AppCompatActivity() {
    private lateinit var txtViewEmail: TextView
    private lateinit var txtViewCarPlate: TextView
    private lateinit var txtViewCompany: TextView
    private lateinit var txtViewColor: TextView
    private lateinit var txtViewHours: TextView
    private lateinit var txtViewDateTime: TextView
    private lateinit var txtViewLot: TextView
    private lateinit var txtViewSpot: TextView
    private lateinit var txtViewPayment: TextView
    private lateinit var txtViewAmount: TextView
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_receipt)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        txtViewEmail = findViewById(R.id.txtVEmail)
        txtViewCarPlate = findViewById(R.id.textViewCarPlate)
        txtViewCompany = findViewById(R.id.textViewCompany)
        txtViewColor = findViewById(R.id.textViewColor)
        txtViewHours = findViewById(R.id.textViewHours)
        txtViewDateTime = findViewById(R.id.textViewDate)
        txtViewLot = findViewById(R.id.textViewLot)
        txtViewSpot = findViewById(R.id.textViewSpot)
        txtViewPayment = findViewById(R.id.textViewPaymentMode)
        txtViewAmount = findViewById(R.id.textViewAmount)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        val userId = user!!.uid
        Log.d("ProfileActivity", "userID: $userId")

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("parkingReceipt").child(userId)

        myRef.orderByKey().limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (child in dataSnapshot.children) {
                        val receipt = child.getValue(ReceiptEntity::class.java)
                        txtViewEmail.text = receipt!!.email
                        txtViewCarPlate.text = receipt.carNo
                        txtViewCompany.text = receipt.carCompany
                        txtViewColor.text = receipt.carColor
                        txtViewHours.text = receipt.noOfHours
                        txtViewDateTime.text = receipt.dateTime
                        txtViewLot.text = receipt.lotNo
                        txtViewSpot.text = receipt.spotNo
                        txtViewPayment.text = receipt.paymentMethod
                        txtViewAmount.text = receipt.paymentAmount
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }
}