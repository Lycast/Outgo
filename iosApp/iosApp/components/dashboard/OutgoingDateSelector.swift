import SwiftUI
import SharedApp

struct OutgoingDateSelector: View {
    let selectedDay: String
    let selectedMonth: String
    let onDayChanged: (String) -> Void
    let onMonthChanged: (String) -> Void
    
    @Environment(\.outgoColors) private var colors
    
    private let months: [(String, String)] = [
        ("0", DashboardLabels.shared.MONTH_ALL),
        ("1", DashboardLabels.shared.MONTH_1), ("2", DashboardLabels.shared.MONTH_2),
        ("3", DashboardLabels.shared.MONTH_3), ("4", DashboardLabels.shared.MONTH_4),
        ("5", DashboardLabels.shared.MONTH_5), ("6", DashboardLabels.shared.MONTH_6),
        ("7", DashboardLabels.shared.MONTH_7), ("8", DashboardLabels.shared.MONTH_8),
        ("9", DashboardLabels.shared.MONTH_9), ("10", DashboardLabels.shared.MONTH_10),
        ("11", DashboardLabels.shared.MONTH_11), ("12", DashboardLabels.shared.MONTH_12)
    ]
    
    private let itemHeight: CGFloat = 32
    private let containerHeight: CGFloat = 96

    var body: some View {
        ZStack {
            // --- 1. LE CONTENEUR UNIQUE (Fond et Bordure) ---
            RoundedRectangle(cornerRadius: 8)
                .fill(colors.surface)
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(colors.onSurfaceVariant.opacity(0.3), lineWidth: 1)
                )

            // --- 2. LES COLONNES ENSEMBLE ---
            HStack(spacing: 0) {
                
                // Colonne JOUR (Sans fond, avec ses propres lignes)
                WheelColumn(
                    items: (1...31).map { "\($0)" },
                    selection: selectedDay,
                    onChanged: onDayChanged,
                    itemHeight: itemHeight,
                    dividerWidth: 60
                )
            
                
                // Colonne MOIS (Sans fond, avec ses propres lignes)
                WheelColumn(
                    items: months.map { $0.0 },
                    labels: months.map { $0.1 },
                    selection: selectedMonth,
                    onChanged: onMonthChanged,
                    itemHeight: itemHeight,
                    dividerWidth: 120
                )
            }
        }
        .frame(height: containerHeight)
    }
}

// --- Sous-composant pour une colonne "Nue" ---
private struct WheelColumn: View {
    let items: [String]
    var labels: [String]? = nil
    let selection: String
    let onChanged: (String) -> Void
    let itemHeight: CGFloat
    let dividerWidth: CGFloat
    
    @Environment(\.outgoColors) private var colors
    
    var body: some View {
        ZStack {
            VStack(spacing: itemHeight) {
                Rectangle()
                    .fill(colors.primary.opacity(0.5))
                    .frame(width: dividerWidth, height: 1)
                Rectangle()
                    .fill(colors.primary.opacity(0.5))
                    .frame(width: dividerWidth, height: 1)
            }

            // La roulette custom
            CustomWheel(
                items: items,
                labels: labels,
                selection: selection,
                onChanged: onChanged,
                itemHeight: itemHeight
            )
        }
        .frame(maxWidth: .infinity)
    }
}

// (Garder le composant CustomWheel tel quel, il fonctionne parfaitement)
struct CustomWheel: View {
    let items: [String]
    var labels: [String]? = nil
    let selection: String
    let onChanged: (String) -> Void
    let itemHeight: CGFloat
    
    @Environment(\.outgoColors) private var colors

    var body: some View {
        ScrollView(.vertical, showsIndicators: false) {
            LazyVStack(spacing: 0) {
                ForEach(0..<items.count, id: \.self) { index in
                    let isSelected = items[index] == selection
                    Text(labels?[index] ?? items[index])
                        .font(.system(size: 16, weight: isSelected ? .bold : .medium))
                        .foregroundColor(isSelected ? colors.primary : colors.onSurfaceVariant)
                        .frame(height: itemHeight)
                        .frame(maxWidth: .infinity)
                }
            }
            .scrollTargetLayout()
        }
        .scrollTargetBehavior(.viewAligned)
        .scrollPosition(id: .init(get: {
            items.firstIndex(of: selection)
        }, set: { newIndex in
            if let i = newIndex { onChanged(items[i]) }
        }))
        .contentMargins(.vertical, itemHeight, for: .scrollContent)
        .sensoryFeedback(.selection, trigger: selection)
    }
}
