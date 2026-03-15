import Foundation
import SharedApp

extension AppException {
    
    var uiString: String {
        if self is OutgoingError.EmptyName {
            return IosResourceHelper.shared.getStringSync(resource: Res.string().error_outgoing_empty_name)
        }
        else if self is OutgoingError.InvalidAmount {
            return IosResourceHelper.shared.getStringSync(resource: Res.string().error_outgoing_invalid_amount)
        }
        else if self is CommonError.NetworkError {
            return IosResourceHelper.shared.getStringSync(resource: Res.string().error_global_network)
        }
        else {
            return IosResourceHelper.shared.getStringSync(resource: Res.string().error_global_unknown)
        }
    }
}
