import SwiftUI
import SharedApp

struct OutgoingFormSheet: View {
    // --- État ---
    @ObservedObject var state: OutgoingFormState
    
    // --- Actions ---
    let onDismiss: () -> Void
    let onSave: (OutgoingIntentSave) -> Void
    
    // --- Environnement ---
    @Environment(\.dismiss) private var dismiss
    @Environment(\.outgoColors) private var colors
    
    var body: some View {
        NavigationStack {
            OutgoingFormContent(
                state: state,
                onCancel: {
                    dismiss()
                    onDismiss()
                },
                onSave: {
                    let monthValue = Int32(state.dueMonthBuffer) ?? 0
                    
                    let intent = OutgoingIntentSave(
                        id: state.outgoingId,
                        name: state.nameBuffer,
                        amountInCents: state.amountInCents,
                        recurrence: state.recurrenceSelection,
                        dueDay: Int32(state.dueDayBuffer) ?? 1,
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
        .background(colors.surface200.ignoresSafeArea())
    }
}
