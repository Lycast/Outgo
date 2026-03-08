# Outgo — Kotlin Multiplatform Showcase

![Status: Under Construction](https://img.shields.io/badge/Status-Work_In_Progress-orange)
![Kotlin: 2.x](https://img.shields.io/badge/Kotlin-2.x-blue?logo=kotlin)

> [!IMPORTANT]
> **Development Status:** This project is currently a **Work in Progress**.
> The **Shared Architecture**, **Database layer**, and **Business Logic** are fully implemented and stable.
> The **UI (Android, iOS, Web)** and **Ktor Server** modules are under active development and are not yet feature-complete.

**Outgo** is a subscription and expense management application built with an **Offline-First** approach and a strict modular architecture. This project serves as a comprehensive demonstration of **Kotlin Multiplatform (KMP)** best practices in 2026.

## Project Vision
The goal is to provide a seamless experience across Android, iOS, and Web, while sharing **95% of business logic**, persistence, and networking—all backed by a robust Ktor server.

## 🛠 Tech Stack
* **Core**: Kotlin Multiplatform (KMP)
* **Android UI**: Jetpack Compose (Native)
* **iOS UI**: SwiftUI (Native Apple)
* **Web UI**: Compose HTML + Tailwind CSS (Kotlin/JS)
* **DI**: Koin (with platform-specific setup for iOS injection)
* **Database**: SQLDelight (Native drivers for each target)
* **Network**: Ktor Client & Server
* **Logic**: `kotlinx-datetime` (precise timezone handling), Coroutines & Flow
* **Build System**: Gradle with **Convention Plugins** (`build-logic`) for centralized, type-safe configuration.

## Architecture
The project follows **Clean Architecture** principles with a feature-based modular structure.

### The API / Implementation Pattern
Each module within `shared/` is split into two parts:
* **`:api`**: Contains interfaces, domain models, and Use Cases. This is the only part exposed to the UI.
* **`:impl`**: Contains concrete logic (SQL, Ktor, etc.). This module is internal to ensure total **Dependency Inversion**.

### Module Structure
```plaintext
├── build-logic          # Convention Plugins (Shared build logic)
├── shared/
│   ├── core/            # Global types and utilities (Time, Exceptions)
│   ├── database/        # SQLDelight persistence layer
│   └── feature/
│       └── outgoing/    # "Expenses" business logic (api/impl)
├── server/              # Ktor Backend (JVM)
└── (androidApp|iosApp)  # Native client applications
```

💎 Technical Highlights
Time Abstraction: Implementation of a TimeProvider to ensure full testability of subscription calculations.

Dependency Inversion: Strict use of Koin to decouple implementations from contracts.

Error Handling: Typed exception system (AppException) mapped to UI states through result flows.

Modern Build Logic: Advanced Gradle configuration using Kotlin DSL and Version Catalogs to eliminate configuration drift.

Quick Start
To run the different components of the project, use the following commands:

Android
Bash
./gradlew :androidApp:installDebug

iOS
Open the iosApp/iosApp.xcworkspace in Xcode and press Cmd + R.
(Note: Ensure you have run ./gradlew :shared:assembleXCFramework first if you are not using the KMP Xcode Plugin).

Web (Compose HTML)
Bash
./gradlew :webApp:jsBrowserDevelopmentRun --continuous

Server (Ktor)
Bash
./gradlew :server:run