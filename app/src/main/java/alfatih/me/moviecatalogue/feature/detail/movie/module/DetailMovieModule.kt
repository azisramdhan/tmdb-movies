package alfatih.me.moviecatalogue.feature.detail.movie.module

import alfatih.me.moviecatalogue.feature.detail.movie.DetailMovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object DetailMovieModule {
    val module: Module = module{
        viewModel {
            DetailMovieViewModel()
        }
    }
}