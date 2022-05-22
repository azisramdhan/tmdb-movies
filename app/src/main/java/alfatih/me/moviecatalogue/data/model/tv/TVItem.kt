package alfatih.me.moviecatalogue.data.model.tv

import android.os.Parcel
import android.os.Parcelable

data class TVItem(
    val backdrop_path: String? = "",
    val first_air_date: String? = "",
    val genre_ids: List<Int?>? = listOf(),
    val id: Int? = 0,
    val name: String? = "",
    val origin_country: List<String?>? = listOf(),
    val original_language: String? = "",
    val original_name: String? = "",
    val overview: String? = "",
    val popularity: Double? = 0.0,
    val poster_path: String? = "",
    val vote_average: Double? = 0.0,
    val vote_count: Int? = 0
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        ArrayList<Int?>().apply { source.readList(this as List<*>, Int::class.java.classLoader) },
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString(),
        ArrayList<String?>().apply { source.readList(this as List<*>, String::class.java.classLoader) },
        source.readString(),
        source.readString(),
        source.readString(),
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readString(),
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(backdrop_path)
        writeString(first_air_date)
        writeList(genre_ids)
        writeValue(id)
        writeString(name)
        writeList(origin_country)
        writeString(original_language)
        writeString(original_name)
        writeString(overview)
        writeValue(popularity)
        writeString(poster_path)
        writeValue(vote_average)
        writeValue(vote_count)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TVItem> = object : Parcelable.Creator<TVItem> {
            override fun createFromParcel(source: Parcel): TVItem = TVItem(source)
            override fun newArray(size: Int): Array<TVItem?> = arrayOfNulls(size)
        }
    }
}