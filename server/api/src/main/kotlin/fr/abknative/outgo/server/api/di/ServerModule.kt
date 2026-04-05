package fr.abknative.outgo.server.api.di

import fr.abknative.outgo.server.core.repository.*
import fr.abknative.outgo.server.core.usecase.DeleteUserAccountUseCase
import fr.abknative.outgo.server.core.usecase.GetSyncPullUseCase
import fr.abknative.outgo.server.core.usecase.ProcessSyncPushUseCase
import fr.abknative.outgo.server.data.repository.*
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
    singleOf(::DeleteUserAccountUseCase)
}