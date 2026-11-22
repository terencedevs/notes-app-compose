# Notes Maker - Android Assignment (Maincrafts Technology, Task 2)
 
**Notes Maker** is a modern Android notes application built with Kotlin, Jetpack Compose (Material 3), Room Database and MVVM architecture.
 
**Task 2 goal:** a real Android app with **persistent local storage** using Room, full **CRUD** (Create, Read, Update, Delete), a clean **UI → ViewModel → Repository → DAO → Database** architecture, and a UI that **updates automatically** when the data changes. Notes are still there after the app is closed and reopened.
 
---
 
## 🌟 Key Features
 
1. **Room Database Persistence**: Notes are stored locally in SQLite through Room (KSP annotation processor). Notes survive app restarts and app updates.
2. **Add & Edit Notes**: A Compose `AlertDialog` creates new notes or edits existing ones with pre-filled inputs.
3. **Delete with Undo**: Deleting a note removes it from the list and shows a Material 3 `Snackbar` with an **Undo** action.
4. **Live Search**: Real-time search bar that filters notes by title and body as you type.
5. **Pin Notes**: Pin important notes so they stay at the top of the list.
6. **Timestamps**: Formatted timestamps (e.g. `8 Nov, 3:42 PM`) show when notes were created or last edited.
7. **Sorting Options**: Sort by **Newest First**, **Oldest First** or **Title (A-Z)**.
8. **Input Validation**: Title character counter (max 60). Save is disabled for an empty note or an over-long title.
9. **Empty States**: Dedicated empty screens for an empty list and for zero search results.
10. **Light Mode & Dark Mode**: A sun/moon toggle in the top app bar switches the theme. The app follows the phone's system setting by default, and the user's choice is remembered after the app is closed.
11. **Update-Safe Data**: Installing a new version over the old one keeps all saved notes and settings (see *Safe Updates* below).
12. **Material 3 & Edge-to-Edge**: Full Material 3 support, dynamic color on Android 12+, and edge-to-edge layout.
13. **Item Animations**: Smooth list transitions using `Modifier.animateItem()`.
---
 
## 🗄️ Database Architecture (Room + MVVM)
 
### Data flow
 
```
 Jetpack Compose UI  (NotesScreen)
        │   user actions  ▲  collects state
        ▼                 │
   NotesViewModel ── exposes NotesUiState (StateFlow)
        │   coroutines (viewModelScope)
        ▼
   NoteRepository
        │
        ▼
     NoteDao  (@Dao)
        │
        ▼
   AppDatabase (RoomDatabase) ──► SQLite database file on the device
```
 
### What each layer does
 
| Layer | File | Responsibility |
|---|---|---|
| **Entity** (database table) | `Note.kt` | One `Note` object = one row in the `notes` table. Holds the id (auto-generated primary key), title, body, pin status and timestamps. |
| **DAO** (Data Access Object) | `NoteDao.kt` | The database operations: insert, update, delete, and a query that returns notes as a `Flow`. Room generates the SQL code. |
| **Database class** | `AppDatabase.kt` | Room database configuration. A singleton, so only one instance of the database exists in the app. |
| **Repository** | `NoteRepository.kt` | The single source of data for the app. Wraps the DAO so the rest of the app does not know how data is stored. |
| **ViewModel** | `NotesViewModel.kt` | Business logic. Calls the repository inside coroutines and exposes the screen state. Survives screen rotation. |
| **UI State** | `NotesUiState.kt` | A single object describing everything the screen shows (notes, search text, sort option, dialog state). |
| **Compose UI** | `NotesScreen.kt` and the components | Displays the state and sends user actions to the ViewModel. It never touches the database directly. |
 
### Why this design
 
- **Persistence:** Room writes every change to SQLite, so notes are not lost when the app closes.
- **Reactive UI:** the DAO returns a `Flow`. When a note is added, edited or deleted, Room emits a new list, the ViewModel updates the state, and Compose redraws automatically. No manual refresh is needed.
- **Main thread safety:** all database work uses Kotlin coroutines (`suspend` functions and `Flow`), so the UI never freezes.
- **Separation of concerns:** UI, logic and data are in separate layers, which makes the code clean and testable (the unit tests use a fake repository).
### Example: adding a note
 
1. The user fills in the dialog and taps **Save**.
2. The UI calls the ViewModel.
3. The ViewModel launches a coroutine and asks the repository to save the note.
4. The repository calls the DAO, and Room inserts the row into the `notes` table.
5. Room notices the table changed and emits the new list through the `Flow`.
6. The ViewModel updates the UI state, and the list on screen updates by itself.
---
 
## 🌗 Light Mode & Dark Mode
 
- Both a light and a dark Material 3 color scheme are defined in `ui/theme/Theme.kt`.
- Screens use `MaterialTheme.colorScheme` colors, so every screen looks right in both modes.
- The sun/moon toggle sits in the top app bar (`NotesTopAppBar.kt`).
- By default the app follows the system theme. Once the user taps the toggle, their choice is stored on the device and restored the next time the app opens.
---
 
## 🔒 Safe Updates (data is never erased)
 
- The app's package name (`applicationId`) and signing setup are unchanged, so a new version installs as an **update** over the old one.
- The version is raised to `versionCode 2` / `versionName "2.0"`.
- The destructive-migration option (`fallbackToDestructiveMigration`) is **not** used, so Room never wipes the database when the app updates.
- Room **schema export** is enabled, and the schema is saved in the project (`app/schemas/.../1.json`).
- If the `Note` fields change in the future, the database `version` is increased and a proper `Migration` (for example `MIGRATION_1_2`) is written, so existing notes are kept.
---
 
## 🛠️ Tech Stack
 
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: MVVM with Unidirectional Data Flow
- **Database**: Room (SQLite) with KSP
- **Async & State**: Kotlin Coroutines, `Flow` and `StateFlow` collected with `collectAsStateWithLifecycle()`
- **Dependency Management**: Gradle Version Catalog (`gradle/libs.versions.toml`)
- **Unit Testing**: JUnit 4 and `kotlinx-coroutines-test` with a Fake Repository
---
 
## 📁 Project Structure
 
```
NotesMaker/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle/
│   └── libs.versions.toml
└── app/
    ├── build.gradle.kts
    ├── schemas/                      (Room exported schema)
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── java/com/example/notesmaker/
        │   │   ├── MainActivity.kt
        │   │   ├── data/
        │   │   │   ├── local/
        │   │   │   │   ├── Note.kt            (Entity)
        │   │   │   │   ├── NoteDao.kt         (DAO)
        │   │   │   │   └── AppDatabase.kt     (Database)
        │   │   │   └── repository/
        │   │   │       └── NoteRepository.kt  (Repository)
        │   │   └── ui/
        │   │       ├── components/
        │   │       │   ├── EmptyState.kt
        │   │       │   ├── NoteDialog.kt
        │   │       │   ├── NoteItem.kt
        │   │       │   └── NotesTopAppBar.kt
        │   │       ├── screens/
        │   │       │   └── NotesScreen.kt
        │   │       ├── state/
        │   │       │   └── NotesUiState.kt
        │   │       ├── theme/
        │   │       │   ├── Color.kt
        │   │       │   ├── Theme.kt
        │   │       │   └── Type.kt
        │   │       └── viewmodel/
        │   │           └── NotesViewModel.kt
        │   └── res/
        │       └── values/
        │           ├── colors.xml
        │           ├── strings.xml
        │           └── themes.xml
        └── test/
            └── java/com/example/notesmaker/
                ├── FakeNoteRepository.kt
                └── NotesViewModelTest.kt
```
 
---
 
## 📸 Screenshots
 
| Adding a note | Notes list |
|---|---|
| ![Adding a note](screenshots/01-add-note.png) | ![Notes list](screenshots/02-notes-list.png) |
 
| After closing and reopening the app (data persists) | Light mode |
|---|---|
| ![Data persists after restart](screenshots/03-after-restart.png) | ![Light mode](screenshots/04-light-mode.png) |
 
| Dark mode |
|---|
| ![Dark mode](screenshots/05-dark-mode.png) |
 
---
 
## ✅ How to Test Persistence
 
1. Run the app and add a note.
2. Close the app completely (swipe it away from recent apps).
3. Open the app again. The note is still there.
4. Delete a note. The list updates immediately.
5. Switch to Dark Mode, close the app and reopen it. Dark Mode is still selected.
6. Install a newer build over the old one **without uninstalling**. All notes are still there.
---
 
## 🚀 How to Run
 
1. Open **Android Studio** (Ladybug / Jellyfish or newer).
2. Select **Open** and choose the `NotesMaker` project folder.
3. Let Gradle sync the dependencies.
4. Select an emulator or a connected phone (API 24 or higher).
5. Press **Run (Shift + F10)**.
To run the unit tests, right-click `app/src/test/java/com/example/notesmaker/NotesViewModelTest.kt` and select **Run 'NotesViewModelTest'**.
 
---
 
## 📈 How to Extend
 
1. **Categories / Tags**: add a `category` field to the `Note` entity (with a Room migration) and filter chips in the top bar.
2. **Reminders**: use `WorkManager` to schedule local notifications for time-sensitive notes.
3. **Cloud Sync**: sync the local Room notes with a backend such as Firebase Firestore.
4. **Rich Text**: use Compose `AnnotatedString` for bold, italic and checklists.
---
 
## ⚠️ Known Limitations
 
- Notes are stored on one device only (no cloud sync).
- Attachments (images, audio) are not supported by the current `Note` entity.

---

## Author
# Terence Hiten Keniya
 
