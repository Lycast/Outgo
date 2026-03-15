import SwiftUI
import SharedApp

// --- 1. Formatage de la Monnaie ---
extension Int64 {
    var uiAmount: String {
        let isNegative = self < 0
        let absoluteValue = abs(self)

        let euros = absoluteValue / 100
        let cents = absoluteValue % 100

        // Formatage des centimes pour toujours avoir 2 chiffres (ex: 05)
        let formattedCents = String(format: "%02d", cents)

        // Swift possède un NumberFormatter très puissant pour grouper par milliers
        let formatter = NumberFormatter()
        formatter.numberStyle = .decimal
        formatter.groupingSeparator = " "
        let eurosString = formatter.string(from: NSNumber(value: euros)) ?? String(euros)

        let prefix = isNegative ? "-" : ""

        // On suppose que CommonLabels a été créé comme vu précédemment
        return "\(prefix)\(eurosString),\(formattedCents) \(CommonLabels.shared.CURRENCY_SYMBOL)"
    }
}

// --- 2. Couleurs des Statuts et Récurrences ---
extension SyncStatus {
    var uiColor: Color {
        switch self {
        case .synced: return Color(hex: 0xFF4CAF50)
        case .pendingCreate: return Color(hex: 0xFF2196F3)
        case .pendingUpdate: return Color(hex: 0xFFFF9800)
        case .pendingDelete: return Color(hex: 0xFFF44336)
        case .unknown: return Color.gray
        default: return Color.gray
        }
    }
}

extension Recurrence {
    var uiRecurrenceColor: Color {
        switch self {
        case .yearly: return Color(hex: Palette.shared.BrandSecondary)
        case .monthly: return Color(hex: Palette.shared.BrandTertiary)
        case .unknown: return Color.gray
        default: return Color.gray
        }
    }
}

// --- 3. L'Entité Outgoing ---
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
        default: return ""
        }
    }
}

// --- 4. Fonction utilitaire pour les mois ---
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
