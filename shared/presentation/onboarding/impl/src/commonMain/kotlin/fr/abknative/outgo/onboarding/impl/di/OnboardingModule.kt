package fr.abknative.outgo.onboarding.impl.di

import fr.abknative.outgo.onboarding.api.OnboardingPresenter
import fr.abknative.outgo.onboarding.impl.OnboardingPresenterImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val onboardingPresentationModule = module {
    viewModelOf(::OnboardingPresenterImpl) { bind<OnboardingPresenter>() }
}