import SwiftUI
import SharedApp

struct PairedBudgetBar: View {
    // --- Données du Haut ---
    let topLabel: String
    let topAmount: String
    let topProgress: Float
    let topBarColor: Color
    
    // --- Données du Bas ---
    let bottomLabel: String
    let bottomAmount: String
    let bottomProgress: Float
    let bottomBarColor: Color
    
    @Environment(\.spacing) private var spacing
    @Environment(\.outgoColors) private var colors

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            
            // --- TEXTE DU HAUT ---
            HStack(alignment: .bottom) {
                Text(topLabel)
                    .font(.caption)
                    .foregroundColor(colors.onSurface)
                
                Spacer()
                
                Text(topAmount)
                    .font(.body)
                    .foregroundColor(colors.onSurface)
            }
            
            // --- LES BARRES (Sandwich) ---
            VStack(spacing: 1) {
                
                // Barre du Haut (Coins arrondis supérieurs)
                ProgressBarItem(
                    progress: topProgress,
                    color: topBarColor,
                    topRadius: 2,
                    bottomRadius: 0
                )
                
                // Barre du Bas (Coins arrondis inférieurs)
                ProgressBarItem(
                    progress: bottomProgress,
                    color: bottomBarColor,
                    topRadius: 0,
                    bottomRadius: 2
                )
            }
            .padding(.vertical, spacing.small)
            
            // --- TEXTE DU BAS (Dans le Tooltip) ---
            InfoTooltip(
                title: DashboardLabels.shared.TOOLTIP_BALANCE_DUE_TITLE,
                description: DashboardLabels.shared.TOOLTIP_BALANCE_DUE_DESC
            ) {
                HStack(alignment: .top) {
                    Text(bottomLabel)
                        .font(.caption)
                        .foregroundColor(colors.onSurface)
                    
                    Spacer()
                    
                    Text(bottomAmount)
                        .font(.body)
                        .fontWeight(.medium)
                        .foregroundColor(colors.onSurface)
                }
            }
        }
        .padding(.horizontal, spacing.medium)
        .frame(maxWidth: .infinity)
    }
}

// --- Sous-composant privé pour la barre ---
private struct ProgressBarItem: View {
    let progress: Float
    let color: Color
    let topRadius: CGFloat
    let bottomRadius: CGFloat
    
    @Environment(\.outgoColors) private var colors

    var body: some View {
        GeometryReader { geo in
            ZStack(alignment: .leading) {
                UnevenRoundedRectangle(
                    topLeadingRadius: topRadius,
                    bottomLeadingRadius: bottomRadius,
                    bottomTrailingRadius: bottomRadius,
                    topTrailingRadius: topRadius
                )
                .fill(colors.onSurface.opacity(0.1))
                
                UnevenRoundedRectangle(
                    topLeadingRadius: topRadius,
                    bottomLeadingRadius: bottomRadius,
                    bottomTrailingRadius: bottomRadius,
                    topTrailingRadius: topRadius
                )
                .fill(color)
                .frame(width: geo.size.width * CGFloat(max(0, min(progress, 1))))
            }
        }
        .frame(height: 4)
    }
}

// --- Previews ---
#Preview("Paired Bar - Nominal") {
    PairedBudgetBar(
        topLabel: "TOTAL DES CHARGES",
        topAmount: "1 250,50 €",
        topProgress: 0.7,
        topBarColor: .purple,
        bottomLabel: "RESTE À PAYER",
        bottomAmount: "320,00 €",
        bottomProgress: 0.25,
        bottomBarColor: .orange
    )
    .padding()
}
