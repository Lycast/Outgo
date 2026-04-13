package fr.abknative.outgo.list.impl.di

import fr.abknative.outgo.list.api.ListPresenter
import fr.abknative.outgo.list.impl.ListPresenterImpl
import fr.abknative.outgo.list.impl.ListStateMapper
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val listPresentationModule = module {
    factoryOf(::ListStateMapper)
    viewModelOf(::ListPresenterImpl) { bind<ListPresenter>() }
}