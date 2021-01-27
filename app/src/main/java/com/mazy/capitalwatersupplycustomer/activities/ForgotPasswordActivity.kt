package com.mazy.capitalwatersupplycustomer.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mazy.capitalwatersupplycustomer.R
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.notContains
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val toolbar=supportActionBar
        toolbar?.title="Forgot Password"
        toolbar?.setDisplayHomeAsUpEnabled(true)
        mAuth = FirebaseAuth.getInstance()

        btnForgot.setOnClickListener {
            if(checkValidation()){
                forgotPasswordPB.visibility = View.VISIBLE
                val email = edtForgotEmail.text.toString()
                sendCodeToEmail(email)
            }
        }
    }

    private fun sendCodeToEmail(email: String) {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                if(it.isSuccessful){
                    forgotPasswordPB.visibility = View.GONE
                    showAlertDialogue()
//                    startActivity(Intent(this, SignInActivity::class.java))
//                    this.finish()
//                    Toast.makeText(this, "password send", Toast.LENGTH_SHORT).show()
                }else{
                    forgotPasswordPB.visibility = View.GONE
                    Toast.makeText(this, "Invalid Email!", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                forgotPasswordPB.visibility = View.GONE
                Toast.makeText(this, "Something went wrong please try again later!", Toast.LENGTH_SHORT).show()

            }
    }

    private fun checkValidation():Boolean{
        if(edtForgotEmail.nonEmpty()){
            if(edtForgotEmail.notContains(" ")){
                if(edtForgotEmail.validEmail()){
                    return true
                }else{
                    edtForgotEmail.error = "Please enter valid email!"
                    edtForgotEmail.requestFocus()
                }
            }else{
                edtForgotEmail.error = "Spaces are not allowed!"
                edtForgotEmail.requestFocus()
            }
        }else{
            edtForgotEmail.error = "Email Required!"
            edtForgotEmail.requestFocus()
        }
        return false
    }

    private fun showAlertDialogue() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Information!")
        alertDialog.setMessage("Please check your email to reset your password!")
        alertDialog.setPositiveButton("ok") { dialog, which ->
            dialog.dismiss()
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            this.finish()

        }
        alertDialog.create()
        alertDialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return super.onSupportNavigateUp()
    }

}