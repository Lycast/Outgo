package fr.abknative.outgo.wallet.impl.di

import fr.abknative.outgo.core.api.DataPurger
import fr.abknative.outgo.core.api.LocalDataDowngrader
import fr.abknative.outgo.core.api.LocalDataMigrator
import fr.abknative.outgo.wallet.api.repository.OperationRepository
import fr.abknative.outgo.wallet.api.repository.WalletRepository
import fr.abknative.outgo.wallet.api.usecase.*
import fr.abknative.outgo.wallet.impl.WalletDataDowngrader
import fr.abknative.outgo.wallet.impl.WalletDataPurger
import fr.abknative.outgo.wallet.impl.migration.LocalDataMigratorImpl
import fr.abknative.outgo.wallet.impl.repository.OperationRepositoryImpl
import fr.abknative.outgo.wallet.impl.repository.WalletRepositoryImpl
import fr.abknative.outgo.wallet.impl.usecase.*
import fr.abknative.outgo.wallet.impl.usecase.engine.SimpleSumEngine
import fr.abknative.outgo.wallet.impl.usecase.engine.TimelineEngine
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val walletModule = module {

    // --- Repositories (Singletons) ---
    singleOf(::OperationRepositoryImpl) { bind<OperationRepository>() }
    singleOf(::WalletRepositoryImpl) { bind<WalletRepository>() }

    // --- Engines (Stratégies) ---
    factoryOf(::SimpleSumEngine)
    factoryOf(::TimelineEngine)

    // --- UseCases Wallet ---
    factoryOf(::SaveWalletUseCaseImpl) { bind<SaveWalletUseCase>() }
    factoryOf(::DeleteWalletUseCaseImpl) { bind<DeleteWalletUseCase>() }
    factoryOf(::ObserveWalletsUseCaseImpl) { bind<ObserveWalletsUseCase>() }

    // --- UseCases Operation ---
    factoryOf(::SaveOperationUseCaseImpl) { bind<SaveOperationUseCase>() }
    factoryOf(::DeleteOperationUseCaseImpl) { bind<DeleteOperationUseCase>() }
    factoryOf(::ObserveActiveOperationsUseCaseImpl) { bind<ObserveActiveOperationsUseCase>() }

    // --- Dashboard Engine Rooter ---
    factoryOf(::CalculateDashboardDataUseCaseImpl) { bind<CalculateDashboardDataUseCase>() }

    singleOf(::WalletDataPurger) { bind<DataPurger>() }
    singleOf(::WalletDataDowngrader) { bind<LocalDataDowngrader>() }
    singleOf(::LocalDataMigratorImpl) { bind<LocalDataMigrator>() }
}