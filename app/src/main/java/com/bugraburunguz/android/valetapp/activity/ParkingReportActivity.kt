package com.bugraburunguz.android.valetapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bugraburunguz.android.valetapp.R
import com.bugraburunguz.android.valetapp.adapter.ParkingReportAdapter
import com.bugraburunguz.android.valetapp.model.ReceiptEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ParkingReportActivity : AppCompatActivity(), ParkingReportAdapter.ListItemClickListener {

    lateinit var parkingReport: RecyclerView
    lateinit var mAuth: FirebaseAuth
    val listOfReceipts = ArrayList<ReceiptEntity>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_report)

        parkingReport = findViewById(R.id.parkingReportRecyclerView)

        val layoutManager = LinearLayoutManager(this)
        parkingReport.layoutManager = layoutManager

        val itemDecor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecor.setDrawable(this.resources.getDrawable(R.drawable.line_divider))
        parkingReport.addItemDecoration(itemDecor)

        val adapter = ParkingReportAdapter(listOfReceipts, this)
        parkingReport.adapter = adapter

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        val userId = user!!.uid
        Log.d("ProfileActivity", "userID: $userId")

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("parkingReceipt").child(userId)

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (child in dataSnapshot.children) {
                    val receipt = child.getValue(ReceiptEntity::class.java)
                    listOfReceipts.add(receipt!!)
                }
                updateDisplay()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun updateDisplay() {
        if (listOfReceipts.isNotEmpty()) {
            val adapter = ParkingReportAdapter(listOfReceipts, this)
            adapter.notifyDataSetChanged()
            parkingReport.adapter = adapter
        }
    }

    override fun onListItemClick(clickedItemIndex: Int) {
        val receipt = listOfReceipts[clickedItemIndex]
        val intent = Intent(this@ParkingReportActivity, ParkingReportDetailActivity::class.java)
        intent.putExtra("receipt", receipt)
        startActivity(intent)
    }
}
