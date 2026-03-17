import SwiftUI
import SharedApp

struct OutgoingFormSheet: View {
    // --- État ---
    @ObservedObject var viewModel: OutgoingFormState
    
    // --- Actions ---
    let onDismiss: () -> Void
    let onSave: (OutgoingIntentSave) -> Void
    
    // --- Environnement ---
    @Environment(\.dismiss) private var dismiss
    
    var body: some View {
        NavigationStack {
            OutgoingFormContent(
                viewModel: viewModel,
                onCancel: {
                    dismiss()
                    onDismiss()
                },
                onSave: {
                    let monthValue = Int32(viewModel.dueMonthBuffer) ?? 0
                    
                    let intent = OutgoingIntentSave(
                        id: viewModel.outgoingId,
                        name: viewModel.nameBuffer,
                        amountInCents: viewModel.amountInCents,
                        recurrence: viewModel.recurrenceSelection,
                        dueDay: Int32(viewModel.dueDayBuffer) ?? 1,
                        dueMonth: (monthValue == 0) ? nil : KotlinInt(value: monthValue)
                    )
                    
                    onSave(intent)
                    dismiss()
                }
            )
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button(CommonLabels.shared.ACTION_CANCEL) {
                        dismiss()
                        onDismiss()
                    }
                }
            }
        }
        .presentationDetents([.medium, .large])
        .presentationDragIndicator(.visible)
    }
}
