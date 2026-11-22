# Notes Maker - Android Assignment (Maincrafts Technology, Task 1)

**Notes Maker** is a production-quality, modern Android application built using Kotlin, Jetpack Compose (Material 3), Room Database, and MVVM architecture. The application is designed to be clean, fast, and feature-rich with local data persistence.

---

## 🌟 Key Features

1. **Room Database Persistence**: Notes are persisted locally using SQLite via Room ORM and KSP annotation processor. Notes survive app restarts.
2. **Add & Edit Notes**: Modal Compose `AlertDialog` allows creating new notes or editing existing ones with pre-filled inputs.
3. **Delete with Undo**: Deleting a note instantly removes it from the list and shows a Material 3 `Snackbar` with an **Undo** action to restore the note.
4. **Live Search**: Integrated real-time search bar that filters notes by title and body as you type.
5. **Pin Notes**: Toggle pin status on any card to keep important notes anchored at the top of the list.
6. **Timestamps**: Formatted timestamps (e.g. `8 Nov, 3:42 PM`) displaying when notes were created or last edited.
7. **Sorting Options**: Dropdown menu in the top bar to sort notes by **Newest First**, **Oldest First**, or **Title (A-Z)**.
8. **Input Validation**: Title character counter (max 60 characters). Save button is disabled if both title and body are empty or if title exceeds limit.
9. **Empty States**: Customized empty screens with icons for empty note list and zero search results.
10. **Material 3 & Edge-to-Edge**: Full Material 3 support, dynamic color on Android 12+, Light/Dark mode support, and edge-to-edge UI layout.
11. **Item Animations**: Smooth list layout transitions using `Modifier.animateItem()`.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin 2.1.0
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture Pattern**: MVVM with Unidirectional Data Flow (UDF)
- **Database**: Room 2.6.1 with KSP annotation processing
- **Async & State**: Kotlin Coroutines & `StateFlow` collected via `collectAsStateWithLifecycle()`
- **Dependency Management**: Gradle Version Catalog (`gradle/libs.versions.toml`)
- **Unit Testing**: JUnit 4 & `kotlinx-coroutines-test` with Fake Repository pattern

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
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── java/com/example/notesmaker/
        │   │   ├── MainActivity.kt
        │   │   ├── data/
        │   │   │   ├── local/
        │   │   │   │   ├── Note.kt
        │   │   │   │   ├── NoteDao.kt
        │   │   │   │   └── AppDatabase.kt
        │   │   │   └── repository/
        │   │   │       └── NoteRepository.kt
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

## 🚀 How to Run

1. Open **Android Studio** (Ladybug / Jellyfish or newer).
2. Select **Open** and select the `NotesMaker` project folder.
3. Allow Gradle to sync dependencies.
4. Select an Android Emulator or connected physical device (API 24 or higher).
5. Press **Run (Shift + F10)**.

To run Unit Tests:
- Right-click on `app/src/test/java/com/example/notesmaker/NotesViewModelTest.kt` and select **Run 'NotesViewModelTest'**.

---

## 📈 How to Extend

1. **Categories / Tags**: Add a `category: String` field to `Note` entity and UI chip filters in `NotesTopAppBar`.
2. **Reminders / Notifications**: Integrate `WorkManager` and `AlarmManager` to schedule local push notifications for time-sensitive notes.
3. **Cloud Sync**: Integrate Firebase Firestore or Ktor Client to sync local Room notes with remote backend services.
4. **Rich Text Formatting**: Enhance note body with Jetpack Compose `AnnotatedString` for bold, italic, and checklist bullet points.

---

## ⚠️ Known Limitations

- Single-device storage (no cloud synchronization implemented out of the box).
- Attachment files (images, audio notes) are not supported in the basic entity schema.

---



---
