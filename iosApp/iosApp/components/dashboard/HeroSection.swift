import SwiftUI
import SharedApp

struct HeroSection: View {
    // --- Propriétés ---
    let isExpanded: Bool
    let formattedMonthDate: String
    let monthlyIncomeInCents: Int64
    let totalOutgoingsInCents: Int64
    let disposableIncomeInCents: Int64
    let remainingToPayInCents: Int64
    
    let onToggleExpand: () -> Void
    let onEditBudgetClick: () -> Void
    let onPreviousMonthClick: () -> Void
    let onNextMonthClick: () -> Void

    // --- Environnement ---
    @Environment(\.outgoColors) private var colors
    @Environment(\.spacing) private var spacing
    @Environment(\.dashboardColors) private var dashColors

    // --- Logique Interne ---
    private var maxValue: Float {
        Float(max(max(monthlyIncomeInCents, totalOutgoingsInCents), 1))
    }
    
    private var isNegativeLive: Bool { disposableIncomeInCents < 0 }
    
    private var liveColor: Color {
        isNegativeLive ? colors.error : dashColors.remainingLive
    }

    private var isTooLong: Bool {
        let totalLength = monthlyIncomeInCents.uiAmount.count + disposableIncomeInCents.uiAmount.count
        return totalLength > 22
    }

    var body: some View {
        VStack(spacing: 0) {
            
            // --- Header du sélecteur de mois ---
            MonthBudgetSelector(
                formattedMonthDate: formattedMonthDate,
                onPreviousMonthClick: onPreviousMonthClick,
                onNextMonthClick: onNextMonthClick
            )

            Divider()
                .background(colors.onSurfaceVariant.opacity(0.2))
                .padding(.horizontal, spacing.large)

            // --- Contenu Dépliable ---
            VStack(spacing: spacing.extraLarge) {
                Spacer().frame(height: spacing.small)

                Group {
                    if isTooLong {
                        VStack(spacing: spacing.medium) { contentItems }
                    } else {
                        HStack {
                            Spacer()
                            contentItems
                            Spacer()
                        }
                    }
                }

                Divider().background(colors.onSurfaceVariant.opacity(0.2))

                // Barres de progression
                PairedBudgetBar(
                    topLabel: DashboardLabels.shared.HERO_TOTAL_CHARGES_LABEL,
                    topAmount: totalOutgoingsInCents.uiAmount,
                    topProgress: Float(totalOutgoingsInCents) / maxValue,
                    topBarColor: colors.tertiary,
                    
                    bottomLabel: DashboardLabels.shared.HERO_REMAINING_TO_PAY_LABEL,
                    bottomAmount: remainingToPayInCents.uiAmount,
                    bottomProgress: Float(remainingToPayInCents) / maxValue,
                    bottomBarColor: colors.secondary
                )
            }
            .padding(.horizontal, spacing.large)
            .opacity(isExpanded ? 1 : 0)
            .fixedSize(horizontal: false, vertical: true)
            .frame(height: isExpanded ? nil : 0, alignment: .top)
            .clipped()

            // --- Bouton Toggle ---
            Button(action: onToggleExpand) {
                OutgoIcon(iconName: isExpanded ? "caret_up" : "caret_down", opacity: 0.5)
            }
        }
        .background(colors.surface)
        .cornerRadius(12)
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(colors.onSurfaceVariant.opacity(0.2), lineWidth: 1)
        )
        .padding(.horizontal, spacing.medium)
        //.padding(.top, spacing.small)
    }

    @ViewBuilder
    private var contentItems: some View {
        BudgetItem(amount: monthlyIncomeInCents.uiAmount, onClick: onEditBudgetClick)
        
        if !isTooLong { Spacer() }
        
        LiveItem(
            amount: disposableIncomeInCents.uiAmount,
            color: liveColor,
            fontWeight: isNegativeLive ? .medium : .bold
        )
    }
}

// --- Sous-Composants Privés ---
private struct BudgetItem: View {
    let amount: String
    let onClick: () -> Void
    @Environment(\.spacing) var spacing
    
    var body: some View {
        Button(action: onClick) {
            HStack(spacing: spacing.medium) {
                CircleIcon(iconName: "bank_duotone")
                Text(amount)
                    .font(.body)
                    .fontWeight(.semibold)
                    .foregroundColor(.primary)
            }
            .contentShape(Rectangle())
        }
        .buttonStyle(.plain)
    }
}

private struct LiveItem: View {
    let amount: String
    let color: Color
    let fontWeight: Font.Weight
    @Environment(\.spacing) var spacing

    var body: some View {
        InfoTooltip(
            title: DashboardLabels.shared.TOOLTIP_BALANCE_TITLE,
            description: DashboardLabels.shared.TOOLTIP_BALANCE_DESC
        ) {
            HStack(spacing: spacing.medium) {
                CircleIcon(iconName: "piggy_bank_duotone")
                Text(amount)
                    .font(.body)
                    .fontWeight(fontWeight)
                    .foregroundColor(color)
            }
        }
    }
}
