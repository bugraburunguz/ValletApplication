package com.bugraburunguz.android.valetapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.bugraburunguz.android.valetapp.R
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mAuth = FirebaseAuth.getInstance()
        Handler().postDelayed({
            val user = mAuth.currentUser
            if (user == null) {
                val intent = Intent(this@StartActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@StartActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, SPLASH_TIME_OUT.toLong())
    }

    companion object {
        private const val SPLASH_TIME_OUT = 3000
    }
}
