package com.bugraburunguz.android.valetapp.activity

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bugraburunguz.android.valetapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EditProfileActivity : AppCompatActivity(), View.OnClickListener {
    private var name: EditText? = null
    private var email: EditText? = null
    private var contact: EditText? = null
    private var city: Spinner? = null
    private var save: Button? = null
    var strName: String? = ""
    var strEmail: String? = ""
    var strContact: String? = ""
    var strCity: String? = ""
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        name = findViewById(R.id.userProfileEditEtNameData)
        email = findViewById(R.id.userProfileEditEtEmailData)
        contact = findViewById(R.id.userProfileEditEtContactNumberData)
        city = findViewById(R.id.userProfileEditSpinnerCityData)
        save = findViewById(R.id.profileEditBtnSave)
        mAuth = FirebaseAuth.getInstance()
        save?.setOnClickListener(this)
        val intent = intent
        strName = intent.getStringExtra("name")
        strEmail = intent.getStringExtra("email")
        strContact = intent.getStringExtra("contactNumber")
        strCity = intent.getStringExtra("city")
        name?.setText(strName)
        email?.setText(strEmail)
        contact?.setText(strContact)
        city?.setSelection(getIndex(city, strCity))
    }

    private fun getIndex(spinner: Spinner?, myString: String?): Int {
        for (i in 0 until spinner!!.count) {
            if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                return i
            }
        }
        return 0
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.profileEditBtnSave -> {
                //Save to firebase
                val user = mAuth!!.currentUser
                val userId = user!!.uid
                val database = FirebaseDatabase.getInstance()
                val myRef = database.reference.child("User").child(userId)
                myRef.child("city").setValue(city!!.selectedItem.toString())
                myRef.child("name").setValue(name!!.text.toString())
                myRef.child("number").setValue(contact!!.text.toString())
                Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
