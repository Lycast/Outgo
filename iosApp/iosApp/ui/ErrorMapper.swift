import Foundation
import SharedApp

extension AppException {
    
    var uiString: String {
        guard let e = StringsCache.shared.resources?.error else { return "" }
        
        if self is OutgoingError.EmptyName { return e.OutgoingErrorEmptyName }
        else if self is OutgoingError.InvalidAmount { return e.OutgoingErrorInvalidAmount }
        else if self is OutgoingError.InvalidDate { return e.OutgoingErrorInvalidDate }
        else if self is OutgoingError.NotFound { return e.OutgoingErrorNotFound }
        else if self is OutgoingError.UnknownCycle { return e.OutgoingErrorUnknownCycle }
        
        else if self is CommonError.NetworkError { return e.CommonErrorNetwork }
        else if self is CommonError.DatabaseError { return e.CommonErrorDatabase }
        else if self is CommonError.UnknownError { return e.CommonErrorUnknown }
        else { return e.CommonErrorUnknown }
    }
}
