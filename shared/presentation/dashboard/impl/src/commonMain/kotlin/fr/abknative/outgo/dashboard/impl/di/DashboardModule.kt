package fr.abknative.outgo.dashboard.impl.di

import fr.abknative.outgo.dashboard.api.DashboardPresenter
import fr.abknative.outgo.dashboard.impl.DashboardPresenterImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dashboardPresentationModule = module {
    viewModelOf(::DashboardPresenterImpl) { bind<DashboardPresenter>() }
}