package com.bugraburunguz.android.valetapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var btnNewRegister: Button
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var city: Spinner
    private lateinit var strName: String
    private lateinit var strEmail: String
    private lateinit var strPassword: String
    private lateinit var strPhoneNumber: String
    private lateinit var strCity: String
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnNewRegister = findViewById(R.id.registerBtnRegister)
        name = findViewById(R.id.registerEtName)
        email = findViewById(R.id.registerEtEmail)
        password = findViewById(R.id.registerEtPassword)
        phoneNumber = findViewById(R.id.registerEtPhone)
        city = findViewById(R.id.registerSpinnerCity)
        mAuth = FirebaseAuth.getInstance()
        btnNewRegister.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.registerBtnRegister -> {
                extractData()
                if (verifyData()) {
                    mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                        .addOnCompleteListener(
                            this
                        ) { task ->
                            if (task.isSuccessful) {
                                Log.d(
                                    TAG,
                                    "createUserWithEmail:success"
                                )
                                val user = mAuth.currentUser
                                val userId = user!!.uid
                                val dateFormatDate: DateFormat =
                                    SimpleDateFormat("dd-MM-yyyy", Locale.CANADA)
                                val dateFormatTime: DateFormat =
                                    SimpleDateFormat("HH:mm:ss", Locale.CANADA)
                                val cal = Calendar.getInstance()
                                val date = dateFormatDate.format(cal.time)
                                val time = dateFormatTime.format(cal.time)


                                val database = FirebaseDatabase.getInstance()
                                val myRef = database.reference
                                val map = HashMap<String, String?>()
                                map["city"] = strCity
                                map["email"] = strEmail
                                map["lastLoginDate"] = date
                                map["lastLoginTime"] = time
                                map["name"] = strName
                                map["number"] = strPhoneNumber
                                myRef.child("User").child(userId).setValue(map)
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Registration successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                                startActivity(intent)
                            } else {
                                Log.w(
                                    TAG,
                                    "createUserWithEmail:failure",
                                    task.exception
                                )
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Error in the form", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
            }
        }
    }

    private fun extractData() {
        strName = name.text.toString()
        strEmail = email.text.toString()
        strPassword = password.text.toString()
        strPhoneNumber = phoneNumber.text.toString()
        strCity = city.selectedItem.toString()
    }

    fun verifyData(): Boolean {
        return strName.isNotEmpty() && strEmail.isNotEmpty() && strPassword.isNotEmpty() &&
                strPhoneNumber.isNotEmpty() && strCity.isNotEmpty()
    }

    companion object {
        private val TAG = RegisterActivity::class.java.simpleName
    }
}
