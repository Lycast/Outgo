import SwiftUI
import SharedApp

struct BudgetEditDialog: View {
    // --- Propriétés ---
    let currentIncomeInCents: Int64
    let onDismiss: () -> Void
    let onConfirm: (Int64) -> Void

    // --- Environnement ---
    @Environment(\.outgoColors) private var colors
    @Environment(\.spacing) private var spacing
    
    // --- État local ---
    @State private var textValue: String = ""

    var body: some View {
        VStack(alignment: .leading, spacing: spacing.large) {
            // --- Titre ---
            Text(BudgetEditDialogLabels.shared.DIALOG_BUDGET_TITLE)
                .font(.title3)
                .fontWeight(.bold)
                .foregroundColor(colors.onSurface)

            VStack(alignment: .leading, spacing: spacing.medium) {
                // Description
                Text(BudgetEditDialogLabels.shared.DIALOG_BUDGET_DESC)
                    .font(.body)
                    .foregroundColor(colors.onSurface)

                // Champ de saisie (TextField)
                HStack {
                    TextField("0.00", text: $textValue)
                        .keyboardType(.decimalPad)
                        .font(.body.monospacedDigit())
                        .onChange(of: textValue) { oldValue, newValue in
                            let filtered = newValue.replacingOccurrences(of: ",", with: ".")
                            if filtered.count <= 15 && filtered.allSatisfy({ $0.isNumber || $0 == "." }) {
                                textValue = filtered
                            } else if newValue != oldValue {
                                textValue = oldValue
                            }
                        }
                    
                    Text(CommonLabels.shared.CURRENCY_SYMBOL)
                        .foregroundColor(colors.onSurfaceVariant)
                        .fontWeight(.medium)
                }
                .padding()
                .background(colors.surface)
                .cornerRadius(8)
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(colors.onSurfaceVariant.opacity(0.2), lineWidth: 1)
                )

                // Info additionnelle
                Text(BudgetEditDialogLabels.shared.DIALOG_BUDGET_INFO)
                    .font(.caption)
                    .foregroundColor(colors.onSurfaceVariant)
            }

            // --- Boutons d'actions ---
            HStack(spacing: spacing.medium) {
                Spacer()
                
                Button(CommonLabels.shared.ACTION_CANCEL) {
                    onDismiss()
                }
                .foregroundColor(colors.primary)
                .fontWeight(.medium)

                Button(action: {
                    // Conversion inverse (Decimal -> Cents)
                    let amount = (Double(textValue) ?? 0.0) * 100
                    onConfirm(Int64(amount))
                }) {
                    Text(CommonLabels.shared.ACTION_SAVE)
                        .foregroundColor(.white)
                        .padding(.horizontal, 16)
                        .padding(.vertical, 8)
                        .background(textValue.isEmpty ? colors.primary.opacity(0.5) : colors.primary)
                        .cornerRadius(8)
                }
                .disabled(textValue.isEmpty)
            }
        }
        .padding(24)
        .background(colors.surface)
        .cornerRadius(16)
        .shadow(color: Color.black.opacity(0.15), radius: 20, x: 0, y: 10)
        .onAppear {
            if currentIncomeInCents > 0 {
                let decimal = Double(currentIncomeInCents) / 100.0
                textValue = String(format: "%.2f", decimal)
                    .replacingOccurrences(of: ".00", with: "")
            }
        }
    }
}

// --- Preview ---
#Preview {
    ZStack {
        Color.gray.opacity(0.2).ignoresSafeArea()
        BudgetEditDialog(
            currentIncomeInCents: 150000,
            onDismiss: {},
            onConfirm: { _ in }
        )
        .padding()
    }
}
