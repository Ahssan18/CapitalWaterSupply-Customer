package com.mazy.capitalwatersupplycustomer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mazy.capitalwatersupplycustomer.R
import com.mazy.capitalwatersupplycustomer.interfaces.TankerClickListner
import com.mazy.capitalwatersupplycustomer.models.Tanker
import kotlinx.android.synthetic.main.main_item_layout.view.*


class MainAdapter(val tankerClickListner: TankerClickListner): RecyclerView.Adapter<MainAdapter.AddressesViewHolder>() {
    class AddressesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

    }
    var tankerList : List<Tanker> = listOf()
    fun setTankers(tankList: List<Tanker>){
        tankerList = tankList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressesViewHolder {
        val View = LayoutInflater.from(parent.context).inflate(R.layout.main_item_layout, parent, false)
        return AddressesViewHolder(View)
    }

    override fun onBindViewHolder(holder: AddressesViewHolder, position: Int) {
        val tanker = tankerList[position]
        holder.itemView.tvTankerName.text = tanker.tankerName
        if(tanker.tankerType != " "){
            holder.itemView.tvTankerType.visibility = View.VISIBLE
            holder.itemView.tvTankerType.text = tanker.tankerType
        }
        holder.itemView.btnOrderTanker.setOnClickListener {
            tankerClickListner.onTankerItemClickListner(it, tanker)
        }


    }
    override fun getItemCount(): Int {
        return tankerList.size
    }
}