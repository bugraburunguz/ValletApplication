package com.bugraburunguz.android.valetapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bugraburunguz.android.valetapp.R
import com.bugraburunguz.android.valetapp.databinding.ActivityHomeBinding
import com.bugraburunguz.android.valetapp.model.UserEntity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding
    private var mAuth: FirebaseAuth? = null
    private lateinit var drawerUsername: TextView
    private lateinit var drawerAccount: TextView
    private lateinit var headerView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val backB: ImageButton = findViewById(R.id.backB)
        val logOutB: ImageButton = findViewById(R.id.logOutB)
        val profileB: ImageButton = findViewById(R.id.profileB)
        val textView2: TextView = findViewById(R.id.textView2)
        val textView3: TextView = findViewById(R.id.textView3)
        val textViewEdt:TextView=findViewById(R.id.textViewEdit)

        // register all the card views with their appropriate IDs
        val paymentReceiptCard: CardView = findViewById(R.id.contributeCard)
        val parkLocationCard: CardView = findViewById(R.id.practiceCard)
        val learnCard: CardView = findViewById(R.id.learnCard)
        val parkingReportCard: CardView = findViewById(R.id.interestsCard)
        val helpCard: CardView = findViewById(R.id.helpCard)
        val logoutCard: CardView = findViewById(R.id.settingsCard)


        // handle each of the image buttons with the OnClickListener
        backB.setOnClickListener {
            Toast.makeText(this, "Back Button", Toast.LENGTH_SHORT).show()
        }
        logOutB.setOnClickListener {
            Toast.makeText(this, "Logout Button", Toast.LENGTH_SHORT).show()
        }
        profileB.setOnClickListener {
            val userProfileIntent = Intent(this@HomeActivity, ProfileActivity::class.java)
            startActivity(userProfileIntent)
        }
        textViewEdt.setOnClickListener {
            val userProfileIntent = Intent(this@HomeActivity, ProfileActivity::class.java)
            startActivity(userProfileIntent)
        }

        // handle each of the cards with the OnClickListener
        paymentReceiptCard.setOnClickListener {
            val paymentIntent = Intent(this@HomeActivity, ParkingActivity::class.java)
            startActivity(paymentIntent)
        }
        parkLocationCard.setOnClickListener {
            val locationIntent = Intent(this@HomeActivity, LocationActivity::class.java)
            startActivity(locationIntent)
        }
        learnCard.setOnClickListener {
            val manualIntent = Intent(this@HomeActivity, ManualActivity::class.java)
            startActivity(manualIntent)
        }
        parkingReportCard.setOnClickListener {
            val paymentReportIntent =
                Intent(this@HomeActivity, ParkingReportActivity::class.java)
            startActivity(paymentReportIntent)
        }
        helpCard.setOnClickListener {
            val supportIntent = Intent(this@HomeActivity, SupportActivity::class.java)
            startActivity(supportIntent)
        }
        logoutCard.setOnClickListener {
            mAuth?.signOut()
            val intent = Intent(this@HomeActivity, StartActivity::class.java)
            startActivity(intent)
            finish()
        }

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        mAuth = FirebaseAuth.getInstance()

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        headerView = navigationView.getHeaderView(0)
        drawerUsername = headerView.findViewById(R.id.drawer_name)
        drawerAccount = headerView.findViewById(R.id.drawer_email)

        navigationView.setNavigationItemSelectedListener(this)

        val database = FirebaseDatabase.getInstance()

        val user = mAuth!!.currentUser
        val userId = user!!.uid

        val myRef = database.getReference("User").child(userId)

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userEntity1: UserEntity? = dataSnapshot.getValue(
                    UserEntity::class.java
                )
                drawerUsername.text = userEntity1?.name
                drawerAccount.text = userEntity1?.email
                textView2.text = userEntity1?.name
                textView3.text=userEntity1?.email

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_payment_receipt -> {
                val paymentIntent = Intent(this@HomeActivity, ParkingActivity::class.java)
                startActivity(paymentIntent)
            }
            R.id.nav_location -> {
                val locationIntent = Intent(this@HomeActivity, LocationActivity::class.java)
                startActivity(locationIntent)
            }
            R.id.nav_parking_manual -> {
                val manualIntent = Intent(this@HomeActivity, ManualActivity::class.java)
                startActivity(manualIntent)
            }
            R.id.nav_report -> {
                val paymentReportIntent =
                    Intent(this@HomeActivity, ParkingReportActivity::class.java)
                startActivity(paymentReportIntent)
            }
            R.id.nav_profile -> {
                val userProfileIntent = Intent(this@HomeActivity, ProfileActivity::class.java)
                startActivity(userProfileIntent)
            }
            R.id.nav_support_contact -> {
                val supportIntent = Intent(this@HomeActivity, SupportActivity::class.java)
                startActivity(supportIntent)
            }
            R.id.nav_logout -> {
                mAuth?.signOut()
                val intent = Intent(this@HomeActivity, StartActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
