# ==========================================
# PROGUARD RULES - OUTGO ANDROID APP
# ==========================================

# --- 1. KOTLINX SERIALIZATION ---
# Preserve annotations and inner classes required for JSON parsing
-keepattributes *Annotation*, InnerClasses

-keepclassmembers class kotlinx.serialization.** {
    *** Companion;
}

# Preserve fields in classes annotated with @Serializable
-keepclasseswithmembers class * {
    @kotlinx.serialization.Serializable <fields>;
}

# --- 2. COROUTINES ---
# Retain generic signatures to prevent crashes with Flow execution
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# --- 3. DATA MODELS (DTOs) ---
# CRITICAL: Prevent obfuscation of DTOs mapped to server JSON responses.
# Changing these variable names will break the Ktor JSON parsing.
-keep class fr.abknative.outgo.wallet.network.dto.** { *; }