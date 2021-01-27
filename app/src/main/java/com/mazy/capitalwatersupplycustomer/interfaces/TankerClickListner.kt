package com.mazy.capitalwatersupplycustomer.interfaces

import android.view.View
import com.mazy.capitalwatersupplycustomer.models.Tanker

interface TankerClickListner {

    fun onTankerItemClickListner(view: View, tanker: Tanker)

}