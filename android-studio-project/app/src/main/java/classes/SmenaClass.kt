package classes

import android.os.Parcel
import android.os.Parcelable


data class SmenaClass(
    val engineerId: String? = null,
    val day: Int? = null,
    val mounth: Int? = null,
    val year: Int? = null,
    val hourStart: Int? = null,
    val minuteStart: Int? = null,
    val hourEnd: Int? = null,
    val minuteEnd: Int? = null,
    val timetable: Boolean? = null,
    val id: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt() == 1, // Изменили чтение timetable
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(engineerId)
        parcel.writeInt(day ?: 0)
        parcel.writeInt(mounth ?: 0)
        parcel.writeInt(year ?: 0)
        parcel.writeInt(hourStart ?: 0)
        parcel.writeInt(minuteStart ?: 0)
        parcel.writeInt(hourEnd ?: 0)
        parcel.writeInt(minuteEnd ?: 0)
        parcel.writeInt(if (timetable == true) 1 else 0) // Изменили запись timetable
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SmenaClass> {
        override fun createFromParcel(parcel: Parcel): SmenaClass {
            return SmenaClass(parcel)
        }

        override fun newArray(size: Int): Array<SmenaClass?> {
            return arrayOfNulls(size)
        }
    }
}
