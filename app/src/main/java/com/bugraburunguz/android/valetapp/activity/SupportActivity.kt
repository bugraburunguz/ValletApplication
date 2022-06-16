package com.bugraburunguz.android.valetapp.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bugraburunguz.android.valetapp.R

class SupportActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var btnCall: Button
    lateinit var btnSMS: Button
    lateinit var btnEmail: Button
    lateinit var imgCall: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)

        imgCall = (findViewById<View>(R.id.imgCall) as ImageView?)!!
        imgCall.setOnClickListener(this)
        btnCall = (findViewById<View>(R.id.btnCall) as Button?)!!
        btnCall.setOnClickListener(this)
        btnSMS = (findViewById<View>(R.id.btnSMS) as Button?)!!
        btnSMS.setOnClickListener(this)
        btnEmail = (findViewById<View>(R.id.btnEmail) as Button?)!!
        btnEmail.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnCall -> makeCall()
            R.id.btnEmail -> sendEmail()
            R.id.btnSMS -> sendSMS()
        }
    }

    //No permission required
    private fun makeCall() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:05347099394")
        startActivity(intent)
    }

    //No permission required
    private fun sendSMS() {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.data = Uri.parse("sms:05347099394")
        sendIntent.putExtra("sms_body", "Hey, we need help")
        startActivity(sendIntent)
    }

    private fun sendEmail() {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("bugraburunguz@gmail.com"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Help us")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Please help us!!")
        emailIntent.type = "message/rfc822"
        startActivity(Intent.createChooser(emailIntent, "bugraburunguz@gmail.com"))
    }
}
