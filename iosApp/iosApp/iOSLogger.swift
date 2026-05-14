import Foundation
import SharedApp

class iOSLogger: Logger {
    
    func log(level: LogLevel, tag: String, message: String, throwable: KotlinThrowable?) {
        let symbol: String
        switch level {
        case .debug: symbol = "🔍"
        case .info:  symbol = "ℹ️"
        case .warn:  symbol = "⚠️"
        case .error: symbol = "❌"
        }
        
        print("[\(tag)] \(symbol) \(message)")
        
        if let error = throwable {
            print("   └─ 🔴 Error Detail: \(error.message ?? "No message")")
        }
    }
    
    
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
