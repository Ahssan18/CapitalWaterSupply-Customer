package com.mazy.capitalwatersupplycustomer.models

data class User(
    var id: String? = null,
    val name: String? = null,
    val email:String? = null,
    val password:String?= null
) {
}