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
import fr.abknative.outgo.wallet.impl.usecase.engine.SimplePeriodStatsCalculation
import fr.abknative.outgo.wallet.impl.usecase.engine.TimelinePeriodStatsCalculation
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val walletModule = module {

    // --- Repositories (Singletons) ---
    singleOf(::OperationRepositoryImpl) { bind<OperationRepository>() }
    singleOf(::WalletRepositoryImpl) { bind<WalletRepository>() }

    // --- Engines (Stratégies) ---
    factoryOf(::SimplePeriodStatsCalculation)
    factoryOf(::TimelinePeriodStatsCalculation)

    // --- UseCases Wallet ---
    factoryOf(::SaveWalletUseCaseImpl) { bind<SaveWalletUseCase>() }
    factoryOf(::DeleteWalletUseCaseImpl) { bind<DeleteWalletUseCase>() }
    factoryOf(::ObserveWalletsUseCaseImpl) { bind<ObserveWalletsUseCase>() }

    // --- UseCases Operation ---
    factoryOf(::SaveOperationUseCaseImpl) { bind<SaveOperationUseCase>() }
    factoryOf(::DeleteOperationUseCaseImpl) { bind<DeleteOperationUseCase>() }
    factoryOf(::ObserveProjectedOperationsUseCaseImpl) { bind<ObserveProjectedOperationsUseCase>() }
    factoryOf(::ObserveStandardOperationsUseCaseImpl) { bind<ObserveStandardOperationsUseCase>() }

    // --- UseCases Dashboard ---
    factoryOf(::InitializeBudgetUseCaseImpl) { bind<InitializeBudgetUseCase>() }

    // --- Dashboard Engine Rooter ---
    factoryOf(::CalculatePeriodStatsUseCaseImpl) { bind<CalculatePeriodStatsUseCase>() }

    single(named("WalletDataPurger")) { WalletDataPurger(get(), get()) } bind DataPurger::class
    singleOf(::WalletDataDowngrader) { bind<LocalDataDowngrader>() }
    singleOf(::LocalDataMigratorImpl) { bind<LocalDataMigrator>() }
}