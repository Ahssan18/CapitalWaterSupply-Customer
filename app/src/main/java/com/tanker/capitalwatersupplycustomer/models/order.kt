package com.tanker.capitalwatersupplycustomer.models

data class Order(
    var userId: String?= null,
    var id: String? =null,
    val name:String? = null,
    val number: String? = null,
    val address :String? = null,
    val quantity: String? = null,
    var uPrice: String? = null,
    var tPrice: String? = null,
    val tankerName : String? = null,
    val tankerType: String? = null,
    val status : String? = null
) {
}