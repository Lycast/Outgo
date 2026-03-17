import SwiftUI
import SharedApp

struct ContentView: View {
    let coordinator = IosDependencyProvider.shared.appCoordinator
    let storage = IosDependencyProvider.shared.keyValueStorage
    
    @State private var currentStep: any AppStep = AppStepDashboard.shared
    @State private var isDarkMode: Bool = false
    @State private var outgoingPresenter = IosDependencyProvider.shared.outgoingPresenter
    
    @Environment(\.openURL) private var openURL
    @Environment(\.colorScheme) private var systemColorScheme
    
    private let themeKey = "app_is_dark_mode"

    private var stepAnimationKey: String {
        String(describing: type(of: currentStep))
    }

    var body: some View {
        ZStack {
            switch currentStep {
            case is AppStepDashboard:
                DashboardScreen(
                    presenter: outgoingPresenter,
                    onNavigateToSettings: {
                        coordinator.navigateTo(step: AppStepSettings.shared)
                    }
                )
                .transition(.opacity)

            case is AppStepSettings:
                SettingsScreen(
                    onNavigateBack: { coordinator.handleBack() },
                    isDarkMode: isDarkMode,
                    onToggleDarkMode: { newValue in
                        isDarkMode = newValue
                        storage.putBoolean(key: themeKey, value: newValue)
                    },
                    onCoffeeClick: { openUrl(SettingsLabels.shared.URL_COFFEE) },
                    onTipsClick: { openUrl(SettingsLabels.shared.URL_SITE) },
                    onContactClick: { openUrl(SettingsLabels.shared.URL_CONTACT) }
                )
                .transition(.opacity)
                
            default:
                ProgressView()
            }
        }
        .animation(.easeInOut, value: stepAnimationKey)
        .preferredColorScheme(isDarkMode ? .dark : .light)
        .outgoTheme(isDark: isDarkMode)
        .task {
            let systemIsDark = (systemColorScheme == .dark)
            isDarkMode = storage.getBoolean(key: themeKey, defaultValue: systemIsDark)
            
            for await navState in coordinator.state {
                withAnimation {
                    self.currentStep = navState.currentStep
                }
            }
        }
    }
    
    private func openUrl(_ urlString: String) {
        if let url = URL(string: urlString) {
            openURL(url)
        }
    }
}
