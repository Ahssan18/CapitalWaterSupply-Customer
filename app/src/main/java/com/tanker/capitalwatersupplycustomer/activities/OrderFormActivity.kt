package com.tanker.capitalwatersupplycustomer.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tanker.capitalwatersupplycustomer.R
import com.tanker.capitalwatersupplycustomer.Utils
import com.tanker.capitalwatersupplycustomer.models.Addresses
import com.tanker.capitalwatersupplycustomer.models.Order
import com.tanker.capitalwatersupplycustomer.models.Tanker
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import kotlinx.android.synthetic.main.activity_order_form.*


class OrderFormActivity : AppCompatActivity() {
    private val dbRef = FirebaseDatabase.getInstance().getReference("Addresses")
    private val dbOrderRef = FirebaseDatabase.getInstance().getReference("Orders")
    val addressList: ArrayList<String> = arrayListOf()
    val CAddressList: ArrayList<Addresses> = arrayListOf()
    val quantityList: ArrayList<String> = arrayListOf()
    private lateinit var util: Utils

    //    val sharedPreference = getSharedPreferences("PREFERENCE_NAME", MODE_PRIVATE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_form)
        val toolbar = supportActionBar

        toolbar?.title = "Order Details"
        util = Utils(this)
        try {
            edtOrderNumber.setText(util?.getData("phone").toString().subSequence(3, 14))
        } catch (e: Exception) {
        }
        edtOrderNumber.isEnabled = false
        edtOrderName.setText(util?.getData("name").toString())
        edtOrderName.isEnabled = false
        toolbar?.setDisplayHomeAsUpEnabled(true)
        OrderProgressDialog.visibility = View.VISIBLE
        getAddresses()

        val tanker = intent.getSerializableExtra("order") as Tanker
        var unitPrice: String = "0"
        spOrderAddresses.onFocusChangeListener
        spOrderAddresses.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
//                val catName = spOrderAddresses.selectedItem.toString()
//                changeTotalPrice(catName)
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val catName = spOrderAddresses.selectedItem.toString()
                for (address in CAddressList) {
                    if (address.address!! == catName) {
                        order_tv_up_amount.setText("Rs. " + address.price)
                        unitPrice = address.price!!
//                        Toast.makeText(this@OrderFormActivity, address.price.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }



        btnConfirmOrder.setOnClickListener {
            if (checkValidation()) {
                OrderProgressDialog.visibility = View.VISIBLE
                val name = edtOrderName.text.toString()
                val phone = edtOrderNumber.text.toString()
//                val address = spOrderAddresses.selectedItem.toString()
                val Address = edtOrderQuantity.text.toString()
                val uPrice = unitPrice
                val order = Order(
                    this.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                        .getString("id", ""),
                    "",
                    name,
                    phone,
                    spOrderAddresses.selectedItem.toString(),
                    "",
                    uPrice,
                    Address,
                    tanker.tankerName,
                    tanker.tankerType,
                    "Pending"
                )
//                Toast.makeText(this, "$totalPrice ${spOrderAddresses.selectedItem.toString()}", Toast.LENGTH_SHORT).show()
                placeOrder(order)
            }
        }
    }


    private fun getAddresses() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    OrderProgressDialog.visibility = View.GONE
                    for (address in snapshot.children) {
                        val addr = address.getValue(Addresses::class.java)
                        addr?.let {
                            addressList.add(it.address!!)
//                            quantityList.add(it.price!!)
                            CAddressList.add(it)
                        }
                    }
                    setAddressSpinnerList()
                } else {
                    OrderProgressDialog.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun setAddressSpinnerList() {
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            addressList
        )
        spOrderAddresses.adapter = spinnerAdapter
    }

    private fun checkValidation(): Boolean {
        if (edtOrderName.nonEmpty()) {
            if (edtOrderNumber.nonEmpty()) {
                if (edtOrderNumber.text.length == 11) {
                    if (edtOrderQuantity.nonEmpty()) {
                        if (edtOrderQuantity.text.toString() != "0") {
                            return true
                        } else {
                            edtOrderQuantity.error = "Please enter quantity"
                            edtOrderQuantity.requestFocus()
                        }
                    } else {
                        edtOrderQuantity.error = "Please enter quantity"
                        edtOrderQuantity.requestFocus()
                    }
                } else {
                    edtOrderNumber.error = "Plaese enter valid phone number!"
                    edtOrderNumber.requestFocus()
                }
            } else {
                edtOrderNumber.error = "Phone Number Required!"
                edtOrderNumber.requestFocus()
            }
        } else {
            edtOrderName.error = "Name Required!"
            edtOrderName.requestFocus()
        }
        return false
    }

    private fun placeOrder(order: Order) {
        val dbAdminOrderRef = FirebaseDatabase.getInstance().getReference("AdminOrders")
        order.id = dbOrderRef.push().key
        dbOrderRef.child(order.userId!!).child(order.id!!).setValue(order).addOnCompleteListener {
            if (it.isSuccessful) {
                //Add Order to admin
                dbAdminOrderRef.child(order.id!!).setValue(order).addOnCompleteListener {
                    if (it.isSuccessful) {
                        OrderProgressDialog.visibility = View.GONE
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        this.finish()
                        Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        OrderProgressDialog.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Something went wrong please try again later!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
//                OrderProgressDialog.visibility = View.GONE
//                val intent = Intent(this, MainActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                startActivity(intent)
//                this.finish()
//                Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_SHORT).show()
            } else {
                OrderProgressDialog.visibility = View.GONE
                Toast.makeText(
                    this,
                    "Something went wrong please try again later!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

//        dbAdminOrderRef.child(order.id!!).setValue(order).addOnCompleteListener {
//            if(it.isSuccessful){
//                OrderProgressDialog.visibility = View.GONE
//                val intent = Intent(this, MainActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                startActivity(intent)
//                this.finish()
//                Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_SHORT).show()
//            }else{
//                OrderProgressDialog.visibility = View.GONE
//                Toast.makeText(this, "Something went wrong please try again later!", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return super.onSupportNavigateUp()
    }
}