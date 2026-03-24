package fr.abknative.outgo.server.api.di

import fr.abknative.outgo.server.core.repository.BudgetRepository
import fr.abknative.outgo.server.core.repository.OutgoingRepository
import fr.abknative.outgo.server.core.repository.UserRepository
import fr.abknative.outgo.server.core.usecase.GetSyncPullUseCase
import fr.abknative.outgo.server.core.usecase.ProcessSyncPushUseCase
import fr.abknative.outgo.server.data.repository.BudgetRepositoryImpl
import fr.abknative.outgo.server.data.repository.OutgoingRepositoryImpl
import fr.abknative.outgo.server.data.repository.UserRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val serverModule = module {
    // Repositories
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    singleOf(::BudgetRepositoryImpl) { bind<BudgetRepository>() }
    singleOf(::OutgoingRepositoryImpl) { bind<OutgoingRepository>() }

    // UseCases
    singleOf(::ProcessSyncPushUseCase) { bind<ProcessSyncPushUseCase>() }
    singleOf(::GetSyncPullUseCase)
}