package alfatih.me.moviecatalogue.data.model.movie

import android.os.Parcel
import android.os.Parcelable

data class MovieItem(
    val adult: Boolean? = false,
    val backdrop_path: String? = "",
    val genre_ids: List<Int?>? = listOf(),
    val id: Int? = 0,
    val original_language: String? = "",
    val original_title: String? = "",
    val overview: String? = "",
    val popularity: Double? = 0.0,
    val poster_path: String? = "",
    val release_date: String? = "",
    val title: String? = "",
    val video: Boolean? = false,
    val vote_average: Double? = 0.0,
    val vote_count: Int? = 0,
    val base64_image: String? = ""
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readValue(Boolean::class.java.classLoader) as Boolean?,
        source.readString(),
        ArrayList<Int?>().apply { source.readList(this as List<*>, Int::class.java.classLoader) },
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString(),
        source.readString(),
        source.readString(),
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readString(),
        source.readString(),
        source.readString(),
        source.readValue(Boolean::class.java.classLoader) as Boolean?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(adult)
        writeString(backdrop_path)
        writeList(genre_ids)
        writeValue(id)
        writeString(original_language)
        writeString(original_title)
        writeString(overview)
        writeValue(popularity)
        writeString(poster_path)
        writeString(release_date)
        writeString(title)
        writeValue(video)
        writeValue(vote_average)
        writeValue(vote_count)
        writeValue(base64_image)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MovieItem> = object : Parcelable.Creator<MovieItem> {
            override fun createFromParcel(source: Parcel): MovieItem = MovieItem(source)
            override fun newArray(size: Int): Array<MovieItem?> = arrayOfNulls(size)
        }
    }
}