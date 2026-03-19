import SwiftUI
import SharedApp

struct Header: View {
    // --- Propriétés ---
    let isConnected: Bool
    var isSettingsScreen: Bool = false
    let onSyncIconClick: () -> Void
    let onSyncNavigationClick: () -> Void
    
    // --- Environnement ---
    @Environment(\.spacing) private var spacing
    @Environment(\.outgoColors) private var colors
    @Environment(\.outgoTypography) private var typo
    
    var body: some View {
        HStack(alignment: .center) {
            // --- Titre (Gauche) ---
            Text(CommonLabels.shared.APP_NAME)
                .font(typo.title)
                .foregroundColor(colors.primary)
            
            Spacer()
            
            // --- Actions (Droite) ---
            HStack() {
                
                // Icône Cloud
                SyncIconLogic(
                    isConnected: isConnected,
                    onClick: onSyncIconClick
                )
                
                // Icône Navigation (Settings <-> Home)
                Button(action: onSyncNavigationClick) {
                    Image(isSettingsScreen ? "house_line" : "gear_six")
                        .resizable()
                        .renderingMode(.template)
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 24, height: 24)
                        .padding(spacing.small)
                        .foregroundColor(colors.primary)
                }
                .accessibilityLabel(isSettingsScreen ? AccessibilityLabels.shared.NAVIGATE_HOME : AccessibilityLabels.shared.NAVIGATE_SETTINGS)
            }
        }
        .padding(.vertical, spacing.medium)
        .padding(.horizontal, spacing.large)
        .frame(maxWidth: .infinity)
    }
}

// --- Preview ---
#Preview("Light Mode") {
    Header(
        isConnected: true,
        isSettingsScreen: false,
        onSyncIconClick: {},
        onSyncNavigationClick: {}
    )
}

#Preview("Dark Mode") {
    Header(
        isConnected: false,
        isSettingsScreen: true,
        onSyncIconClick: {},
        onSyncNavigationClick: {}
    )
    .preferredColorScheme(.dark)
}
