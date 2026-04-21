# 🌊 Outgo — KMP Offline-First Showcase

![Status: MVP Path](https://img.shields.io/badge/Status-MVP_In_Progress-blue)
![Kotlin: 2.x](https://img.shields.io/badge/Kotlin-2.x-7F52FF?logo=kotlin)

**Outgo** is a budget management application built for speed and resilience. Designed with a strict **Offline-First** philosophy, it ensures a zero-latency user experience while intelligently synchronizing data with the cloud.

> [!TIP]
> **Technical Showcase:** This project demonstrates a high-level implementation of **Kotlin Multiplatform (KMP)** using a modular architecture and strict Dependency Inversion (API/Impl pattern).

## 🚀 Project Vision
* **Offline-First Core:** The UI never waits for the network. Every action is local and reactive.
* **Unified Architecture:** 95% of the business logic is shared between Android and iOS.
* **Intelligent Sync:** Robust push/pull orchestration with conflict resolution between Local SQLite and Cloud PostgreSQL.

## 🛠 Tech Stack (2026)
* **Core**: Kotlin 2.1+ & Coroutines/Flow.
* **UI**: Compose Multiplatform (Android) & Native SwiftUI (iOS).
* **Persistence**: SQLDelight (Local) + PostgreSQL (Cloud).
* **Backend**: Ktor Server (Deployed on Scaleway Serverless).
* **Auth**: Firebase Auth (Google & Apple Sign-In).
* **DI**: Koin (Modular platform-specific injection).
* **Logic**: `kotlinx-datetime`, `kotlinx-uuid`, and custom Validation Engines.

## 🏗 Modular Architecture
The project is highly decoupled, featuring **20+ specialized modules** following the **API/Implementation** pattern:
* **`:api` modules**: Define interfaces, domain models, and Use Cases. This is the only entry point for the UI layers.
* **`:impl` modules**: Contain private logic (SQL, Ktor, Mappers). These are kept internal to ensure total **Dependency Inversion**.

### Core Pillars
1. **`shared:app`**: The application entry point. Handles global Dependency Injection (Koin) and cross-platform initialization.
2. **`shared:core`**: Time abstraction (`TimeProvider`), unified Exception handling, and local storage.
3. **`shared:database`**: Common SQLDelight schema and drivers (Android/iOS/JVM).
4. **`shared:feature`**: Domain-specific logic (Auth, Wallet management, Sync orchestration).
5. **`shared:presentation`**: UI state management, Presenters (ViewModels), and platform-agnostic UI logic.
6. **`server`**: Ktor-based JVM backend for data persistence and multi-device synchronization.

### 🛠 Modern Build Logic (Convention Plugins)
To manage its modular complexity, Outgo uses a custom **`build-logic`** module. This centralizes Gradle configuration via custom Kotlin DSL plugins, ensuring consistency and type-safety across all modules:
* `outgo.kmp.library`: Unified config for multiplatform modules.
* `outgo.android.library` / `outgo.android.application`: Standardized Android setups.
* `outgo.jvm`: Optimized configuration for the Ktor server and JVM-only modules.

## 💎 Technical Highlights
* **Temporal Projection Engine**: A custom logic layer that transforms complex recurring rules into simple monthly budget snapshots.
* **Sync Orchestrator**: A sophisticated push/pull system with debouncing and network monitoring to optimize battery and data usage.
* **Reactive UI State**: Heavy use of `combine` and `StateFlow` to ensure the UI perfectly mirrors the database state in real-time.

## 📄 License & Copyright

**© 2026 ABK Native (Anthony). All rights reserved.**

The source code in this repository is made public for portfolio and demonstration purposes only.
**No license is granted** for the use, modification, distribution, or commercialization of this code.

**You are welcome to read the code to evaluate my technical skills, but you may not use it to build your own application.

---
Created by **ABK Native**.
[Get in touch](https://www.abknative.fr/contact)