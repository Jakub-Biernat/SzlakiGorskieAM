package com.example.szlakigrskieam.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Trail::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trailDao(): TrailDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_db"
                )
                    .addCallback(object : RoomDatabase.Callback() {

                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.trailDao()?.insertTrails(
                                    listOf(
                                        Trail(
                                            name = "Giewont",
                                            description = "Popularny szlak w Tatrach",
                                            category = "górski",
                                            distance = 6.5,
                                            color = "czerwony"
                                        ),
                                        Trail(
                                            name = "Dolina Kościeliska",
                                            description = "Łatwy spacerowy szlak",
                                            category = "górski",
                                            distance = 8.0,
                                            color = "zielony"
                                        ),
                                        Trail(
                                            name = "Velo Dunajec",
                                            description = "Trasa rowerowa w Małopolsce",
                                            category = "rowerowy",
                                            distance = 60.0,
                                            color = "niebieski"
                                        )
                                    )
                                )
                            }
                        }
                    }).fallbackToDestructiveMigration(false).build()
                INSTANCE = instance
                instance
            }
        }
    }
}