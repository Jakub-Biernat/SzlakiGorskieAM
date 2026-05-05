package com.example.szlakigrskieam.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.szlakigrskieam.R

@Database(entities = [Trail::class, TrailTime::class], version = 10)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trailDao(): TrailDao
    abstract fun trailTimeDao(): TrailTimeDao

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
                    .addCallback(object : Callback() {

                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.trailDao()?.insertTrails(
                                    listOf(
                                        Trail(
                                            category = "rowerowy",
                                            name = "Szlak wokół Tatr",
                                            trailStart = "Nowy Targ",
                                            trailEnd = "Chochołów-Sucha",
                                            distance = 21.0,
                                            descRes = R.string.szlak_wokol_tatr,
                                            websiteUrl = "https://www.szlakwokoltatr.eu/",
                                            imageRes = R.drawable.szlak_wokol_tatr
                                        ),
                                        Trail(
                                            category = "rowerowy",
                                            name = "Rowery Szlak Orlich Gniazd",
                                            trailStart = "Częstochowa",
                                            trailEnd = "Kraków",
                                            distance = 190.0,
                                            descRes = R.string.rowery_szlak_orlich_gniazd,
                                            websiteUrl = "https://www.orlegniazda.pl/",
                                            imageRes = R.drawable.rowery_szlak_orlich_gniazd
                                        ),
                                        Trail(
                                            category = "rowerowy",
                                            name = "Wiślana Trasa Rowerowa",
                                            trailStart = "Oświęcim",
                                            trailEnd = "Kraków",
                                            distance = 90.0,
                                            descRes = R.string.wislana_trasa_rowerowa,
                                            websiteUrl = "https://polskanarowerze.pl/wislana-trasa-rowerowa-i/",
                                            imageRes = R.drawable.wislana_trasa_rowerowa
                                        ),
                                        Trail(
                                            category = "rowerowy",
                                            name = "Wzdłuż Bałtyku",
                                            trailStart = "Świnoujście",
                                            trailEnd = "Braniewo",
                                            distance = 590.0,
                                            descRes = R.string.wzdluz_baltyku,
                                            websiteUrl = "https://polskanarowerze.pl/szlak-rowerowy-wzdluz-baltyku/",
                                            imageRes = R.drawable.wzdluz_baltyku
                                        ),
                                        Trail(
                                            category = "rowerowy",
                                            name = "Velo Czorsztyn",
                                            trailStart = "Niedzica",
                                            trailEnd = "Czorsztyn",
                                            distance = 27.5,
                                            descRes = R.string.velo_czorsztyn,
                                            websiteUrl = "https://www.centrumrowerowe.pl/trasy-rowerowe/trasa-rowerowa-velo-czorsztyn/",
                                            imageRes = R.drawable.velo_czorsztyn
                                        ),
                                        Trail(
                                            category = "górski",
                                            name = "Główny Szlak Beskidzki",
                                            trailStart = "Wołosate",
                                            trailEnd = "Ustroń",
                                            distance = 500.0,
                                            descRes = R.string.glowny_szlak_beskidzki,
                                            websiteUrl = "https://www.e-horyzont.pl/blog/glowny-szlak-beskidzki",
                                            imageRes = R.drawable.glowny_szlak_beskidzki
                                        ),
                                        Trail(
                                            category = "górski",
                                            name = "Główny Szlak Sudecki",
                                            trailStart = "Świeradów Zdrój",
                                            trailEnd = "Prudnik",
                                            distance = 440.0,
                                            descRes = R.string.glowny_szlak_sudecki,
                                            websiteUrl = "https://www.e-horyzont.pl/blog/glowny-szlak-sudecki",
                                            imageRes = R.drawable.glowny_szlak_sudecki
                                        ),
                                        Trail(
                                            category = "górski",
                                            name = "Szlak Karpacki",
                                            trailStart = "Rzeszów",
                                            trailEnd = "Grybów",
                                            distance = 425.0,
                                            descRes = R.string.szlak_karpacki,
                                            websiteUrl = "https://www.szumzkoncaswiata.pl/szlak-karpacki-najdzikszy-szlak-w-polsce/",
                                            imageRes = R.drawable.szlak_karpacki
                                        ),
                                        Trail(
                                            category = "górski",
                                            name = "Niebieski Szlak Sudecki",
                                            trailStart = "Szklarska Poręba Dolna",
                                            trailEnd = "schronisko Pasterka",
                                            distance = 388.0,
                                            descRes = R.string.niebieski_szlak_sudecki,
                                            websiteUrl = "https://outdoorsunrise.pl/niebieski-szlak-sudecki-co-warto-o-nim-wiedziec/",
                                            imageRes = R.drawable.niebieski_szlak_sudecki
                                        ),
                                        Trail(
                                            category = "górski",
                                            name = "Szlak Nadmorski Bałtycki",
                                            trailStart = "Świnoujście",
                                            trailEnd = "Żarnowiec",
                                            distance = 378.0,
                                            descRes = R.string.szlak_nadmorski_baltycki,
                                            websiteUrl = "https://pomorskieszlakipttk.pl/szlaki-piesze/nadmorski-baltycki/",
                                            imageRes = R.drawable.szlak_nadmorski_baltycki
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