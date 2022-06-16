package com.bugraburunguz.android.valetapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bugraburunguz.android.valetapp.R
import com.bugraburunguz.android.valetapp.model.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ParkingActivity : AppCompatActivity(), View.OnClickListener {
    private var email: TextView? = null
    private var carPlate: EditText? = null
    private var amount: TextView? = null
    private var dateTime: TextView? = null
    private var carCompany: Spinner? = null
    private var carColor: Spinner? = null
    private var paymentMode: Spinner? = null
    private var spotNumber: Spinner? = null
    private var lotNumber: Spinner? = null
    private var parkingEtNoHours: EditText? = null
    private var submit: Button? = null
    private var mAuth: FirebaseAuth? = null

    private var strEmail = ""
    private var strCarPlate: String? = ""
    private var strAmount = ""
    private var strDateTime: String? = ""
    private var strCarCompany: String? = ""
    private var strCarColor = ""
    private var strPaymentMode: String? = ""
    private var strSpotNumber: String? = ""
    private var strLotNumber = ""
    private var strParkingNoOfHours: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        email = findViewById(R.id.parkingTxtEmailData)
        carPlate = findViewById(R.id.edtParkingCarNoData)
        amount = findViewById(R.id.parkingTxtAmountData)
        dateTime = findViewById(R.id.parkingTxtDateData)

        carCompany = findViewById(R.id.parkingSpinCarCompany)
        carColor = findViewById(R.id.parkingSpinCarColor)
        paymentMode = findViewById(R.id.parkingSpinPaymentMode)
        spotNumber = findViewById(R.id.parkingSpinSpot)
        lotNumber = findViewById(R.id.parkingSpinLot)
        submit = findViewById(R.id.parkingBtnSubmit)
        submit?.setOnClickListener(this)
        parkingEtNoHours = findViewById(R.id.parkingEtNoHours)

        val dateFormatDate: DateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.CANADA)
        val dateFormatTime: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.CANADA)
        val cal = Calendar.getInstance()
        val date = dateFormatDate.format(cal.time)
        val time = dateFormatTime.format(cal.time)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth?.currentUser
        val userId = user!!.uid

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("User").child(userId)

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user1 = dataSnapshot.getValue(
                    UserEntity::class.java
                )
                Log.d("ProfileActivity", user1!!.email.toString())
                email?.text = user1.email
                dateTime?.text = "$date $time"
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (!s.toString().equals("", ignoreCase = true)) {
                    val value = Integer.valueOf(s.toString())
                    val amountValue = value * 10
                    amount?.text = amountValue.toString()
                }
            }
        }

        parkingEtNoHours?.addTextChangedListener(textWatcher)


    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.parkingBtnSubmit -> {
                //Save data to database
                extractData()
                if (checkData()) {
                    //save to db
                    val user = mAuth!!.currentUser
                    val userId = user!!.uid
                    val database = FirebaseDatabase.getInstance()
                    val myRef = database.reference
                    val map = HashMap<String, String>()
                    map["carColor"] = strCarColor
                    map["carCompany"] = strCarCompany!!
                    map["carNo"] = strCarPlate!!
                    map["dateTime"] = strDateTime!!
                    map["email"] = strEmail
                    map["lotNo"] = strLotNumber
                    map["noOfHours"] = strParkingNoOfHours!!
                    map["paymentAmount"] = strAmount
                    map["paymentMethod"] = strPaymentMode!!
                    map["spotNo"] = strSpotNumber!!
                    myRef.child("parkingReceipt").child(userId).push().setValue(map)
                    Toast.makeText(
                        this@ParkingActivity,
                        "Parking reciept successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@ParkingActivity, ParkingReceiptActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Incomplete form", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
            }
        }
    }

    fun checkData(): Boolean {
        var isDataTrue = true
        if (parkingEtNoHours!!.text.toString() == "") {
            isDataTrue = false
        }
        return isDataTrue
    }

    fun extractData() {
        strEmail = email!!.text.toString()
        strCarPlate = carPlate!!.text.toString()
        strCarCompany = carCompany!!.selectedItem.toString()
        strCarColor = carColor!!.selectedItem.toString()
        strParkingNoOfHours = parkingEtNoHours!!.text.toString()
        strAmount = amount!!.text.toString()
        strDateTime = dateTime!!.text.toString()
        strPaymentMode = paymentMode!!.selectedItem.toString()
        strLotNumber = lotNumber!!.selectedItem.toString()
        strSpotNumber = spotNumber!!.selectedItem.toString()
    }
}
