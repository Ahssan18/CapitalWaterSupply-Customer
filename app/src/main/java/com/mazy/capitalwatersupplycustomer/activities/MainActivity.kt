package com.mazy.capitalwatersupplycustomer.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mazy.capitalwatersupplycustomer.AboutUsActivity
import com.mazy.capitalwatersupplycustomer.R
import com.mazy.capitalwatersupplycustomer.adapters.MainAdapter
import com.mazy.capitalwatersupplycustomer.interfaces.TankerClickListner
import com.mazy.capitalwatersupplycustomer.models.Tanker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TankerClickListner{
   private val dbRef = FirebaseDatabase.getInstance().getReference("Tankers")
    private lateinit var adapter : MainAdapter
    private lateinit var adminNumber:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar= supportActionBar
        toolbar?.title="Capital Water Supply"
        mainPB.visibility = View.VISIBLE

        val manager = GridLayoutManager(this, 2)
        rvMain.layoutManager = manager
        adapter = MainAdapter(this)
        rvMain.adapter = adapter
        getTankers()
        getAdminNumber()
    }

    private fun getAdminNumber() {
        val dbNumberRef = FirebaseDatabase.getInstance().getReference("AdminNumber")
        dbNumberRef.child("number").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    adminNumber = snapshot.value.toString()
                } else {

                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun getTankers(){
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val tankerList : ArrayList<Tanker> = arrayListOf()
                    mainPB.visibility = View.GONE
                    for (tankers in snapshot.children){
                        val tanker  = tankers.getValue(Tanker::class.java)
                        tanker?.let {
                            tankerList.add(it)
                        }
                    }
                    adapter.setTankers(tankerList)
                }else{
                    mainPB.visibility = View.GONE
                    tvNoTanker.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onTankerItemClickListner(view: View, tanker: Tanker) {
        when(view.id){
            R.id.btnOrderTanker->{
                val intent = Intent(this, OrderFormActivity::class.java)
                intent.putExtra("order", tanker)
                startActivity(intent)
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.orders->{
                startActivity(Intent(this, OrdersActivity::class.java))
            }
            R.id.logout->{
                showAlertDialogue()
            }
            R.id.aboutUs->{
                startActivity(Intent(this, AboutUsActivity::class.java))
            }
            R.id.callUs->{
                if(checkPermission()){
                    callPhone()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
//        val searchItem = menu?.findItem(R.id.manageTankers)
        return true
    }

    private fun showAlertDialogue() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Warning!")
        alertDialog.setMessage("Are you sure you want to Logout!")
        alertDialog.setPositiveButton("yes") { dialog, which ->
            FirebaseAuth.getInstance().signOut()
            dialog.dismiss()
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            this.finish()

        }
        alertDialog.setNegativeButton("no"){ dialogue, which->
            dialogue.dismiss()
        }
        alertDialog.create()
        alertDialog.show()

    }


    fun callPhone(){
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + adminNumber))
        startActivity(intent)
    }
    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.CALL_PHONE
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.CALL_PHONE
                )
            ) {
                requestPermission()

            } else {
                requestPermission()

            }
            return false
        }
        return true
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CALL_PHONE),
            PERMISSION_REQUEST_CODE
        )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if (ContextCompat.checkSelfPermission(
                            this, android.Manifest.permission.CALL_PHONE
                        ) ==
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        callPhone()
                    }
                } else {
//                    requestPermission()
//                    ActivityCompat.requestPermissions(
//                        this,
//                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
//                        PERMISSION_REQUEST_CODE
//                    )
                }
                return
            }
        }
    }
    companion object{
        const val PERMISSION_REQUEST_CODE = 100
    }
}