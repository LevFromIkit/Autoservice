package classes

import android.os.Parcel
import android.os.Parcelable

data class serviceClass(
    val id: String? = null,
    val name: String? = null,
    val duration: Int? = null,
    val speciliality: String? = null,
    val cost: Int? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeInt(duration ?: 0)
        parcel.writeString(speciliality)
        parcel.writeInt(cost ?: 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<serviceClass> {
        override fun createFromParcel(parcel: Parcel): serviceClass {
            return serviceClass(parcel)
        }

        override fun newArray(size: Int): Array<serviceClass?> {
            return arrayOfNulls(size)
        }
    }
}
