package com.mazy.capitalwatersupplycustomer.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mazy.capitalwatersupplycustomer.R
import java.util.*


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val id = sharedPreference.getString("id", "")
        if (id.equals("")) {
            var editor = sharedPreference.edit()
            editor.putString("id", UUID.randomUUID().toString())
            editor.apply()
        }

        Handler().postDelayed(Runnable {
           /* if (FirebaseAuth.getInstance().currentUser != null) {*/
                startActivity(Intent(this, MainActivity::class.java))
            /*}*/ /*else {
                startActivity(Intent(this, SignInActivity::class.java))
            }*/
            finish()
        }, SPLASH_TIMEOUT)
    }

    companion object {
        const val SPLASH_TIMEOUT: Long = 2000
    }
}