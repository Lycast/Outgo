import SwiftUI
import SharedApp

struct SyncIconLogic: View {
    // --- Propriétés ---
    let isConnected: Bool
    let onClick: () -> Void
    
    // --- Environnement ---
    @Environment(\.outgoColors) private var colors
    @Environment(\.spacing) private var spacing
    
    var body: some View {
            Button(action: onClick) {
                Image(isConnected ? "cloud_check" : "cloud_slash")
                    .resizable()
                    .renderingMode(.template)
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 24, height: 24)
                    .padding(spacing.small)
                    .foregroundColor(colors.primary)
            }
            .accessibilityLabel(isConnected ? AccessibilityLabels.shared.SYNCED : AccessibilityLabels.shared.NOT_SYNCED)
        }
    }

    // --- Preview ---
    #Preview {
        ZStack {
            AppTheme.colorScheme(isDark: false).background.ignoresSafeArea()
            
            HStack(spacing: 20) {
                SyncIconLogic(isConnected: true, onClick: {})
                SyncIconLogic(isConnected: false, onClick: {})
            }
        }
        .outgoTheme()
    }
