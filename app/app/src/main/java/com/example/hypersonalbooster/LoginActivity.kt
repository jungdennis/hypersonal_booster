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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay


class LoginActivity : AppCompatActivity() {

    private lateinit var binding : LayoutLoginBinding

    private var mAuth: FirebaseAuth? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 9001

    val database = FirebaseDatabase.getInstance("https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app")
    val ref_booster = database.getReference("1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY")

    private var end_time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 나중에 첫 화면으로 빠질 예정
        binding = LayoutLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
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

                    val cloud = cloud_check(uid)
                    val health = health_check()

                    ref_booster.child("apptest").child(uid).child("UID").setValue(uid)
                    ref_booster.child("apptest").child(uid).child("name").setValue(name)

                    val shared = getSharedPreferences("data_cloud", 0)
                    val editor = shared.edit()

                    editor.putString("uid", uid)
                    editor.putString("name", name)
                    editor.apply()


                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT)
                        .show()

                    if(cloud) {
                        if(health) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else {
                            val intent = Intent(this, RegisterActivity_0_Welcome::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    else{
                        val intent = Intent(this, RegisterActivity_0_Welcome::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                else {
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun health_check() : Boolean {
        val check = getSharedPreferences("data_health", 0)
        val editor = check.edit()

        val height = check.getFloat("height", 0.0F)
        val weight = check.getFloat("weight", 0.0F)

        if(height == 0.0F || weight == 0.0F) {
            Log.d("health_check", "false")
            return false
        }
        else {
            Log.d("health_check", "true")
            editor.putString("health_check", "true")
            editor.apply()
            return true
        }
    }

    fun cloud_check(uid : String) : Boolean {
        var flag : String? = null

        ref_booster.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                flag = dataSnapshot.child("cloud_flag").getValue().toString()
            }
            override fun onCancelled(databaseError: DatabaseError) {}})

        if(flag == null) {
            Log.d("cloud_check", "false")
            return false
        }
        else{
            Log.d("cloud_check", "true")
            return true
        }
    }
}