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
    @Environment(\.outgoTypography) private var typo

    // --- Logique Interne ---
    private var maxValue: Float {
        Float(max(max(monthlyIncomeInCents, totalOutgoingsInCents), 1))
    }
    
    private var isNegativeLive: Bool { disposableIncomeInCents < 0 }
    
    private var liveColor: Color { isNegativeLive ? colors.error : colors.tertiary }

    private var isTooLong: Bool {
        let totalLength = monthlyIncomeInCents.uiAmount.count + disposableIncomeInCents.uiAmount.count
        return totalLength > 20
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
                .background(colors.textSecondary.opacity(0.1))
                .padding(.horizontal, spacing.large)

            // --- Contenu Dépliable ---
            if isExpanded {
                VStack(spacing: spacing.large) {
                    Spacer().frame(height: spacing.small)

                    // Bloc Revenus / Reste à vivre
                    if isTooLong {
                        VStack(spacing: spacing.medium) { contentItems }
                    } else {
                        HStack(alignment: .center) {
                        
                            contentItems
                        }
                    }

                    Divider().background(colors.textSecondary.opacity(0.1))

                    // Barres de progression (PairedBudgetBar)
                    PairedBudgetBar(
                        topLabel: DashboardLabels.shared.HERO_TOTAL_CHARGES_LABEL,
                        topAmount: totalOutgoingsInCents.uiAmount,
                        topProgress: Float(totalOutgoingsInCents) / maxValue,
                        topBarColor: colors.primary, // Bleu

                        bottomLabel: DashboardLabels.shared.HERO_REMAINING_TO_PAY_LABEL,
                        bottomAmount: remainingToPayInCents.uiAmount,
                        bottomProgress: Float(remainingToPayInCents) / maxValue,
                        bottomBarColor: colors.tertiary // Or
                    )
                }
                .padding(.horizontal, spacing.large)
                .transition(.opacity.combined(with: .offset(y: -5)))
            }
            
            Spacer().frame(height: spacing.medium)
            
            // --- Bouton Toggle ---
            Button(action: {
                withAnimation(.spring(response: 0.2, dampingFraction: 1)) {
                    onToggleExpand()
                }
            }) {
                Image(isExpanded ? "caret_up" : "caret_down")
                    .resizable()
                    .renderingMode(.template)
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 24, height: 24)
                    .foregroundColor(colors.textSecondary.opacity(0.5))
                    .padding(.vertical, spacing.small)
                    .frame(maxWidth: .infinity)
            }
            .accessibilityLabel(isExpanded ? AccessibilityLabels.shared.COLLAPSE_HERO : AccessibilityLabels.shared.EXPAND_HERO)
        }
        .background(colors.surface100)
        .cornerRadius(12)
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(colors.textSecondary.opacity(0.1), lineWidth: 1)
        )
        .padding(.horizontal, spacing.medium)
        .animation(.spring(response: 0.2, dampingFraction: 1), value: isExpanded)
        .sensoryFeedback(.impact(weight: .light), trigger: isExpanded)
    }

    @ViewBuilder
    private var contentItems: some View {
        BudgetItem(
            amount: monthlyIncomeInCents.uiAmount,
            onClick: onEditBudgetClick
        )
        
        Spacer()
        
        LiveItem(
            isNegativeLive: isNegativeLive,
            amount: disposableIncomeInCents.uiAmount,
            color: liveColor
        )
    }
}

// --- Sous-Composants Privés ---
private struct BudgetItem: View {
    let amount: String
    let onClick: () -> Void
    @Environment(\.spacing) var spacing
    @Environment(\.outgoColors) var colors
    @Environment(\.outgoTypography) var typo
    
    var body: some View {
        Button(action: onClick) {
            HStack(spacing: spacing.medium) {
                CircleIcon(iconName: "bank_duotone", tintColor: colors.textOnBrand, containerColor: colors.primary)
                
                VStack(alignment: .leading, spacing: 0) {
                    Text(DashboardLabels.shared.HERO_TOTAL_INCOME_LABEL)
                        .font(typo.caption)
                        .foregroundColor(colors.textSecondary)
                    Text(amount)
                        .font(typo.title)
                        .foregroundColor(colors.textPrimary)
                }
            }
        }
        .buttonStyle(.plain)
    }
}

private struct LiveItem: View {
    let isNegativeLive: Bool
    let amount: String
    let color: Color
    @Environment(\.spacing) var spacing
    @Environment(\.outgoColors) var colors
    @Environment(\.outgoTypography) var typo

    var body: some View {
        InfoTooltip(
            title: DashboardLabels.shared.TOOLTIP_BALANCE_TITLE,
            description: DashboardLabels.shared.TOOLTIP_BALANCE_DESC
        ) {
            HStack(spacing: spacing.medium) {
                CircleIcon(iconName: "piggy_bank_duotone", tintColor: colors.textOnBrand, containerColor: color)
                
                VStack(alignment: .leading, spacing: 0) {
                    Text(isNegativeLive ? DashboardLabels.shared.HERO_MISSING_INCOME_LABEL : DashboardLabels.shared.HERO_DISPOSABLE_INCOME_LABEL)
                        .font(typo.caption)
                        .foregroundColor(colors.textSecondary)
                    Text(amount)
                        .font(typo.title)
                        .fontWeight(isNegativeLive ? .medium : .bold)
                        .foregroundColor(color)
                }
            }
        }
    }
}
