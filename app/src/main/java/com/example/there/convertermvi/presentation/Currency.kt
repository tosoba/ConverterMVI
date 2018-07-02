package com.example.there.convertermvi.presentation

import android.os.Parcel
import android.os.Parcelable

data class Currency(
        val code: String,
        val type: Type
): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readSerializable() as Type
    )

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeString(code)
        parcel?.writeSerializable(type)
    }

    override fun describeContents(): Int = 0

    enum class Type {
        BASE, CHOSEN
    }

    companion object CREATOR : Parcelable.Creator<Currency> {
        override fun createFromParcel(parcel: Parcel): Currency = Currency(parcel)

        override fun newArray(size: Int): Array<Currency?> = arrayOfNulls(size)
    }
}