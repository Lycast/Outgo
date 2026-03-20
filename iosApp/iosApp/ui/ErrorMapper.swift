import Foundation
import SharedApp

extension AppException {
    
    var uiString: String {
        guard let e = StringsCache.shared.resources?.error else {
            return ""
        }
        
        if self is OutgoingError.EmptyName {
            return e.errorEmptyName
        }
        else if self is OutgoingError.InvalidAmount {
            return e.errorInvalidAmount
        }
        else if self is CommonError.NetworkError {
            return e.errorNetwork
        }
        else {
            return e.errorUnknown
        }
    }
}
