package com.example.hypersonalbooster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hypersonalbooster.databinding.LayoutLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LoginActivity : AppCompatActivity(), CloudCallbackListener {

    private lateinit var binding : LayoutLoginBinding

    private var mAuth: FirebaseAuth? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 9001

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref = database.getReference("members")
    val ref_kiosk = database.getReference("kiosk")

    var kiosk_list = ""

    private var end_time: Long = 0

    var check_health = ""
    var check_cloud = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        if(mAuth?.currentUser != null) {
            val intent: Intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.login.setOnClickListener {
            signIn()
        }
    }

    override fun onBackPressed() {
        // super.onBackPressed()

        if (System.currentTimeMillis() - end_time >= 2000) {
            end_time = System.currentTimeMillis()
            Toast.makeText(applicationContext, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else if (System.currentTimeMillis() - end_time < 2000) {
            finishAffinity()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth!!.currentUser
                    val uid = user!!.uid.toString()
                    val name = user!!.displayName

                    ref.child(uid).child("UID").setValue(uid)
                    ref.child(uid).child("name").setValue(name)

                    val shared_cloud = getSharedPreferences("data_cloud", 0)
                    val editor_cloud = shared_cloud.edit()

                    editor_cloud.putString("uid", uid)
                    editor_cloud.putString("name", name)
                    editor_cloud.apply()

                    //Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()

                    health_check()
                    cloud_check(uid, this)


                } else {
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun health_check() {
        val shared_health = getSharedPreferences("data_health", 0)
        val editor_health = shared_health.edit()

        val height = shared_health.getFloat("height", 0.0F)
        val weight = shared_health.getFloat("weight", 0.0F)

        if (height == 0.0F || weight == 0.0F) {
            Log.d("health_check", "false")
            check_health = "false"
        } else {
            Log.d("check_health", "true")
            editor_health.putString("check_health", "true")
            editor_health.apply()
            check_health = "true"
        }
    }

    private fun cloud_check(uid : String, callback : CloudCallbackListener) {
        var flag: String = ""
        ref.child(uid).child("cloud_check")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot != null) {
                        Log.d("check_cloud", "snapshot result : $dataSnapshot")
                        if (dataSnapshot.value != null) {
                            Log.d("check_cloud", "true")
                            check_cloud = "true"
                        }
                        else {
                            Log.d("check_cloud", "false")
                            check_cloud = "false"
                        }

                        callback.onCallback()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    override fun onCallback() {
        if(check_cloud == "true") {
            getSharedPreferences("data_cloud", 0).edit().putString("cloud_check", "true").apply()
        }
        else {
            getSharedPreferences("data_cloud", 0).edit().putString("cloud_check", "false").apply()
        }

        val intent = Intent(this,CloudLoadingActivity_Booster::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
    }
}