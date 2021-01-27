package com.mazy.capitalwatersupplycustomer.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mazy.capitalwatersupplycustomer.R
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.notContains
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        val toolbar= supportActionBar
        toolbar?.title="Login"
        toolbar?.setDisplayHomeAsUpEnabled(true)
        mAuth = FirebaseAuth.getInstance()

        btnSignIn.setOnClickListener {
            if(checkValidation()){
                signInPB.visibility = View.VISIBLE
                val email = edtSignInEmail.text.toString()
                val password = edtSignInPassword.text.toString()
                signIn(email, password)
            }

        }
        tvSignUp.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

    }

    private fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                signInPB.visibility = View.GONE
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                this.finish()
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
            }else{
                signInPB.visibility = View.GONE
                Toast.makeText(this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkValidation():Boolean{
        if(edtSignInEmail.nonEmpty()){
            if(edtSignInEmail.notContains(" ")){
                if(edtSignInEmail.validEmail()){
                    if(edtSignInPassword.nonEmpty()){
                        return true
                    }else{
                        edtSignInPassword.error = "Password Required!"
                        edtSignInPassword.requestFocus()
                    }
                }else{
                    edtSignInEmail.error = "Please enter valid email!"
                    edtSignInEmail.requestFocus()
                }
            }else{
                edtSignInEmail.error = "Spaces are not allowed!"
                edtSignInEmail.requestFocus()
            }
        }else{
            edtSignInEmail.error = "Email Required!"
            edtSignInEmail.requestFocus()
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return super.onSupportNavigateUp()
    }

}