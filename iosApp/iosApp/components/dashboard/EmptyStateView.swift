import SwiftUI
import SharedApp

struct EmptyStateView: View {
    // --- Propriétés ---
    let filter: OutgoingFilter
    
    // --- Environnement ---
    @Environment(\.outgoColors) private var colors
    @Environment(\.outgoTypography) private var typo
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
            Image(systemName: "info.circle.fill")
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: 32, height: 32)
                .foregroundColor(colors.textSecondary.opacity(0.4))
                .accessibilityLabel(AccessibilityLabels.shared.INFO_EMPTY_STATE)
            
            Spacer()
                .frame(height: spacing.extraLarge)
            
            // Message principal
            Text(message)
                .font(typo.subtitle)
                .foregroundColor(colors.textPrimary)
                .multilineTextAlignment(.center)
                .padding(.horizontal, spacing.large)
            
            Spacer()
                .frame(height: spacing.medium)
            
            // Description
            if filter == .all {
                Text(DashboardLabels.shared.EMPTY_STATE_DESC)
                    .font(typo.body)
                    .foregroundColor(colors.textSecondary)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, spacing.extraLarge)
            }
            
            Spacer()
        }
        .frame(maxWidth: .infinity)
        .padding(.vertical, spacing.extraLarge)
    }
}

// --- Previews ---
#Preview("Mode Clair") {
    EmptyStateView(filter: .all)
        .outgoTheme()
}

#Preview("Mode Sombre") {
    EmptyStateView(filter: .all)
        .outgoTheme(isDark: true)
}
