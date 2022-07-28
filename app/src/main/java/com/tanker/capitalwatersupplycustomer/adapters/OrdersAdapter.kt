package com.tanker.capitalwatersupplycustomer.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tanker.capitalwatersupplycustomer.R
import com.tanker.capitalwatersupplycustomer.models.Order
import kotlinx.android.synthetic.main.orders_item_layout.view.*


class OrdersAdapter(): RecyclerView.Adapter<OrdersAdapter.AddressesViewHolder>() {
    class AddressesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

    }
    var OrdersList : List<Order> = listOf()
    fun setOrders(orderList: List<Order>){
        OrdersList = orderList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressesViewHolder {
        val View = LayoutInflater.from(parent.context).inflate(R.layout.orders_item_layout, parent, false)
        return AddressesViewHolder(View)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AddressesViewHolder, position: Int) {
        val order  = OrdersList[position]
        holder.itemView.tvOrderId.text ="Order Id: " + order.id.toString()
        holder.itemView.tvOrderTankerName.text = "Name: " + order.tankerName.toString()
        if(order.tankerType != " "){
            holder.itemView.tvOrderTankerType.visibility = View.VISIBLE
            holder.itemView.tvOrderTankerType.text = order.tankerType.toString()
        }
        holder.itemView.tvOrderQuantity.text ="Quantity: " +order.quantity.toString()
        holder.itemView.tvOrderUnitPrice.text = "Unit Price: "+ order.uPrice.toString()
        holder.itemView.tvOrderTotalPrice.text ="Customer Address: "+ order.tPrice.toString()
        holder.itemView.tvOrderPhoneNumber.text = "Phone Number: "+ order.number.toString()
        holder.itemView.tvOrderStatus.text = "Status: "+ order.status.toString()
        holder.itemView.tvOrderAddress.text = "Address: "+order.address.toString()
    }
    override fun getItemCount(): Int {
        return OrdersList.size
    }
}