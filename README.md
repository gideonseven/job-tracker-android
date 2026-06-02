# Job Tracker — Android

A production-quality Android app for tracking job applications through their full lifecycle. Built with a multi-module Clean Architecture, Jetpack Compose, and a complete Firebase integration stack including offline-first sync, push notifications, feature flags, crash reporting, and performance monitoring.

> Inspired by *Building Mobile Apps at Scale* by Gergely Orosz — implemented to cover 13 hands-on engineering challenges from the book.

---

## Features

- Track applications from saved to offer/rejection with enforced status transitions
- Add, edit, and delete applications with full form validation
- Pull-to-refresh sync with remote API
- Offline-first support — queue changes locally, sync automatically when back online
- Interview reminder notifications via Firebase Cloud Messaging
- Feature flag-gated dashboard (statistics screen behind `DASHBOARD_STATISTICS` flag)
- Soft-delete pattern for safe offline/online reconciliation
- Real-time analytics, crash reporting, and performance monitoring

### Status Transition Flow

```
SAVED → APPLIED → PHONE_SCREEN → TECHNICAL → FINAL_ROUND → OFFER
                ↘              ↘            ↘             ↘
              REJECTED       REJECTED    REJECTED       REJECTED
              GHOSTED        GHOSTED     GHOSTED
                ↑
            (GHOSTED → APPLIED to re-engage)
```

---

## Tech Stack

| Category | Technology |
|---|---|
| Language | Kotlin 2.0.21 |
| UI | Jetpack Compose (BOM 2024.09.00) |
| Architecture | Multi-module Clean Architecture |
| DI | Hilt 2.51.1 |
| Database | Room 2.7.1 |
| Networking | Retrofit 2.11.0 + OkHttp |
| Background Work | WorkManager + Hilt Workers |
| Navigation | Jetpack Compose Navigation 2.9.0 (type-safe + deeplinks) |
| Analytics | Firebase Analytics |
| Crash Reporting | Firebase Crashlytics |
| Feature Flags | Firebase Remote Config |
| Performance | Firebase Performance Monitoring |
| Push Notifications | Firebase Cloud Messaging |
| Logging | Timber 5.0.1 |
| Testing | JUnit 4, MockK, Compose UI Tests, WorkManager Testing |
| Min SDK | 26 (Android 8.0) |
| Target / Compile SDK | 36 |

---

## Module Structure

```
job-tracker-android/
├── app/                    # Entry point, navigation graph, DI setup, FCM service
├── core-domain/            # Pure Kotlin — business logic, use cases, repository interfaces
├── core-data/              # Room DB, Retrofit API, SyncWorker, ReminderWorker
├── core-ui/                # Shared Compose theme (JobTrackerTheme), no business logic
├── feature-applications/   # Applications list screen + detail screen
├── feature-addedit/        # Add/edit application form
```

**Dependency rules:**
- `core-domain` has zero Android dependencies (pure Kotlin + coroutines)
- `core-data` and features depend on domain, not on each other
- Features only depend on core modules, never on other features

---

## Dependencies

### Core Android

```toml
androidx-core-ktx = "1.18.0"
androidx-appcompat = "1.7.1"
androidx-lifecycle-runtime-ktx = "2.10.0"
androidx-lifecycle-viewmodel-compose = "2.10.0"
androidx-activity-compose = "1.13.0"
androidx-startup = "1.2.0"
androidx-profileinstaller = "1.4.1"
```

### Jetpack Compose

```toml
androidx-compose-bom = "2024.09.00"
# Includes: ui, ui-graphics, ui-tooling, material3
```

### Navigation

```toml
androidx-navigation-compose = "2.9.0"
androidx-hilt-navigation-compose = "1.2.0"
```

### Dependency Injection (Hilt)

```toml
hilt-android = "2.51.1"
hilt-compiler = "2.51.1"
androidx-hilt-work = "1.2.0"
javax-inject = "1"
```

### Database (Room)

```toml
androidx-room-runtime = "2.7.1"
androidx-room-ktx = "2.7.1"
androidx-room-compiler = "2.7.1"  # via KSP
```

### Networking

```toml
retrofit = "2.11.0"
retrofit-converter-gson = "2.11.0"
okhttp-logging-interceptor = "4.12.0"
```

### Serialization & Coroutines

```toml
kotlinx-serialization-json = "1.7.3"
kotlinx-coroutines-android = "1.8.1"
kotlinx-coroutines-test = "1.8.1"
```

### Background Work

```toml
androidx-work-runtime-ktx = "2.10.0"
```

### Firebase

```toml
firebase-bom = "33.13.0"
# firebase-messaging-ktx
# firebase-analytics-ktx
# firebase-crashlytics-ktx
# firebase-config-ktx
# firebase-perf-ktx
```

### Logging & Testing

```toml
timber = "5.0.1"
junit = "4.13.2"
androidx-test-ext-junit = "1.3.0"
espresso-core = "3.7.0"
mockk = "1.13.10"
```

### Build Tooling

```toml
agp = "8.13.2"
kotlin = "2.0.21"
ksp = "2.0.21-1.0.25"
google-services = "4.4.2"
firebase-crashlytics-plugin = "3.0.3"
firebase-perf-plugin = "1.4.2"
```

---

## Architecture Overview

### Data Flow

```
UI (Compose) ←→ ViewModel (StateFlow) ←→ UseCase (domain) ←→ Repository ←→ Room / Retrofit
```

### Offline Sync Strategy

- All writes go to Room first (`pendingSync = true`)
- `SyncWorker` (WorkManager) runs on network restore, pushing local changes and pulling remote state
- Soft-delete: `deletedAt` timestamp instead of hard delete — safe for offline queuing
- Conflict resolution: `updatedAt` + `pendingSync` flag — local edits are never silently overwritten

### Feature Flags

Three flags managed via Firebase Remote Config:

| Flag | Default | Purpose |
|---|---|---|
| `ADD_EDIT_NOTES` | `true` | Show notes field in add/edit form |
| `EXPORT_APPLICATIONS` | `false` | Export applications to CSV/PDF |

### Deeplinks

| Route | Destination |
|---|---|
| `jobtracker://applications` | Applications list |
| `jobtracker://applications/{id}` | Application detail |
| `jobtracker://addedit` | Add new application |
| `jobtracker://addedit?applicationId={id}` | Edit existing application |

---

## Android Permissions

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```

---

## Getting Started

### Prerequisites

- Android Studio Meerkat or later
- JDK 11+
- A `google-services.json` file from your Firebase project placed in `app/`

### Setup

1. Clone the repository
2. Add your `google-services.json` to `app/`
3. Set your API base URL in `core-data/src/main/java/.../network/NetworkModule.kt`
4. Sync Gradle and run on a device or emulator (API 26+)

### Build Variants

| Variant | Minify | Crashlytics | StrictMode |
|---|---|---|---|
| `debug` | No | Disabled | Enabled |
| `release` | Yes (R8) | Enabled | Disabled |

---

## Testing

```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

**Coverage:**
- `StatusTransitionManagerTest` — validates all valid and invalid status transitions
- `ApplicationsScreenTest` — Compose UI test for the applications list
- `SyncWorkerTest` — WorkManager integration tests

---

## APK Size

R8 full-mode minification and resource shrinking are enabled in release builds.

| Build | Size |
|---|---|
| Debug (no minify) | ~17 MB |
| Release (R8 + shrink) | ~3.1 MB |