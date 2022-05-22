package alfatih.me.moviecatalogue.feature.detail.tv.module

import alfatih.me.moviecatalogue.feature.detail.tv.DetailTVShowViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object DetailTVShowModule {
    val module: Module = module{
        viewModel {
            DetailTVShowViewModel()
        }
    }
}