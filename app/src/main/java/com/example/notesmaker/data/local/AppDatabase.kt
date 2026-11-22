package com.example.notesmaker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Main Room Database for the Notes application.
 * Holds table "notes" and provides access to [NoteDao].
 *
 * Migration Guidance:
 * When modifying database schema in future releases (e.g., adding/modifying fields in [Note]):
 * 1. Increment database version number (e.g., version = 2).
 * 2. Define a Migration object, for example:
 *    val MIGRATION_1_2 = object : Migration(1, 2) {
 *        override fun migrate(db: SupportSQLiteDatabase) {
 *            db.execSQL("ALTER TABLE notes ADD COLUMN category TEXT NOT NULL DEFAULT ''")
 *        }
 *    }
 * 3. Add the migration to the builder: `.addMigrations(MIGRATION_1_2)`.
 * 4. NEVER use fallbackToDestructiveMigration() so existing user data is preserved safely.
 */
@Database(entities = [Note::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notes_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

