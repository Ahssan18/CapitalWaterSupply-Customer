package com.mazy.capitalwatersupplycustomer.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mazy.capitalwatersupplycustomer.R


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(Runnable {
            if(FirebaseAuth.getInstance().currentUser!=null)
            {
                startActivity(Intent(this, MainActivity::class.java))

            }else
            {

                startActivity(Intent(this, SignInActivity::class.java))
            }
            finish()
        }, SPLASH_TIMEOUT)
    }
    companion object{
        const val SPLASH_TIMEOUT:Long = 2000
    }
}