package alfatih.me.moviecatalogue.data.model.tv

data class TVResponse(
    val page: Int? = 0,
    val results: List<TVItem>? = listOf(),
    val total_pages: Int? = 0,
    val total_results: Int? = 0
)