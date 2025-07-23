# Alleymate  

An all-in-one inventory, sales, and event management solution for independent artists at tabling events.

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-7F52FF.svg?style=for-the-badge&logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6-4285F4.svg?style=for-the-badge&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)

---

## 📋 Table of Contents

- [The Problem It Solves](#-the-problem-it-solves)
- [Key Features](#-key-features)
- [Screenshots](#-screenshots)
- [Tech Stack & Architecture](#️-tech-stack--architecture)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Author](#-author)

---

## 💡 The Problem It Solves

Managing inventory, tracking sales, and calculating profits during a busy event can be chaotic. Artists and small vendors often rely on spreadsheets, notebooks, or memory, which can lead to errors and missed opportunities. AlleyMate provides a dedicated, offline-first mobile solution to:

-   **Centralize** product inventory.
-   **Allocate** specific stock for different events.
-   **Track** sales in real-time with a dedicated "Live Sale" mode.
-   **Analyze** performance with insightful reports and dashboards.
-   **Reduce** the mental overhead of event management, allowing creators to focus on their customers.

---

## ✨ Features

| Feature                  | Description                                                                                                                                                             |
| ------------------------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **🏠 Home Dashboard**      | Get an at-a-glance overview of your business, including upcoming events, a snapshot of your current live sale, and key performance indicators.                           |
| **📚 Catalogue Management** | Easily add, edit, and delete products. Organize items with custom categories, upload product images using the camera, and manage master stock levels.                    |
| **🗓️ Event Management**     | Schedule and manage multiple events. Each event gets its own dashboard to track allocated inventory, sales, expenses, and profitability.                                 |
| **📦 Inventory Allocation**  | Select items from your main catalogue and allocate specific quantities to an upcoming event. This keeps your event stock separate from your master inventory.             |
| **🔴 Live Sale Mode**        | A focused UI for processing transactions quickly during an event. Select items, adjust quantities, and confirm sales with just a few taps. |
| **📊 Reports** | Dive deep into your sales data. Filter by event or time period. Instantly see total revenue, profit, and your best-selling items. |
| **💸 Expense Tracking**     | Log event-specific expenses like table fees or travel costs to get a picture of your net profit for each event.                                                    |
| **📱 Offline-First**       | Built with a local Room database, the app works entirely offline.                                        |

---

## 🖼️ Screenshots

| Home Dashboard | Catalogue | Events | Live Sale | Reports |
| :---: | :---: | :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/c37ba534-4d3d-4cc6-84b4-06ef4ea0f398" alt="Home Screen" width="250"> | <img src="https://github.com/user-attachments/assets/018faa30-fa0b-4a50-9b31-157964bb3938" alt="Catalogue Screen" width="250"> | <img src="https://github.com/user-attachments/assets/78562838-b5d0-4067-a5b0-2a06d69d6326" alt="Events Screen" width="250"> | <img src="https://github.com/user-attachments/assets/dd1a2a41-b2c3-48b3-bda2-11e96132a9ee" alt="Live Sale Screen" width="250"> | <img src="https://github.com/user-attachments/assets/a5ed48e3-5e70-4eb0-b086-8510838fa426" alt="Reports Screen" width="250"> |

---

## 🛠️ Tech Stack & Architecture

This project showcases a modern, robust, and scalable Android architecture.

-   **Language:** **[Kotlin](https://kotlinlang.org/)**
-   **UI Toolkit:** **[Jetpack Compose](https://developer.android.com/jetpack/compose)** for a fully declarative and modern UI.
-   **Architecture:** **MVVM (Model-View-ViewModel)** to separate UI logic from business logic.
-   **State Management:** **[StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)** and **ViewModel** to manage and expose UI state reactively.
-   **Navigation:** **[Navigation Compose](https://developer.android.com/jetpack/compose/navigation)** for navigating between screens.
-   **Database:** **[Room](https://developer.android.com/jetpack/androidx/releases/room)** for robust, offline-first data persistence.

---


## 🏁 Getting Started

To build and run this project yourself:
1.  Clone this repository: `git clone https://github.com/your-username/alleymate.git`
2.  Open the project in the latest stable version of Android Studio.
3.  Let Gradle sync and download the required dependencies.
4.  Build and run the app on an Android emulator or a physical device.

---

## 👤 Author

**Isaac Nathan Roman**
- BSCS-ST, De La Salle University
