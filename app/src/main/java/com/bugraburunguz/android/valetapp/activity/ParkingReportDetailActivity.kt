package com.bugraburunguz.android.valetapp.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bugraburunguz.android.valetapp.R
import com.bugraburunguz.android.valetapp.model.ReceiptEntity

class ParkingReportDetailActivity : AppCompatActivity() {
    private var txtViewEmail: TextView? = null
    private var textViewCarPlate: TextView? = null
    private var txtViewCompany: TextView? = null
    private var txtViewColor: TextView? = null
    private var txtViewHours: TextView? = null
    private var txtViewDateTime: TextView? = null
    private var txtViewLot: TextView? = null
    private var txtViewSpot: TextView? = null
    private var txtViewPayment: TextView? = null
    private var txtViewAmount: TextView? = null
    private var receiptEntity: ReceiptEntity? = ReceiptEntity()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_report_detail)

        txtViewEmail = findViewById(R.id.txtVEmail)
        textViewCarPlate = findViewById(R.id.txtViewCarPlate)
        txtViewCompany = findViewById(R.id.textViewCompany)
        txtViewColor = findViewById(R.id.textViewColor)
        txtViewHours = findViewById(R.id.textViewHours)
        txtViewDateTime = findViewById(R.id.textViewDate)
        txtViewLot = findViewById(R.id.textViewLot)
        txtViewSpot = findViewById(R.id.textViewSpot)
        txtViewPayment = findViewById(R.id.textViewPaymentMode)
        txtViewAmount = findViewById(R.id.textViewAmount)
        val intent = intent
        receiptEntity = intent.getSerializableExtra("receipt") as ReceiptEntity
        txtViewEmail?.text = receiptEntity!!.email
        textViewCarPlate?.text = receiptEntity!!.carNo
        txtViewCompany?.text = receiptEntity!!.carCompany
        txtViewColor?.text = receiptEntity!!.carColor
        txtViewHours?.text = receiptEntity!!.noOfHours
        txtViewDateTime?.text = receiptEntity!!.dateTime
        txtViewLot?.text = receiptEntity!!.lotNo
        txtViewSpot?.text = receiptEntity!!.spotNo
        txtViewPayment?.text = receiptEntity!!.paymentMethod
        txtViewAmount?.text = receiptEntity!!.paymentAmount
    }
}
