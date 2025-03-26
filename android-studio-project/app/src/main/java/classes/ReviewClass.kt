package classes

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class reviewClass(
    val title: String? = null,
    val text: String? = null,
    val author: String? = null,
    val mark: Int? = null,
    val date: Date? = null

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(text)
        parcel.writeString(author)
        parcel.writeValue(mark)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<reviewClass> {
        override fun createFromParcel(parcel: Parcel): reviewClass {
            return reviewClass(parcel)
        }

        override fun newArray(size: Int): Array<reviewClass?> {
            return arrayOfNulls(size)
        }
    }
}
