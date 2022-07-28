package com.tanker.capitalwatersupplycustomer.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tanker.capitalwatersupplycustomer.R
import com.tanker.capitalwatersupplycustomer.adapters.OrdersAdapter
import com.tanker.capitalwatersupplycustomer.models.Order
import kotlinx.android.synthetic.main.activity_orders.*

class OrdersActivity : AppCompatActivity() {

    private  lateinit var adapter: OrdersAdapter
    private var id:String=""
    private val dbOrderRef = FirebaseDatabase.getInstance().getReference("Orders")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        val toolBar = supportActionBar
        toolBar?.title = "My Orders"
        toolBar?.setDisplayHomeAsUpEnabled(true)
        toolBar?.setDisplayShowHomeEnabled(true);

        OrdersPB.visibility = View.VISIBLE
        val manager  = LinearLayoutManager(this)
        ordersRV.layoutManager = manager
        adapter = OrdersAdapter()
        ordersRV.adapter = adapter

        getOrders()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true;
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
    private fun getOrders(){
     dbOrderRef.child(this.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE).getString("id", "").toString()).addValueEventListener(object : ValueEventListener{
         override fun onDataChange(snapshot: DataSnapshot) {
             if(snapshot.exists()){
                 OrdersPB.visibility = View.GONE
                 val orderList :  ArrayList<Order> = arrayListOf()
                 for(orders in snapshot.children){
                     val order = orders.getValue(Order::class.java)
                         order?.let{
                             orderList.add(it)
                         }
                 }
                 adapter.setOrders(orderList)
             }else{
                 OrdersPB.visibility = View.GONE
                 noOrderFount.visibility = View.VISIBLE
             }
         }

         override fun onCancelled(error: DatabaseError) {

         }

     })
    }

    override fun onNavigateUp(): Boolean {
        this.finish()
        return super.onNavigateUp()
    }
}