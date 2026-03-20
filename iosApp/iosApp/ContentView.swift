import SharedApp
import SwiftUI

struct ContentView: View {
    let coordinator = IosDependencyProvider.shared.appCoordinator
    let storage = IosDependencyProvider.shared.keyValueStorage

    // --- ÉTATS ---
    @State private var resourcesLoaded: Bool = false
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
            if !resourcesLoaded {
                // Écran de transition (Splash Screen)
                VStack(spacing: 20) {
                    ProgressView()
                        .tint(isDarkMode ? .white : .black)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(isDarkMode ? Color.black : Color.white)
                .transition(.opacity)
            } else {
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
        }
        .animation(.easeInOut, value: stepAnimationKey)
        .preferredColorScheme(isDarkMode ? .dark : .light)
        .outgoTheme(isDark: isDarkMode)
        .task {
            // CHARGEMENT DES RESSOURCES (Asynchrone et Parallèle)
            if !resourcesLoaded {
                do {
                    let resources = try await IosStringsResourcesLoader.shared.loadAll()
                    StringsCache.shared.resources = resources
                    withAnimation {
                        self.resourcesLoaded = true
                    }
                } catch {
                    print("Erreur critique de chargement des labels: \(error)")
                }
            }

            // LOGIQUE(Theme & Navigation)
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
