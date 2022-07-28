package com.tanker.capitalwatersupplycustomer.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthProvider
import com.tanker.capitalwatersupplycustomer.R
import com.tanker.capitalwatersupplycustomer.Utils


class OtpActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var otpNum: EditText
    private lateinit var verifyOtp: Button
    private lateinit var verificationId: String
    private lateinit var mobileNumber: String
    private lateinit var name: String
    private lateinit var util: Utils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        initViews()
        clickListeners()
    }

    private fun clickListeners() {
        verifyOtp.setOnClickListener {
            var otp: String = otpNum.text.toString()
            if (otp.length < 6) {
                Toast.makeText(this@OtpActivity, "Enter valid 6 digit code", Toast.LENGTH_LONG)
                    .show()
            } else {
                verifyOTP(otp);
            }
        }
    }

    private fun verifyOTP(otp: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    util.addData("phone", mobileNumber)
                    util.addData("login", "yes")
                    util.addData("name", name)
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    var message = "Somthing is wrong, we will fix it soon..."

                    if (it.getException() is FirebaseAuthInvalidCredentialsException) {
                        message = "Invalid code entered..."
                    }
                    Toast.makeText(this@OtpActivity, message, Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    private fun initViews() {
        util = Utils(this)
        mAuth = FirebaseAuth.getInstance();
        verificationId = intent?.getStringExtra("verificationId").toString()
        mobileNumber = intent?.getStringExtra("mobileNumber").toString()
        name = intent?.getStringExtra("name").toString()
        otpNum = findViewById(R.id.txtEnterMobileNumber)
        verifyOtp = findViewById(R.id.btnSubmit)
    }
}