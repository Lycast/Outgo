import SwiftUI
import SharedApp

class OutgoingFormState: ObservableObject {
    
    let outgoingId: String?
    
    @Published var nameBuffer: String
    @Published var amountBuffer: String
    @Published var recurrenceSelection: Recurrence
    @Published var dueDayBuffer: String
    @Published var dueMonthBuffer: String
    
    init(
        outgoingId: String? = nil,
        initialName: String = "",
        initialAmount: String = "",
        initialRecurrence: Recurrence = .monthly,
        initialDueDay: String = "",
        initialDueMonth: String = ""
    ) {
        self.outgoingId = outgoingId
        self.nameBuffer = initialName
        self.amountBuffer = initialAmount
        self.recurrenceSelection = initialRecurrence
        self.dueDayBuffer = initialDueDay
        self.dueMonthBuffer = initialDueMonth
    }
    
    var isValid: Bool {
        let isNameValid = !nameBuffer.trimmingCharacters(in: .whitespaces).isEmpty
        let isAmountValid = amountInCents > 0
        let isDayValid = (1...31).contains(Int(dueDayBuffer) ?? 0)
        
        let monthInt = Int(dueMonthBuffer) ?? 0
        let isMonthValid = (recurrenceSelection == .yearly) ? (1...12).contains(monthInt) : (monthInt == 0)
        
        return isNameValid && isAmountValid && isDayValid && isMonthValid
    }
    
    var amountInCents: Int64 {
        let cleanAmount = amountBuffer.replacingOccurrences(of: ",", with: ".")
        guard let decimalValue = Double(cleanAmount) else { return 0 }
        return Int64((decimalValue * 100).rounded())
    }
    
    func onEvent(_ event: OutgoingFormEventSwift) {
        switch event {
        case .updateName(let name):
            nameBuffer = name
        case .updateAmount(let amount):
            let filtered = amount.replacingOccurrences(of: ",", with: ".")
            if filtered.count <= 12 {
                amountBuffer = filtered
            }
        case .updateRecurrence(let rec):
            recurrenceSelection = rec
            if rec == .monthly { dueMonthBuffer = "0" }
            
        case .updateDueDay(let day):
            dueDayBuffer = day
            
        case .updateDueMonth(let month):
            dueMonthBuffer = month
            if month == "0" || month.isEmpty {
                recurrenceSelection = .monthly
            } else {
                recurrenceSelection = .yearly
            }
        }
    }
}

enum OutgoingFormEventSwift {
    case updateName(String)
    case updateAmount(String)
    case updateRecurrence(Recurrence)
    case updateDueDay(String)
    case updateDueMonth(String)
}
