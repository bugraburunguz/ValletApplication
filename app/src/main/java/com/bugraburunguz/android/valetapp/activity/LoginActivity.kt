package com.bugraburunguz.android.valetapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bugraburunguz.android.valetapp.R
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity(), View.OnClickListener,
    GoogleApiClient.OnConnectionFailedListener {

    var btnLogin: Button? = null
    var btnRegister: Button? = null
    var edtEmail: EditText? = null
    var edtPassword: EditText? = null
    var signInButton: SignInButton? = null
    var strEmail = ""
    var strPassword: String? = ""
    var mAuth: FirebaseAuth? = null
    var googleApiClient: GoogleApiClient? = null

    val TAG = LoginActivity::class.java.simpleName
    val RC_SIGN_IN = 9001

    var name = ""
    var email: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        mAuth = FirebaseAuth.getInstance()
        btnLogin = findViewById(R.id.loginBtnLogin)
        btnRegister = findViewById<Button>(R.id.loginBtnRegister)
        edtEmail = findViewById<EditText>(R.id.loginEtEmail)
        edtPassword = findViewById<EditText>(R.id.loginEtPassword)
        signInButton = findViewById<SignInButton>(R.id.google_sign_in)
        btnLogin!!.setOnClickListener(this)
        btnRegister?.setOnClickListener(this)
        signInButton!!.setOnClickListener(this)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("177024362875-47c182nbkasvaifgd8lto0p4eeqjaepu.apps.googleusercontent.com")
            .build()
        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.loginBtnLogin -> {
                extractData()
                if (dataValidate()) {
                    mAuth!!.signInWithEmailAndPassword(strEmail, strPassword!!)
                        .addOnCompleteListener(
                            this
                        ) { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "signInWithEmail:success")
                                val user = mAuth!!.currentUser
                                val loginIntent =
                                    Intent(this@LoginActivity, HomeActivity::class.java)
                                startActivity(loginIntent)
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
            R.id.loginBtnRegister -> {
                Toast.makeText(this, "Register Clicked", Toast.LENGTH_SHORT).show()
                val registerIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(registerIntent)
            }
            R.id.google_sign_in -> signIn()
            else -> {
            }
        }
    }

    fun extractData() {
        strEmail = edtEmail!!.text.toString()
        strPassword = edtPassword!!.text.toString()
    }

    fun dataValidate(): Boolean {
        var isDataValid = true
        if (strEmail == "" || strPassword == "") {
            isDataValid = false
        }
        return isDataValid
    }

    fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient!!)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                email = account.email
                name = account.displayName!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d(
            TAG,
            "onConnectionFailed: $connectionResult"
        )
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    Log.d(
                        TAG,
                        "signInWithCredential:success"
                    )
                    val user = mAuth!!.currentUser
                    Log.d(
                        TAG,
                        "onComplete: UserID : " + user!!.uid
                    )
                    checkIfFirstTimeLogin()
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w(
                        TAG,
                        "signInWithCredential:failure",
                        task.exception
                    )
                    Toast.makeText(
                        this@LoginActivity,
                        "Authentication Failed.",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                }

            }
    }


    fun checkIfFirstTimeLogin() {
        val userId = mAuth!!.uid
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("User").child(userId!!)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@LoginActivity, GoogleSignInExtraActivity::class.java)
                    intent.putExtra("name", name)
                    intent.putExtra("email", email)
                    startActivity(intent)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}
