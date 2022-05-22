package alfatih.me.moviecatalogue.feature.list.content

import alfatih.me.moviecatalogue.util.getOrAwaitValue
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class ListViewModelTest : TestCase() {

    private lateinit var viewModel: ListViewModel

    @Before
    override fun setUp() {
        viewModel = ListViewModel()
    }

    fun testSearchMovie() {
        val query = "Naruto"
        viewModel.searchMovie(query)
        assertNotNull(viewModel.movieList)
        assertEquals(0, viewModel.movieList.value)
    }

    fun testSearchTVShow() {}

    @Test
    fun testFetchData() {
        viewModel.fetchData()
        assertNotNull(viewModel.movieList.getOrAwaitValue())
        assertNotNull(viewModel.tvShowList.getOrAwaitValue())
    }

    fun testFetchLocalDataMovie() {}

    fun testFetchLocalDataTV() {}

    fun testCancelAllRequests() {}
}