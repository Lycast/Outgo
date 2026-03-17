import SwiftUI
import SharedApp

struct SettingsScreen: View {
    // --- Propriétés ---
    let onNavigateBack: () -> Void
    let isDarkMode: Bool
    let onToggleDarkMode: (Bool) -> Void
    let onCoffeeClick: () -> Void
    let onTipsClick: () -> Void
    let onContactClick: () -> Void
    
    // --- État Local ---
    @State private var showSyncModal = false
    
    // --- Environnement ---
    @Environment(\.spacing) private var spacing
    @Environment(\.outgoColors) private var colors
    
    var body: some View {
        ZStack {
            // Fond global
            colors.background.ignoresSafeArea()
            
            VStack(spacing: 0) {
                // --- 1. Header ---
                Header(
                    isConnected: false, // À lier à l'Auth plus tard
                    isSettingsScreen: true,
                    onSyncIconClick: { showSyncModal = true },
                    onSyncNavigationClick: onNavigateBack
                )
                
                ScrollView {
                    VStack(spacing: spacing.large) {
                        
                        // --- SECTION 1 : Apparence ---
                        SettingsSection(title: SettingsLabels.shared.SECTION_APPEARANCE) {
                            SettingsRowToggle(
                                icon: "moon_duotone",
                                title: SettingsLabels.shared.DARK_MODE_TITLE,
                                subtitle: SettingsLabels.shared.DARK_MODE_SUBTITLE,
                                isChecked: Binding(
                                    get: { isDarkMode },
                                    set: { onToggleDarkMode($0) }
                                )
                            )
                        }
                        
                        // --- SECTION 2 : Soutien & Communauté ---
                        SettingsSection(title: SettingsLabels.shared.SECTION_SUPPORT) {
                            SettingsRowClickable(
                                icon: "lightbulb_duotone",
                                title: SettingsLabels.shared.TIPS_TITLE,
                                subtitle: SettingsLabels.shared.TIPS_SUBTITLE,
                                onClick: onTipsClick
                            )
                            
                            Divider().background(colors.onSurfaceVariant.opacity(0.2))
                            
                            SettingsRowClickable(
                                icon: "envelope_duotone",
                                title: SettingsLabels.shared.CONTACT_TITLE,
                                subtitle: SettingsLabels.shared.CONTACT_SUBTITLE,
                                onClick: onContactClick
                            )
                            
                            Divider().background(colors.onSurfaceVariant.opacity(0.2))
                            
                            SettingsRowClickable(
                                icon: "coffee_duotone",
                                title: SettingsLabels.shared.COFFEE_TITLE,
                                subtitle: SettingsLabels.shared.COFFEE_SUBTITLE,
                                onClick: {
                                    // onCoffeeClick()
                                }
                            )
                        }
                        
                        // --- SECTION 3 : Compte & Données ---
                        SettingsSection(title: SettingsLabels.shared.SECTION_DATA) {
                            SettingsRowClickable(
                                icon: "arrows_clockwise_duotone",
                                title: SettingsLabels.shared.SYNC_TITLE,
                                subtitle: SettingsLabels.shared.SYNC_SUBTITLE,
                                onClick: { showSyncModal = true }
                            )
                        }
                        
                        // Version de l'app (Footer)
                        Text(SettingsLabels.shared.APP_VERSION_PREFIX)
                            .font(.caption2)
                            .foregroundColor(colors.onSurfaceVariant)
                            .multilineTextAlignment(.center)
                            .frame(maxWidth: .infinity)
                            .padding(.top, spacing.large)
                    }
                    .padding(spacing.medium)
                }
            }
        }
        // --- 2. Modale de Promotion Sync ---
        .syncPromotionModal(isPresented: $showSyncModal) {
            showSyncModal = false
            // TODO: Navigation vers Auth
        }
    }
}

// --- Previews ---
#Preview("Mode Clair") {
    SettingsScreen(
        onNavigateBack: {},
        isDarkMode: false,
        onToggleDarkMode: { _ in },
        onCoffeeClick: {},
        onTipsClick: {},
        onContactClick: {}
    )
}
