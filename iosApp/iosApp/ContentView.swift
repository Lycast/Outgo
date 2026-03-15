import SwiftUI
import SharedApp

struct ContentView: View {
    let coordinator = IosDependencyProvider.shared.appCoordinator
    let storage = IosDependencyProvider.shared.keyValueStorage
    
    @State private var currentStep: any AppStep = AppStepDashboard.shared
    @State private var isDarkMode: Bool = false
    
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
                    presenter: IosDependencyProvider.shared.outgoingPresenter,
                    onNavigateToSettings: {
                        // 4. On passe le singleton de l'écran Settings
                        coordinator.navigateTo(step: AppStepSettings.shared)
                    }
                )
                .transition(.opacity)

            case is AppStepSettings:
                SettingsScreen(
                    isDarkMode: isDarkMode,
                    onNavigateBack: { coordinator.handleBack() },
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
                EmptyView()
            }
        }
        .animation(.easeInOut, value: stepAnimationKey)
        .preferredColorScheme(isDarkMode ? .dark : .light)
        .task {
            let systemIsDark = (systemColorScheme == .dark)
            isDarkMode = storage.getBoolean(key: themeKey, defaultValue: systemIsDark)
            
            for await navState in coordinator.state {
                self.currentStep = navState.currentStep
            }
        }
    }
    
    private func openUrl(_ urlString: String) {
        if let url = URL(string: urlString) {
            openURL(url)
        }
    }
}
