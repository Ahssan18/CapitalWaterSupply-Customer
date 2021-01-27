package com.mazy.capitalwatersupplycustomer.models

import java.io.Serializable

data class Tanker(
    var id:String? = null,
    val tankerName: String? = null,
    val tankerType : String? = null,
//    val tankerPrice: String? = null,
//    val address:String? = null
):Serializable