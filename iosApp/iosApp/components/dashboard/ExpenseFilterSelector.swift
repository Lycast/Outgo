import SwiftUI
import SharedApp

struct ExpenseFilterSelector: View {
    // --- Propriétés ---
    let selectedFilter: OutgoingFilter
    let onFilterSelected: (OutgoingFilter) -> Void
    
    // --- Environnement ---
    @Environment(\.outgoColors) private var colors
    @Environment(\.spacing) private var spacing
    
    var body: some View {
        HStack(spacing: 0) {
            
            FilterTabItem(
                label: DashboardLabels.shared.TAB_ALL,
                isSelected: selectedFilter == .all,
                onClick: { onFilterSelected(.all) }
            )
            
            Spacer().frame(width: spacing.large)

            FilterTabItem(
                label: DashboardLabels.shared.TAB_PAID,
                isSelected: selectedFilter == .paid,
                onClick: { onFilterSelected(.paid) }
            )
            
            Spacer().frame(width: spacing.large)

            FilterTabItem(
                label: DashboardLabels.shared.TAB_REMAINING,
                isSelected: selectedFilter == .remaining,
                onClick: { onFilterSelected(.remaining) }
            )
        }
        .padding(.horizontal, spacing.large)
        .frame(maxWidth: .infinity)
        .sensoryFeedback(.selection, trigger: selectedFilter)
    }
}

// --- Sous-composant ---
private struct FilterTabItem: View {
    let label: String
    let isSelected: Bool
    let onClick: () -> Void
    
    @Environment(\.outgoColors) private var colors
    @Environment(\.outgoTypography) private var typo
    @Environment(\.spacing) private var spacing
    
    var body: some View {
        Button(action: {
            withAnimation(.spring(response: 0.3, dampingFraction: 0.7)) {
                onClick()
            }
        }) {
            VStack(alignment: .center, spacing: 4) {
                Text(label.uppercased())
                    .font(typo.body)
                    .fontWeight(isSelected ? .medium : .regular)
                    .foregroundColor(isSelected ? colors.primary : colors.textSecondary)
                    .padding(.vertical, spacing.small)
                
                RoundedRectangle(cornerRadius: 50)
                    .fill(isSelected ? colors.primary : Color.clear)
                    .frame(height: 3)
                    .frame(maxWidth: .infinity)
                    .opacity(isSelected ? 1 : 0)
                    .scaleEffect(x: isSelected ? 1 : 0.4, y: 1)
            }
        }
        .buttonStyle(.plain)
        .frame(maxWidth: .infinity)
        .accessibilityAddTraits(isSelected ? .isSelected : [])
    }
}

// --- Preview ---
#Preview {
    struct PreviewWrapper: View {
        @State var filter: OutgoingFilter = .all
        var body: some View {
            ZStack {
                AppTheme.colorScheme(isDark: false).background.ignoresSafeArea()
                
                VStack {
                    ExpenseFilterSelector(
                        selectedFilter: filter,
                        onFilterSelected: { filter = $0 }
                    )
                    Spacer()
                }
                .padding(.top, 50)
            }
            .outgoTheme()
        }
    }
    return PreviewWrapper()
}
