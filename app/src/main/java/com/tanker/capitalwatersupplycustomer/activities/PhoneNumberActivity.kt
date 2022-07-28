package com.tanker.capitalwatersupplycustomer.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.tanker.capitalwatersupplycustomer.R
import java.util.concurrent.TimeUnit


class PhoneNumberActivity : AppCompatActivity() {
    private var TAG = "PhoneNumberActivity";
    private lateinit var sendOtp: Button
    private lateinit var phoneNumber: EditText
    private lateinit var name: EditText
    private lateinit var userName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number)
        supportActionBar?.hide();
        initViews()
        clickListeners()
    }

    private fun clickListeners() {
        FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true);
        sendOtp.setOnClickListener {
            var num = phoneNumber.text.toString()
            var nameUser = name.text.toString()
            if (!nameUser.isEmpty()) {
                userName = nameUser
                if (num.length < 10) {
                    Toast.makeText(this, "Enter valid number", Toast.LENGTH_LONG).show()
                } else {
                    sendOtp(num)
                }
            } else {
                Toast.makeText(this, "Name is required!", Toast.LENGTH_LONG).show()
            }


        }
    }

    private fun sendOtp(mobileNumber: String) {
        Log.e(TAG, "sendOtp 92" + "${mobileNumber}")
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber("+92${mobileNumber}")       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(object : OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    Toast.makeText(
                        this@PhoneNumberActivity,
                        "Verification completed",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e(TAG, "onVerificationFailed => " + e.message)
                    Toast.makeText(
                        this@PhoneNumberActivity,
                        "Verification failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                    super.onCodeSent(s, forceResendingToken)
                    var intent = Intent(this@PhoneNumberActivity, OtpActivity::class.java)
                    intent.putExtra("verificationId", s)
                    intent.putExtra("mobileNumber", "+92" + mobileNumber)
                    intent.putExtra("name", userName)
                    startActivity(intent)
                    Toast.makeText(
                        this@PhoneNumberActivity,
                        "Code has Sent to given mobile number.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCodeAutoRetrievalTimeOut(s: String) {
                    super.onCodeAutoRetrievalTimeOut(s)
                }
            })          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)


    }

    private fun initViews() {
        sendOtp = findViewById(R.id.btnSubmit)
        phoneNumber = findViewById(R.id.txtEnterMobileNumber)
        name = findViewById(R.id.et_name)
    }
}