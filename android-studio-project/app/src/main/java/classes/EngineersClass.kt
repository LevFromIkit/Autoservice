package classes

import android.os.Parcel
import android.os.Parcelable

class engineersClass (
    val login: String? = null,
    val name: String? = null,
    val lastname: String? = null,
    val speciality: String? = null,
    val email: String? = null,
    val phone: String? = null,
    var password: String? = null,
    val id: String? = null,
    val faild: Boolean = false

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt() == 1,
        ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(login)
        parcel.writeString(name)
        parcel.writeString(lastname)
        parcel.writeString(speciality)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(password)
        parcel.writeString(id)
        parcel.writeInt(if (faild == true) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<engineersClass> {
        override fun createFromParcel(parcel: Parcel): engineersClass {
            return engineersClass(parcel)
        }

        override fun newArray(size: Int): Array<engineersClass?> {
            return arrayOfNulls(size)
        }
    }
}