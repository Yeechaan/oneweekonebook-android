package com.lee.oneweekonebook.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lee.oneweekonebook.database.model.Book

@Database(
    entities = [Book::class],
    version = 3,
    exportSchema = false,
)
abstract class BookDatabase : RoomDatabase() {

    abstract val bookDatabaseDao: BookDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: BookDatabase? = null

        fun getInstance(context: Context): BookDatabase {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a
            // time.
            synchronized(this) {
                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE
                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BookDatabase::class.java,
                        "book_history_database"
                    )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not part of this lesson. You can learn more about
                        // migration with Room in this blog post:
                        // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
                        .fallbackToDestructiveMigration()
                        .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }
                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE book_history_table ADD COLUMN isbn TEXT NOT NULL DEFAULT ''")
    }
}