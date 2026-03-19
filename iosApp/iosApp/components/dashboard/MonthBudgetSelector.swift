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
    @Environment(\.outgoTypography) private var typo
    
    var body: some View {
        HStack {
            Spacer()
            
            // Bouton Précédent
            Button(action: onPreviousMonthClick) {
                Image("caret_left")
                    .renderingMode(.template)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 24, height: 24)
                    .foregroundColor(colors.primary)
                    .padding(spacing.small)
            }
            .accessibilityLabel(AccessibilityLabels.shared.PREVIOUS_MONTH)
            
            Spacer()
            
            // --- Titre du Mois ---
            Text(formattedMonthDate.uppercased())
                .font(typo.subtitle)
                .fontWeight(.bold)
                .foregroundColor(colors.textPrimary)
            
            Spacer()
            
            // Bouton Suivant
            Button(action: onNextMonthClick) {
                Image("caret_right")
                    .renderingMode(.template)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 24, height: 24)
                    .foregroundColor(colors.primary)
                    .padding(spacing.small)
            }
            .accessibilityLabel(AccessibilityLabels.shared.NEXT_MONTH)
            
            Spacer()
        }
        .padding(.horizontal, spacing.large)
        .frame(maxWidth: .infinity)
        .frame(height: 56)
    }
}

// --- Preview ---
#Preview {
    MonthBudgetSelector(
        formattedMonthDate: "Mars 2026",
        onPreviousMonthClick: {},
        onNextMonthClick: {}
    )
    .outgoTheme()
}
