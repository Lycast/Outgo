package fr.abknative.outgo.server.api.di

import fr.abknative.outgo.server.core.repository.GarbageCollectorRepository
import fr.abknative.outgo.server.core.repository.OperationRepository
import fr.abknative.outgo.server.core.repository.TransactionRunner
import fr.abknative.outgo.server.core.repository.UserRepository
import fr.abknative.outgo.server.core.repository.WalletRepository
import fr.abknative.outgo.server.core.usecase.GetSyncPullUseCase
import fr.abknative.outgo.server.core.usecase.ProcessSyncPushUseCase
import fr.abknative.outgo.server.data.repository.ExposedTransactionRunner
import fr.abknative.outgo.server.data.repository.GarbageCollectorRepositoryImpl
import fr.abknative.outgo.server.data.repository.OperationRepositoryImpl
import fr.abknative.outgo.server.data.repository.UserRepositoryImpl
import fr.abknative.outgo.server.data.repository.WalletRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val serverModule = module {
    // Repositories
    singleOf(::GarbageCollectorRepositoryImpl) { bind<GarbageCollectorRepository>() }
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    singleOf(::WalletRepositoryImpl) { bind<WalletRepository>() }
    singleOf(::OperationRepositoryImpl) { bind<OperationRepository>() }
    singleOf(::ExposedTransactionRunner) { bind<TransactionRunner>() }

    // UseCases
    singleOf(::ProcessSyncPushUseCase) { bind<ProcessSyncPushUseCase>() }
    singleOf(::GetSyncPullUseCase)
}