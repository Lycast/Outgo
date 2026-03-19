import SwiftUI
import SharedApp

struct BudgetEditModal: ViewModifier {
    @Binding var isPresented: Bool
    let currentIncomeInCents: Int64
    let onConfirm: (Int64) -> Void
    
    @Environment(\.outgoColors) private var colors
    @Environment(\.outgoTypography) private var typo
    @Environment(\.spacing) private var spacing
    
    @State private var textValue: String = ""
    @FocusState private var isInputActive: Bool
    
    func body(content: Content) -> some View {
        ZStack {
            content
            
            if isPresented {
                // --- Fond sombre ---
                Color.black.opacity(0.4)
                    .ignoresSafeArea()
                    .onTapGesture { dismiss() }
                    .transition(.opacity)
                
                // --- La Carte ---
                VStack(spacing: spacing.medium) {
                    
                    Image("bank_duotone")
                        .resizable()
                        .renderingMode(.template)
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 32, height: 32)
                        .foregroundColor(colors.primary)
                    
                    // Titre
                    Text(BudgetEditDialogLabels.shared.DIALOG_BUDGET_TITLE)
                        .font(typo.subtitle)
                        .foregroundColor(colors.textPrimary)
                        .multilineTextAlignment(.center)

                    VStack(spacing: spacing.medium) {
                        // Description
                        Text(BudgetEditDialogLabels.shared.DIALOG_BUDGET_DESC)
                            .font(typo.body)
                            .foregroundColor(colors.textSecondary)
                            .multilineTextAlignment(.center)
                            .fixedSize(horizontal: false, vertical: true)

                        // Champ de saisie
                        VStack(alignment: .leading, spacing: 4) {
                            
                            HStack {
                                TextField(FormLabels.shared.FIELD_PLACE_HOLDER_AMOUNT, text: $textValue)
                                    .keyboardType(.decimalPad)
                                    .focused($isInputActive)
                                    .autocorrectionDisabled(true)
                                    .font(typo.body)
                                    .foregroundColor(colors.textPrimary)
                                    .onChange(of: textValue) { oldValue, newValue in
                                        filterInput(oldValue: oldValue, newValue: newValue)
                                    }
                                
                                Text(CommonLabels.shared.CURRENCY_SYMBOL)
                                    .font(typo.body)
                                    .foregroundColor(colors.textSecondary)
                            }
                            .padding(spacing.medium)
                            .background(Color.black.opacity(0.02))
                            .cornerRadius(8)
                            .overlay(
                                RoundedRectangle(cornerRadius: 8)
                                    .stroke(colors.textSecondary.opacity(0.2), lineWidth: 1)
                            )
                        }

                        Text(BudgetEditDialogLabels.shared.DIALOG_BUDGET_INFO)
                            .font(typo.caption)
                            .foregroundColor(colors.textSecondary)
                            .multilineTextAlignment(.center)
                    }

                    Spacer().frame(height: spacing.small)

                    // Boutons d'actions
                    HStack(spacing: spacing.medium) {
                        Button(action: { dismiss() }) {
                            Text(CommonLabels.shared.ACTION_CANCEL)
                                .font(typo.body)
                                .fontWeight(.medium)
                                .foregroundColor(colors.textSecondary)
                                .frame(maxWidth: .infinity)
                                .padding(.vertical, 12)
                        }

                        Button(action: { confirm() }) {
                            Text(CommonLabels.shared.ACTION_SAVE)
                                .font(typo.body)
                                .fontWeight(.bold)
                                .foregroundColor(colors.primary)
                                .frame(maxWidth: .infinity)
                                .padding(.vertical, 12)
                        }
                        .disabled(textValue.isEmpty)
                        .opacity(textValue.isEmpty ? 0.5 : 1.0)
                    }
                }
                .padding(spacing.extraLarge)
                .background(colors.surface200)
                .cornerRadius(24)
                .padding(spacing.extraLarge)
                .transition(.scale(scale: 0.9).combined(with: .opacity))
                .onTapGesture { isInputActive = false}
                .onAppear { initializeValue() }
            }
        }
        .animation(.spring(response: 0.3, dampingFraction: 0.7), value: isPresented)
    }
    
    // --- Logique ---
    private func dismiss() { isPresented = false }
    
    private func confirm() {
        let amount = (Double(textValue) ?? 0.0) * 100
        onConfirm(Int64(amount))
        dismiss()
    }

    private func filterInput(oldValue: String, newValue: String) {
        let filtered = newValue.replacingOccurrences(of: ",", with: ".")
        if filtered.count <= 15 && filtered.allSatisfy({ $0.isNumber || $0 == "." }) {
            textValue = filtered
        } else if newValue != oldValue {
            textValue = oldValue
        }
    }

    private func initializeValue() {
        if currentIncomeInCents > 0 {
            let decimal = Double(currentIncomeInCents) / 100.0
            textValue = String(format: "%.2f", decimal).replacingOccurrences(of: ".00", with: "")
        } else {
            textValue = ""
        }
    }
}

extension View {
    func budgetEditModal(
        isPresented: Binding<Bool>,
        currentIncomeInCents: Int64,
        onConfirm: @escaping (Int64) -> Void
    ) -> some View {
        self.modifier( BudgetEditModal(
            isPresented: isPresented,
            currentIncomeInCents: currentIncomeInCents,
            onConfirm: onConfirm
        ))
    }
}
