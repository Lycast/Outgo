import SwiftUI
import SharedApp

struct SyncIconLogic: View {
    // --- Propriétés ---
    let isConnected: Bool
    let onClick: () -> Void
    
    // --- Environnement ---
    @Environment(\.outgoColors) private var colors
    
    var body: some View {
            Button(action: onClick) {
                OutgoIcon(iconName: isConnected ? "cloud_check" : "cloud_slash")
                    .frame(width: 36, height: 44, alignment: .center)
                    .contentShape(Rectangle())
                    .accessibilityLabel(isConnected ? AccessibilityLabels.shared.SYNCED : AccessibilityLabels.shared.NOT_SYNCED)
            }
        }
}

// --- Preview ---
#Preview {
    HStack(spacing: 20) {
        SyncIconLogic(isConnected: true, onClick: {})
        SyncIconLogic(isConnected: false, onClick: {})
    }
    .padding()
}
