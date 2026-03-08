package fr.abknative.outgo.outgoing.impl.di

import fr.abknative.outgo.outgoing.api.presenter.OutgoingPresenter
import fr.abknative.outgo.outgoing.api.repository.OutgoingRepository
import fr.abknative.outgo.outgoing.api.usecase.*
import fr.abknative.outgo.outgoing.impl.presenter.OutgoingPresenterImpl
import fr.abknative.outgo.outgoing.impl.repository.OutgoingRepositoryImpl
import fr.abknative.outgo.outgoing.impl.usecase.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun outgoingModule() = module {

    singleOf(::OutgoingRepositoryImpl) { bind<OutgoingRepository>() }

    factoryOf(::SaveOutgoingUseCaseImpl) { bind<SaveOutgoingUseCase>() }
    factoryOf(::DeleteOutgoingUseCaseImpl) { bind<DeleteOutgoingUseCase>() }
    factoryOf(::ObserveActiveOutgoingsUseCaseImpl) { bind<ObserveActiveOutgoingsUseCase>() }
    factoryOf(::CalculateMonthlyTotalUseCaseImpl) { bind<CalculateMonthlyTotalUseCase>() }
    factoryOf(::CalculateRemainingToPayThisMonthUseCaseImpl) { bind<CalculateRemainingToPayThisMonthUseCase>() }

    viewModelOf(::OutgoingPresenterImpl) { bind<OutgoingPresenter>() }
}