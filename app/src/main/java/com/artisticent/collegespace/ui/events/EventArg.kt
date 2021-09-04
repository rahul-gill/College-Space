package com.artisticent.collegespace.ui.events

import android.os.Parcel
import android.os.Parcelable
import java.util.*


data class EventArg(
    var name: String,
    var startDate: Calendar = Calendar.getInstance(),
    var endDate: Calendar = Calendar.getInstance()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readSerializable() as Calendar,
        parcel.readSerializable() as Calendar
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeSerializable(startDate)
        parcel.writeSerializable(endDate)
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