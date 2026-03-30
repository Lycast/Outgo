package fr.abknative.outgo.wallet.impl.di

import fr.abknative.outgo.wallet.api.repository.OperationRepository
import fr.abknative.outgo.wallet.api.repository.WalletRepository
import fr.abknative.outgo.wallet.api.usecase.*
import fr.abknative.outgo.wallet.impl.repository.OperationRepositoryImpl
import fr.abknative.outgo.wallet.impl.repository.WalletRepositoryImpl
import fr.abknative.outgo.wallet.impl.usecase.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val walletModule = module {

    // --- Repositories (Singletons) ---
    singleOf(::OperationRepositoryImpl) { bind<OperationRepository>() }
    singleOf(::WalletRepositoryImpl) { bind<WalletRepository>() }

    // --- UseCases Wallet ---
    factoryOf(::SaveWalletUseCaseImpl) { bind<SaveWalletUseCase>() }
    factoryOf(::DeleteWalletUseCaseImpl) { bind<DeleteWalletUseCase>() }
    factoryOf(::ObserveWalletsUseCaseImpl) { bind<ObserveWalletsUseCase>() }

    // --- UseCases Operation ---
    factoryOf(::SaveOperationUseCaseImpl) { bind<SaveOperationUseCase>() }
    factoryOf(::DeleteOperationUseCaseImpl) { bind<DeleteOperationUseCase>() }
    factoryOf(::ObserveActiveOperationsUseCaseImpl) { bind<ObserveActiveOperationsUseCase>() }

    // --- Dashboard Data ---
    factoryOf(::CalculateDashboardDataUseCaseImpl) { bind<CalculateDashboardDataUseCase>() }
}