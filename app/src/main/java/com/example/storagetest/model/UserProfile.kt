package com.example.storagetest.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class UserProfile (
    val firstName: String,
    val lastName: String,
    val address: String,
    val phone: String
): Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "") {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeString(firstName)
        p0?.writeString(lastName)
        p0?.writeString(address)
        p0?.writeString(phone)
    }

    companion object CREATOR : Parcelable.Creator<UserProfile> {
        override fun createFromParcel(parcel: Parcel): UserProfile {
            return UserProfile(parcel)
        }

        override fun newArray(size: Int): Array<UserProfile?> {
            return arrayOfNulls(size)
        }
    }
}

