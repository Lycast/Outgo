import SwiftUI
import SharedApp

struct OutgoingDateSelector: View {
    let selectedDay: String
    let selectedMonth: String
    let onDayChanged: (String) -> Void
    let onMonthChanged: (String) -> Void
    
    @Environment(\.outgoColors) private var colors
    @Environment(\.spacing) private var spacing
    
    private let months: [(String, String)] = [
        ("0", DashboardLabels.shared.MONTH_ALL),
        ("1", DashboardLabels.shared.MONTH_1), ("2", DashboardLabels.shared.MONTH_2),
        ("3", DashboardLabels.shared.MONTH_3), ("4", DashboardLabels.shared.MONTH_4),
        ("5", DashboardLabels.shared.MONTH_5), ("6", DashboardLabels.shared.MONTH_6),
        ("7", DashboardLabels.shared.MONTH_7), ("8", DashboardLabels.shared.MONTH_8),
        ("9", DashboardLabels.shared.MONTH_9), ("10", DashboardLabels.shared.MONTH_10),
        ("11", DashboardLabels.shared.MONTH_11), ("12", DashboardLabels.shared.MONTH_12)
    ]
    
    private let itemHeight: CGFloat = 40
    private let containerHeight: CGFloat = 120

    var body: some View {
        ZStack {
            // --- LE CONTENEUR UNIQUE  ---
            RoundedRectangle(cornerRadius: 8)
                .strokeBorder(colors.textSecondary.opacity(0.3), lineWidth: 1)

            // --- 2. LES COLONNES ENSEMBLE ---
            HStack(spacing: 0) {
                
                // Colonne JOUR
                WheelColumn(
                    items: (1...31).map { "\($0)" },
                    selection: selectedDay,
                    onChanged: onDayChanged,
                    itemHeight: itemHeight,
                    dividerWidth: 60
                )
            
                
                // Colonne MOIS
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
                    .fill(colors.primary.opacity(0.2))
                    .frame(width: dividerWidth, height: 1)
                Rectangle()
                    .fill(colors.primary.opacity(0.2))
                    .frame(width: dividerWidth, height: 1)
            }

            // La roulette custom
            CustomWheel(
                items: items,
                labels: labels,
                selection: selection,
                onChanged: onChanged,
                itemHeight: itemHeight,
                dividerWidth: dividerWidth
            )
        }
        .frame(maxWidth: .infinity)
    }
}

struct CustomWheel: View {
    let items: [String]
    var labels: [String]? = nil
    let selection: String
    let onChanged: (String) -> Void
    let itemHeight: CGFloat
    let dividerWidth: CGFloat
    
    @Environment(\.outgoColors) private var colors
    @Environment(\.outgoTypography) private var typo

    // --- États pour le mode manuel ---
    @State private var isManualMode = false
    @State private var textBuffer = ""
    @FocusState private var isFocused: Bool

    var body: some View {
        ZStack {
            if isManualMode {
                TextField("", text: $textBuffer)
                    .keyboardType(.numberPad)
                    .focused($isFocused)
                    .font(typo.body.weight(.bold))
                    .multilineTextAlignment(.center)
                    .foregroundColor(colors.primary)
                    .frame(maxWidth: dividerWidth)
                    .frame(height: itemHeight)
                    .background(colors.background)
                    .cornerRadius(8)
                    .onSubmit { validateManualInput() }
                    .onAppear { textBuffer = selection }
            } else {
                // --- TON DESIGN ORIGINAL ---
                ScrollView(.vertical, showsIndicators: false) {
                    LazyVStack(spacing: 0) {
                        ForEach(0..<items.count, id: \.self) { index in
                            let isSelected = items[index] == selection
                            Text(labels?[index] ?? items[index])
                                .font(typo.body)
                                .fontWeight(isSelected ? .semibold : .regular)
                                .foregroundColor(isSelected ? colors.primary : colors.textSecondary)
                                .frame(height: itemHeight)
                                .frame(maxWidth: .infinity)
                                .opacity(isSelected ? 1.0 : 0.4)
                                .scaleEffect(isSelected ? 1.15 : 1.0)
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
                .disabled(isManualMode)
            }
        }
        .sensoryFeedback(.selection, trigger: selection)
        .onAppear { if selection.isEmpty || !items.contains(selection) {
            if let firstItem = items.first { onChanged(firstItem) }} else { onChanged(selection) }
        }
        .contentShape(Rectangle())
        .onLongPressGesture(minimumDuration: 0.5) { enableManualMode() }
        .onTapGesture { if isManualMode { validateManualInput() } }
        .toolbar { if isFocused { CustomToolbarGroup( title: "OK") { validateManualInput() }}}
        // --- ACCESSIBILITÉ ---
        .accessibilityElement(children: .ignore)
        .accessibilityValue(labels?[items.firstIndex(of: selection) ?? 0] ?? selection)
        .accessibilityAction {
            enableManualMode()
        }
    }

    private func enableManualMode() {
        let impact = UIImpactFeedbackGenerator(style: .medium)
            impact.impactOccurred()
        
        isManualMode = true
        isFocused = true
    }

    private func validateManualInput() {
        isManualMode = false
        isFocused = false
        
        if items.contains(textBuffer) {
            onChanged(textBuffer)
        }
    }
}
