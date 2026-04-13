package fr.abknative.outgo.month.impl.di

import fr.abknative.outgo.month.api.MonthPresenter
import fr.abknative.outgo.month.impl.MonthPresenterImpl
import fr.abknative.outgo.month.impl.MonthStateMapper
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val monthPresentationModule = module {
    factoryOf(::MonthStateMapper)
    viewModelOf(::MonthPresenterImpl) { bind<MonthPresenter>() }
}