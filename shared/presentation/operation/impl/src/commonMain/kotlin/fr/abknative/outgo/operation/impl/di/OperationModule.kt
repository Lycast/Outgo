package fr.abknative.outgo.operation.impl.di

import fr.abknative.outgo.operation.api.OperationPresenter
import fr.abknative.outgo.operation.impl.OperationPresenterImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val operationPresentationModule = module {
    viewModelOf(::OperationPresenterImpl) { bind<OperationPresenter>() }
}