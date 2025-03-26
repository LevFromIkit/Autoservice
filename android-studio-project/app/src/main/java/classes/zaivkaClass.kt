package classes

import android.os.Parcel
import android.os.Parcelable

data class zaivkaClass(
    val id: String? = null,
    val engineerId: String? = null,
    val userId: String? = null,
    val serviceId: String? = null,
    val dayAndMonth: String? = null,
    val year: String? = null,
    val hourAndMinute: String? = null,
    var clientDescription: String? = null,
    var engineerDescription: String? = null,
    var carDescription: String? = null,
    var carProbeg: String? = null,
    var finish: Boolean = false,
    var accept: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt() == 1,
                parcel.readInt() == 1
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(engineerId)
        parcel.writeString(userId)
        parcel.writeString(serviceId)
        parcel.writeString(dayAndMonth)
        parcel.writeString(year)
        parcel.writeString(hourAndMinute)
        parcel.writeString(clientDescription)
        parcel.writeString(engineerDescription)
        parcel.writeString(carDescription)
        parcel.writeString(carProbeg)
        parcel.writeInt(if (finish == true) 1 else 0)
        parcel.writeInt(if (finish == true) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<zaivkaClass> {
        override fun createFromParcel(parcel: Parcel): zaivkaClass {
            return zaivkaClass(parcel)
        }

        override fun newArray(size: Int): Array<zaivkaClass?> {
            return arrayOfNulls(size)
        }
    }
}

