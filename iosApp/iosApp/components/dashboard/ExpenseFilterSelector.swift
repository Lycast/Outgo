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
        HStack(spacing: spacing.small) {
            FilterTabItem(
                label: DashboardLabels.shared.TAB_ALL,
                isSelected: selectedFilter == .all,
                onClick: { onFilterSelected(.all) }
            )
            
            FilterTabItem(
                label: DashboardLabels.shared.TAB_PAID,
                isSelected: selectedFilter == .paid,
                onClick: { onFilterSelected(.paid) }
            )
            
            FilterTabItem(
                label: DashboardLabels.shared.TAB_REMAINING,
                isSelected: selectedFilter == .remaining,
                onClick: { onFilterSelected(.remaining) }
            )
        }
        .padding(spacing.small)
        .background(colors.surface)
        .cornerRadius(12) // Material Medium Shape
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(colors.onSurfaceVariant.opacity(0.2), lineWidth: 1)
        )
        .padding(.horizontal, spacing.medium)
    }
}

// --- Sous-composant pour l'item de filtre ---
private struct FilterTabItem: View {
    let label: String
    let isSelected: Bool
    let onClick: () -> Void
    
    @Environment(\.outgoColors) private var colors
    
    var body: some View {
        Button(action: onClick) {
            Text(label)
                .font(.caption)
                .fontWeight(isSelected ? .bold : .regular)
                .foregroundColor(colors.primary)
                .frame(height: 36)
                .frame(maxWidth: .infinity) // Équivalent de weight(1f)
                .background(isSelected ? colors.primary.opacity(0.1) : Color.clear)
                .cornerRadius(8)
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(isSelected ? colors.primary : Color.clear, lineWidth: 1)
                )
        }
        .buttonStyle(.plain)
    }
}

// --- Preview ---
#Preview {
    struct PreviewWrapper: View {
        @State var filter: OutgoingFilter = .all
        var body: some View {
            VStack {
                ExpenseFilterSelector(
                    selectedFilter: filter,
                    onFilterSelected: { filter = $0 }
                )
            }
            .frame(maxHeight: .infinity)
            .background(Color.gray.opacity(0.1))
        }
    }
    return PreviewWrapper()
}
