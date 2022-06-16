package com.bugraburunguz.android.valetapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bugraburunguz.android.valetapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class GoogleSignInExtraActivity : AppCompatActivity(), View.OnClickListener {
    private var phone: EditText = findViewById(R.id.registerExtraEtPhone)
    private var city: Spinner = findViewById(R.id.registerExtraSpinnerCity)
    private var submit: Button = findViewById(R.id.registerExtraBtnRegister)
    private var strPhone: String? = null
    private var strCity: String? = null
    private var mAuth: FirebaseAuth? = null
    private var name: String? = ""
    private var email: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_sign_in_extra)

        val intent = intent

        name = intent.getStringExtra("name")
        email = intent.getStringExtra("email")
        submit.setOnClickListener(this)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.registerExtraBtnRegister -> {
                extractData()
                if (veriftData()) {
                    //Data is valid
                    val user = mAuth!!.currentUser
                    val userId = user!!.uid
                    val dateFormatDate: DateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
                    val dateFormatTime: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
                    val cal = Calendar.getInstance()
                    val date = dateFormatDate.format(cal.time)
                    val time = dateFormatTime.format(cal.time)
                    val database = FirebaseDatabase.getInstance()
                    val myRef = database.reference
                    val map = HashMap<String, String?>()
                    map["city"] = strCity
                    map["email"] = email
                    map["lastLoginDate"] = date
                    map["lastLoginTime"] = time
                    map["name"] = name
                    map["number"] = strPhone
                    myRef.child("User").child(userId).setValue(map)
                    Toast.makeText(
                        this@GoogleSignInExtraActivity,
                        "Registration successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@GoogleSignInExtraActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun extractData() {
        strPhone = phone.text.toString()
        strCity = city.selectedItem.toString()
    }

    private fun veriftData(): Boolean {
        var isDataTrue = true
        if (strCity!!.isEmpty()|| strPhone!!.isEmpty()) {
            isDataTrue = false
        }
        return isDataTrue
    }
}
