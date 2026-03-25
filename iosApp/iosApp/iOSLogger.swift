import Foundation
import SharedApp

/**
 * Implémentation du Logger pour iOS.
 * Utilise 'print' pour afficher les messages dans la console Xcode.
 */
class iOSLogger: Logger {
    
    // 1. La méthode principale (que vous aviez déjà)
    func log(level: LogLevel, tag: String, message: String, throwable: KotlinThrowable?) {
        let symbol: String
        switch level {
        case .debug: symbol = "🔍"
        case .info:  symbol = "ℹ️"
        case .warn:  symbol = "⚠️"
        case .error: symbol = "❌"
        }
        
        // Formatage : [Tag] ❌ Message
        print("[\(tag)] \(symbol) \(message)")
        
        // Si on a une exception, on affiche le détail en dessous
        if let error = throwable {
            print("   └─ 🔴 Error Detail: \(error.message ?? "No message")")
        }
    }
    
    // --- 2. LES MÉTHODES MANQUANTES EXIGÉES PAR SWIFT ---
    
    func d(tag: String, message: String) {
        log(level: .debug, tag: tag, message: message, throwable: nil)
    }
    
    func i(tag: String, message: String) {
        log(level: .info, tag: tag, message: message, throwable: nil)
    }
    
    func w(tag: String, message: String, throwable: KotlinThrowable?) {
        log(level: .warn, tag: tag, message: message, throwable: throwable)
    }
    
    func e(tag: String, message: String, throwable: KotlinThrowable?) {
        log(level: .error, tag: tag, message: message, throwable: throwable)
    }
}
