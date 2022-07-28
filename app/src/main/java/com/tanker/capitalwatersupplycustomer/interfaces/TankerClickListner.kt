package com.tanker.capitalwatersupplycustomer.interfaces

import android.view.View
import com.tanker.capitalwatersupplycustomer.models.Tanker

interface TankerClickListner {

    fun onTankerItemClickListner(view: View, tanker: Tanker)

}