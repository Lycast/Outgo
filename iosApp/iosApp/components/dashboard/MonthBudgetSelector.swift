import SwiftUI
import SharedApp

struct MonthBudgetSelector: View {
    // --- Propriétés ---
    let formattedMonthDate: String
    let onPreviousMonthClick: () -> Void
    let onNextMonthClick: () -> Void
    
    // --- Environnement ---
    @Environment(\.outgoColors) private var colors
    @Environment(\.spacing) private var spacing
    
    var body: some View {
        HStack {
            // Bouton Précédent
            Button(action: onPreviousMonthClick) {
                Image("caret_left_bold")
                    .renderingMode(.template)
                    .font(.system(size: 20, weight: .medium))
                    .foregroundColor(colors.primary)
                    .padding(spacing.small)
            }
            .accessibilityLabel(AccessibilityLabels.shared.PREVIOUS_MONTH)
            
            Spacer()
            
            Text(formattedMonthDate.uppercased())
                .font(.body)
                .fontWeight(.semibold)
                .foregroundColor(colors.onSurface)
                .padding(spacing.large)
            
            Spacer()
            
            // Bouton Suivant
            Button(action: onNextMonthClick) {
                Image("caret_right_bold")
                    .renderingMode(.template)
                    .font(.system(size: 20, weight: .medium))
                    .foregroundColor(colors.primary)
                    .padding(spacing.small)
            }
            .accessibilityLabel(AccessibilityLabels.shared.NEXT_MONTH)
        }
        .padding(.horizontal, spacing.large)
        .frame(maxWidth: .infinity)
    }
}

// --- Preview ---
#Preview {
    MonthBudgetSelector(
        formattedMonthDate: "Mars 2026",
        onPreviousMonthClick: {},
        onNextMonthClick: {}
    )
}
