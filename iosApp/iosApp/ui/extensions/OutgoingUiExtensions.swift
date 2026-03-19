import SwiftUI
import SharedApp

// --- Formatage de la Monnaie ---
extension Int64 {
    var uiAmount: String {
        let isNegative = self < 0
        let absoluteValue = abs(self)

        let euros = absoluteValue / 100
        let cents = absoluteValue % 100

        let formattedCents = String(format: "%02d", cents)

        let formatter = NumberFormatter()
        formatter.numberStyle = .decimal
        formatter.groupingSeparator = " "
        let eurosString = formatter.string(from: NSNumber(value: euros)) ?? String(euros)

        let prefix = isNegative ? "-" : ""

        return "\(prefix)\(eurosString),\(formattedCents) \(CommonLabels.shared.CURRENCY_SYMBOL)"
    }
}

extension Recurrence {
    var uiRecurrenceColor: Color {
        switch self {
        case .yearly: return Color(hex: ColorPalette.shared.SECONDARY)
        case .monthly: return Color(hex: ColorPalette.shared.TERTIARY)
        case .unknown: return Color.gray
        }
    }
}

// --- L'Entité Outgoing ---
extension Outgoing {
    var uiTitle: String {
        let trimmedName = self.name.trimmingCharacters(in: .whitespacesAndNewlines)
        return trimmedName.isEmpty ? DashboardLabels.shared.DEFAULT_NAME : trimmedName
    }
    
    var uiDueDayLabel: String {
        if self.recurrence == .yearly, let kotlinMonth = self.dueMonth {
            let swiftMonth = Int(truncating: kotlinMonth)
            let monthLabel = getMonthName(month: swiftMonth)
            return "\(DashboardLabels.shared.DUE_PREFIX) \(self.dueDay) \(monthLabel)"
        } else {
            return "\(DashboardLabels.shared.DUE_PREFIX) \(self.dueDay)"
        }
    }
    
    var uiFrequencySummary: String {
        switch self.recurrence {
        case .monthly: return FormLabels.shared.CYCLE_MONTHLY
        case .yearly: return FormLabels.shared.CYCLE_YEARLY
        case .unknown: return ""
        }
    }
}

// --- Fonction utilitaire pour les mois ---
private func getMonthName(month: Int) -> String {
    switch month {
    case 1: return DashboardLabels.shared.MONTH_1
    case 2: return DashboardLabels.shared.MONTH_2
    case 3: return DashboardLabels.shared.MONTH_3
    case 4: return DashboardLabels.shared.MONTH_4
    case 5: return DashboardLabels.shared.MONTH_5
    case 6: return DashboardLabels.shared.MONTH_6
    case 7: return DashboardLabels.shared.MONTH_7
    case 8: return DashboardLabels.shared.MONTH_8
    case 9: return DashboardLabels.shared.MONTH_9
    case 10: return DashboardLabels.shared.MONTH_10
    case 11: return DashboardLabels.shared.MONTH_11
    case 12: return DashboardLabels.shared.MONTH_12
    default: return ""
    }
}
