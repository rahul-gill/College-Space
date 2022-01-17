package com.github.rahul_gill.collegespace.presentation.ui.events

import android.os.Parcel
import android.os.Parcelable


data class EventArg(
    var name: String,
    var startDate: Long,
    var endDate: Long,
    var description: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeSerializable(startDate)
        parcel.writeSerializable(endDate)
        parcel.writeSerializable(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EventArg> {
        override fun createFromParcel(parcel: Parcel): EventArg {
            return EventArg(parcel)
        }

        override fun newArray(size: Int): Array<EventArg?> {
            return arrayOfNulls(size)
        }
    }
}