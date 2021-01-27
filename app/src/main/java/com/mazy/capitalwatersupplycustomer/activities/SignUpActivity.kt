package com.mazy.capitalwatersupplycustomer.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.mazy.capitalwatersupplycustomer.R
import com.mazy.capitalwatersupplycustomer.models.User
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.notContains
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private val dbRef = FirebaseDatabase.getInstance().getReference("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mAuth = FirebaseAuth.getInstance()
        val toolbar = supportActionBar
        toolbar?.title="SignUp"
        toolbar?.setDisplayHomeAsUpEnabled(true)

        btnSignUp.setOnClickListener {
            if(checkValidation()){
                signUpPB.visibility = View.VISIBLE
                val name = edtSignUpName.text.toString()
                val email = edtSignUpEmail.text.toString()
                val password = edtSignUpPassword.text.toString()
                val user = User("", name, email, password)
                createAccount(user)
            }
        }
        tvSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            this.finish()
        }
    }

    private fun createAccount(user: User) {
        mAuth.createUserWithEmailAndPassword(user.email!!, user.password!!).addOnCompleteListener {
            Log.e("what_res",it.toString()+it.exception);
            if(it.isSuccessful){
                    addUser(user)
//                val intent   = Intent(this, SignInActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                startActivity(intent)
//                this.finish()
//                Toast.makeText(this, "Account Created Successfully!", Toast.LENGTH_SHORT).show()
            }else{
                signUpPB.visibility = View.GONE
                Toast.makeText(this, "Something went wrong pleae try again later!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun  addUser(user: User){
        user.id = dbRef.push().key
        dbRef.child(user.id!!).setValue(user).addOnCompleteListener {
            if(it.isSuccessful){
                signUpPB.visibility = View.GONE
                val intent   = Intent(this, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                this.finish()
                Toast.makeText(this, "Account Created Successfully!", Toast.LENGTH_SHORT).show()
            }else{
                signUpPB.visibility = View.GONE
                Toast.makeText(this, "Something went wrong pleae try again later!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun checkValidation():Boolean{
        if(edtSignUpName.nonEmpty()){
            if(edtSignUpEmail.nonEmpty()){
                if(edtSignUpEmail.notContains(" ")){
                    if(edtSignUpEmail.validEmail()){
                        if(edtSignUpPassword.nonEmpty()){
                            if(edtSignUpPassword.notContains(" ")){
                                if(edtSignUpPassword.text.length >= 6){
                                    if(edtSignUpCPassword.nonEmpty()){
                                        if(edtSignUpCPassword.text.toString() == edtSignUpPassword.text.toString()){
                                            return true
                                        }else{
                                            edtSignUpCPassword.error = "Password and confirm password must be same!"
                                            edtSignUpCPassword.requestFocus()
                                        }
                                    }else{
                                        edtSignUpCPassword.error = "Please confirm your password!"
                                        edtSignUpCPassword.requestFocus()
                                    }
                                }else{
                                    edtSignUpPassword.error = "Password must contain at least 6 characters!"
                                    edtSignUpPassword.requestFocus()
                                }
                            }else{
                                edtSignUpPassword.error = "Spaces are not allowed!"
                                edtSignUpPassword.requestFocus()
                            }
                        }else{
                            edtSignUpPassword.error = "Password Required!"
                            edtSignUpPassword.requestFocus()
                        }
                    }else{
                        edtSignUpEmail.error = "Please enter valid email!"
                        edtSignUpEmail.requestFocus()
                    }
                }else{
                    edtSignUpEmail.error = "Spaces are not allowed!"
                    edtSignUpEmail.requestFocus()
                }
            }else{
                edtSignUpEmail.error = "Email Required!"
                edtSignUpEmail.requestFocus()
            }
        }else{
            edtSignUpName.error = "Name Required!"
            edtSignUpName.requestFocus()
        }
     return false
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return super.onSupportNavigateUp()
    }
}