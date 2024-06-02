package com.shankha.epiceats.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.ArrayList

class OrderDetails() : Serializable{
    var userUid :String ?= null
    var userName :String ?= null
    var foodNames : MutableList<String> ? = null
    var foodImage : MutableList<String> ? = null
    var foodPrice : MutableList<String> ? = null
    var foodQuantities : MutableList<Int> ? = null
    var address :String ?= null
    var totalPrice :String ?= null
    var phoneNo :String ?= null
    var orderAccepted :Boolean = false
    var paymentReceived :Boolean =false
    var itemPushKey :String ?= null
    var currentTime :Long = 0
    var orderDispatch :Boolean = false

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNo = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentReceived = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
        orderDispatch = parcel.readByte() != 0.toByte()
    }

    constructor(
        userId: String,
        name: String,
        foodItemName: ArrayList<String>,
        foodItemImage: ArrayList<String>,
        foodItemPrice: ArrayList<String>,
        foodItemQuantities: ArrayList<Int>,
        address: String,
        totalAmount: String,
        phoneNo: String,
        orderAccepted: Boolean,
        paymentReceived: Boolean,
        itemPushKey: String?,
        time: Long,
        orderDispatch: Boolean
    ) : this(){
        this.userUid=userId
        this.userName=name
        this.foodNames=foodItemName
        this.foodImage=foodItemImage
        this.foodPrice=foodItemPrice
        this.foodQuantities=foodItemQuantities
        this.address=address
        this.totalPrice=totalAmount
        this.phoneNo=phoneNo
        this.orderAccepted=orderAccepted
        this.paymentReceived=paymentReceived
        this.itemPushKey=itemPushKey
        this.currentTime=time
        this.orderDispatch=orderDispatch
    }

     fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNo)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
         parcel.writeByte(if (orderDispatch) 1 else 0)
    }

    fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }
}
