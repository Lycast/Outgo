package fr.abknative.outgo.android.di

import fr.abknative.outgo.android.outgoing.OutgoingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val androidAppModule = module {
    viewModelOf(::OutgoingViewModel)
}