import SwiftUI
import SharedApp

struct EmptyStateView: View {
    // --- Propriétés ---
    let filter: OutgoingFilter
    
    // --- Environnement ---
    @Environment(\.outgoColors) private var colors
    @Environment(\.spacing) private var spacing

    // --- Logique du message ---
    private var message: String {
        switch filter {
        case .all:
            return DashboardLabels.shared.EMPTY_ALL
        case .paid:
            return DashboardLabels.shared.EMPTY_PAID
        case .remaining:
            return DashboardLabels.shared.EMPTY_REMAINING
        }
    }

    var body: some View {
        VStack(alignment: .center, spacing: 0) {
            Spacer()
                .frame(height: spacing.large)
            
            // Icône Info
            Image(systemName: "info.circle")
                .font(.system(size: 32))
                .foregroundColor(colors.onSurfaceVariant.opacity(0.5))
                .accessibilityLabel(AccessibilityLabels.shared.INFO_EMPTY_STATE)
            
            Spacer()
                .frame(height: spacing.extraLarge)
            
            // Message principal
            Text(message)
                .font(.headline)
                .foregroundColor(colors.onSurface)
                .multilineTextAlignment(.center)
            
            Spacer()
                .frame(height: spacing.medium)
            
            // Description subsidiaire (uniquement pour le filtre ALL)
            if filter == .all {
                Text(DashboardLabels.shared.EMPTY_STATE_DESC)
                    .font(.body)
                    .foregroundColor(colors.onSurfaceVariant)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, spacing.large)
            }
            
            Spacer()
        }
        .frame(maxWidth: .infinity)
        .padding(.vertical, spacing.extraLarge)
    }
}

// --- Preview ---
#Preview("All Empty") {
    EmptyStateView(filter: .all)
}

#Preview("Paid Empty") {
    EmptyStateView(filter: .paid)
}
