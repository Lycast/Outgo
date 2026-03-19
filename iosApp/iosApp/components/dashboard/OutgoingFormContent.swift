import SwiftUI
import SharedApp

struct OutgoingFormContent: View {
    // --- État ---
    @ObservedObject var state: OutgoingFormState
    
    // --- Actions ---
    let onCancel: () -> Void
    let onSave: () -> Void
    
    // --- Environnement ---
    @Environment(\.outgoColors) private var colors
    @Environment(\.spacing) private var spacing
    @Environment(\.outgoTypography) private var typo
    
    private enum Field {
            case name
            case amount
        }
    
    @FocusState private var focusedField: Field?
    
    private var isEditMode: Bool { state.outgoingId != nil }

    var body: some View {
            ScrollView(showsIndicators: false) {
                
                VStack(alignment: .center, spacing: spacing.large) {
                    Spacer().frame(height: spacing.medium)
                    // --- Titre ---
                    Text(isEditMode ? FormLabels.shared.SHEET_TITLE_EDIT : FormLabels.shared.SHEET_TITLE_ADD)
                        .font(typo.subtitle)
                        .foregroundColor(colors.textPrimary)
                        .toolbar(.hidden, for: .navigationBar)
                        .padding(.bottom, spacing.medium)

                    // --- Champ : Nom ---
                    OutlinedTextField(
                        value: $state.nameBuffer,
                        label: FormLabels.shared.FIELD_NAME,
                        placeholder: FormLabels.shared.FIELD_PLACE_HOLDER_NAME,
                        keyboardType: .default
                    )
                    .focused($focusedField, equals: .name)
                    .autocorrectionDisabled(true)

                    // --- Champ : Montant ---
                    OutlinedTextField(
                        value: $state.amountBuffer,
                        label: FormLabels.shared.FIELD_AMOUNT,
                        placeholder: FormLabels.shared.FIELD_PLACE_HOLDER_AMOUNT,
                        suffix: CommonLabels.shared.CURRENCY_SYMBOL,
                        keyboardType: .decimalPad
                    )
                    .focused($focusedField, equals: .amount)
                    .autocorrectionDisabled(true)

                    // --- Section Date & Récurrence ---
                    VStack(alignment: .leading) {
                        
                        Text(FormLabels.shared.FIELD_DATE_DESC)
                            .font(typo.caption)
                            .foregroundColor(colors.textSecondary)
                            .multilineTextAlignment(.leading)
                            .fixedSize(horizontal: false, vertical: true)

                        OutgoingDateSelector(
                            selectedDay: state.dueDayBuffer,
                            selectedMonth: state.dueMonthBuffer,
                            onDayChanged: { state.onEvent(.updateDueDay($0)) },
                            onMonthChanged: { state.onEvent(.updateDueMonth($0)) }
                        )
                    }
                    .padding(.vertical, spacing.small)

                    // --- Boutons d'actions ---
                    HStack {
                        Spacer()
                        
                        Button(CommonLabels.shared.ACTION_CANCEL) {
                            focusedField = nil
                            onCancel()
                        }
                        .foregroundColor(colors.primary)
                        .padding(.trailing, spacing.medium)

                        Button(action:{
                            focusedField = nil
                            onSave()
                        }) {
                            Text(CommonLabels.shared.ACTION_SAVE)
                                .font(typo.label)
                                .fontWeight(.bold)
                                .foregroundColor(state.isValid ? colors.textOnBrand : colors.textSecondary.opacity(0.8))
                                .padding(.horizontal, 24)
                                .padding(.vertical, 12)
                                .background(state.isValid ? colors.primary : colors.textSecondary.opacity(0.2))
                                .cornerRadius(100)
                        }
                        .disabled(!state.isValid)
                    }
                    .padding(.top, spacing.medium)
                }
                .padding(spacing.large)
            }
            .onTapGesture { focusedField = nil }
        }

    // --- Helper : Style des Chips ---
    @ViewBuilder
    private func filterChip(title: String, isSelected: Bool, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            Text(title)
                .font(.subheadline)
                .fontWeight(isSelected ? .bold : .regular)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 8)
                .background(isSelected ? colors.primary : colors.primary.opacity(0.1))
                .foregroundColor(isSelected ? .white : colors.textPrimary)
                .cornerRadius(8)
        }
        .buttonStyle(.plain)
    }
}

// --- Composant Privé : TextField Stylisé (Outlined) ---
private struct OutlinedTextField: View {
    @Binding var value: String
    let label: String
    let placeholder: String
    var suffix: String? = nil
    let keyboardType: UIKeyboardType
    
    @Environment(\.outgoColors) private var colors

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(label)
                .font(.caption)
                .foregroundColor(colors.primary)
                .fontWeight(.medium)
            
            HStack {
                TextField(placeholder, text: $value)
                    .keyboardType(keyboardType)
                
                if let suffix = suffix {
                    Text(suffix)
                        .foregroundColor(colors.textSecondary)
                }
            }
            .padding()
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(colors.textSecondary.opacity(0.3), lineWidth: 1)
            )
        }
    }
}
