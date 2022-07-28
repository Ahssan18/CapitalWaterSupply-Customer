package com.tanker.capitalwatersupplycustomer.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.tanker.capitalwatersupplycustomer.R
import com.tanker.capitalwatersupplycustomer.Utils
import java.util.*


class SplashActivity : AppCompatActivity() {
    private lateinit var util: Utils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        util = Utils(this)
        val id = util.getData("id")
        if (id.equals("")) {
            util.addData("id", UUID.randomUUID().toString())
        }

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            if (util.getData("login").equals("yes")) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, PhoneNumberActivity::class.java))
            }
            finish()
        }, SPLASH_TIMEOUT)
    }

    companion object {
        const val SPLASH_TIMEOUT: Long = 2000
    }
}