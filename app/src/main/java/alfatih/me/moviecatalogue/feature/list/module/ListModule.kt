package alfatih.me.moviecatalogue.feature.list.module

import alfatih.me.moviecatalogue.feature.list.content.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object ListModule {
    val module: Module = module {
        viewModel {
            ListViewModel()
        }
    }
}