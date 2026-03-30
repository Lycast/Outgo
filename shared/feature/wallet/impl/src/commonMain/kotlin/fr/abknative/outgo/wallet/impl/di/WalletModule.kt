package fr.abknative.outgo.wallet.impl.di

import fr.abknative.outgo.wallet.api.repository.BudgetRepository
import fr.abknative.outgo.wallet.api.repository.OutgoingRepository
import fr.abknative.outgo.wallet.api.usecase.*
import fr.abknative.outgo.wallet.impl.repository.BudgetRepositoryImpl
import fr.abknative.outgo.wallet.impl.repository.OutgoingRepositoryImpl
import fr.abknative.outgo.wallet.impl.usecase.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val walletModule = module {

    // --- Repositories (Singletons) ---
    singleOf(::OutgoingRepositoryImpl) { bind<OutgoingRepository>() }
    singleOf(::BudgetRepositoryImpl) { bind<BudgetRepository>() }

    // --- UseCases (Factories) ---
    factoryOf(::SaveOperationUseCaseImpl) { bind<SaveOperationUseCase>() }
    factoryOf(::DeleteOperationUseCaseImpl) { bind<DeleteOperationUseCase>() }
    factoryOf(::ObserveActiveOperationsUseCaseImpl) { bind<ObserveActiveOperationsUseCase>() }
    factoryOf(::CalculateTotalExpensesUseCaseImpl) { bind<CalculateTotalExpensesUseCase>() }
    factoryOf(::CalculateRemainingToPayUseCaseImpl) { bind<CalculateRemainingToPayUseCase>() }
    factoryOf(::CalculateDisposableIncomeUseCaseImpl) { bind<CalculateDisposableIncomeUseCase>() }
    factoryOf(::UpdateIncomeUseCaseImpl) { bind<UpdateIncomeUseCase>() }
}