import SwiftUI
import SharedApp

struct OutgoingFormContent: View {
    // --- État ---
    @ObservedObject var viewModel: OutgoingFormViewModel
    
    // --- Actions ---
    let onCancel: () -> Void
    let onSave: () -> Void
    
    // --- Environnement ---
    @Environment(\.outgoColors) private var colors
    @Environment(\.spacing) private var spacing
    
    private var isEditMode: Bool { viewModel.outgoingId != nil }

    var body: some View {
            ScrollView(showsIndicators: false) {
                
                VStack(alignment: .center, spacing: spacing.large) {
                    
                    // --- Titre ---
                    Text(isEditMode ? FormLabels.shared.SHEET_TITLE_EDIT : FormLabels.shared.SHEET_TITLE_ADD)
                        .font(.title2)
                        .fontWeight(.bold)
                        .foregroundColor(colors.onSurface)
                        .toolbar(.hidden, for: .navigationBar)
                        .padding(.bottom, spacing.medium)

                    // --- Champ : Nom ---
                    OutlinedTextField(
                        value: $viewModel.nameBuffer,
                        label: FormLabels.shared.FIELD_NAME,
                        placeholder: "Ex: Netflix",
                        keyboardType: .default
                    )

                    // --- Champ : Montant ---
                    OutlinedTextField(
                        value: $viewModel.amountBuffer,
                        label: FormLabels.shared.FIELD_AMOUNT,
                        placeholder: "0.00",
                        suffix: CommonLabels.shared.CURRENCY_SYMBOL,
                        keyboardType: .decimalPad
                    )

                    // --- Section Date & Récurrence ---
                    VStack(alignment: .leading, spacing: spacing.medium) {
                        Text(FormLabels.shared.FIELD_DATE_DESC)
                            .font(.body)
                            .foregroundColor(colors.onSurfaceVariant)
                            .multilineTextAlignment(.leading)
                            .fixedSize(horizontal: false, vertical: true)

                        // --- Sélecteur : Récurrence (Chips) ---
                        HStack(spacing: spacing.medium) {
                            filterChip(
                                title: FormLabels.shared.CYCLE_MONTHLY,
                                isSelected: viewModel.recurrenceSelection == .monthly,
                                action: { viewModel.onEvent(.updateRecurrence(.monthly)) }
                            )
                            filterChip(
                                title: FormLabels.shared.CYCLE_YEARLY,
                                isSelected: viewModel.recurrenceSelection == .yearly,
                                action: { viewModel.onEvent(.updateRecurrence(.yearly)) }
                            )
                        }

                        // --- Champs numériques (Jour / Mois) ---
                        HStack(spacing: spacing.large) {
                            OutlinedTextField(
                                value: $viewModel.dueDayBuffer,
                                label: FormLabels.shared.FIELD_DAY,
                                placeholder: "1-31",
                                keyboardType: .numberPad
                            )
                            .frame(maxWidth: .infinity)

                            if viewModel.recurrenceSelection == .yearly {
                                OutlinedTextField(
                                    value: $viewModel.dueMonthBuffer,
                                    label: FormLabels.shared.FIELD_MONTH,
                                    placeholder: "1-12",
                                    keyboardType: .numberPad
                                )
                                .frame(maxWidth: .infinity)
                            }
                        }
                    }
                    .padding(.top, spacing.small)

                    // --- Boutons d'actions ---
                    HStack {
                        Spacer()
                        
                        Button(CommonLabels.shared.ACTION_CANCEL) {
                            onCancel()
                        }
                        .foregroundColor(colors.primary)
                        .padding(.trailing, spacing.medium)

                        Button(action: onSave) {
                            Text(CommonLabels.shared.ACTION_SAVE)
                                .fontWeight(.bold)
                                .foregroundColor(.white)
                                .padding(.horizontal, 24)
                                .padding(.vertical, 12)
                                .background(viewModel.isValid ? colors.primary : colors.primary.opacity(0.5))
                                .cornerRadius(100)
                        }
                        .disabled(!viewModel.isValid)
                    }
                    .padding(.top, spacing.medium)
                }
                .padding(spacing.large)
            }
            .background(colors.surface)
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
                .foregroundColor(isSelected ? .white : colors.onSurface)
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
                        .foregroundColor(colors.onSurfaceVariant)
                }
            }
            .padding()
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(colors.onSurfaceVariant.opacity(0.3), lineWidth: 1)
            )
        }
    }
}
