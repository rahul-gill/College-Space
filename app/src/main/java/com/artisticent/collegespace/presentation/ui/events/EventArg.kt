package com.artisticent.collegespace.presentation.ui.events

import android.os.Parcel
import android.os.Parcelable
import java.util.*


data class EventArg(
    var name: String,
    var startDate: Calendar = Calendar.getInstance(),
    var endDate: Calendar = Calendar.getInstance(),
    var type: String = "Personal Event"
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readSerializable() as Calendar,
        parcel.readSerializable() as Calendar,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeSerializable(startDate)
        parcel.writeSerializable(endDate)
        parcel.writeSerializable(type)
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