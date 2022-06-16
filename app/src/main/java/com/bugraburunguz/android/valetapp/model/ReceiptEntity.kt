package com.bugraburunguz.android.valetapp.model

import java.io.Serializable

class ReceiptEntity : Serializable {
    var carColor: String? = null
    var carCompany: String? = null
    var carNo: String? = null
    var dateTime: String? = null
    var email: String? = null
    var lotNo: String? = null
    var noOfHours: String? = null
    var paymentAmount: String? = null
    var paymentMethod: String? = null
    var spotNo: String? = null

    constructor(
        carColor: String?,
        carCompany: String?,
        carNo: String?,
        dateTime: String?,
        email: String?,
        lotNo: String?,
        noOfHours: String?,
        paymentAmount: String?,
        paymentMethod: String?,
        spotNo: String?
    ) {
        this.carColor = carColor
        this.carCompany = carCompany
        this.carNo = carNo
        this.dateTime = dateTime
        this.email = email
        this.lotNo = lotNo
        this.noOfHours = noOfHours
        this.paymentAmount = paymentAmount
        this.paymentMethod = paymentMethod
        this.spotNo = spotNo
    }

    constructor() {}
}
