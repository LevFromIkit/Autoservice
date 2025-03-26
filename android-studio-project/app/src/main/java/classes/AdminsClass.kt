package classes

import android.os.Parcel
import android.os.Parcelable

data class adminsClass(
    val login: String? = null,
    val name: String? = null,
    val lastname: String? = null,
    val email: String? = null,
    val phone: String? = null,
    var password: String? = null,
    val id: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(login)
        parcel.writeString(name)
        parcel.writeString(lastname)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(password)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<adminsClass> {
        override fun createFromParcel(parcel: Parcel): adminsClass {
            return adminsClass(parcel)
        }

        override fun newArray(size: Int): Array<adminsClass?> {
            return arrayOfNulls(size)
        }
    }
}
