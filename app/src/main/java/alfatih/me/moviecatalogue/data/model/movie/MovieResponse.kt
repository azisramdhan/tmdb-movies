package alfatih.me.moviecatalogue.data.model.movie

data class MovieResponse(
    val page: Int? = 0,
    val results: List<MovieItem> = listOf(),
    val total_pages: Int? = 0,
    val total_results: Int? = 0
)