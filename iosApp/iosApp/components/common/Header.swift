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
    
    var body: some View {
        HStack(alignment: .center) {
            // --- Titre (Gauche) ---
            Text(CommonLabels.shared.APP_NAME)
                .font(.system(.title, design: .rounded))
                .fontWeight(.black)
                .foregroundColor(colors.primary)
                .padding(.bottom, spacing.small)
            
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
                    OutgoIcon(iconName: isSettingsScreen ? "house_line" : "gear_six")
                        .frame(width: 36, height: 44, alignment: .center)
                        .contentShape(Rectangle())
                        .foregroundColor(.primary)
                }
                .accessibilityLabel(isSettingsScreen ? AccessibilityLabels.shared.NAVIGATE_HOME : AccessibilityLabels.shared.NAVIGATE_SETTINGS)
            }
        }
        .padding(.top, spacing.extraLarge)
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
