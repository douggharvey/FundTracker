package com.douglasharvey.fundtracker3.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//todo add separate table for favourites
@Database(entities = arrayOf(
        Fund::class,
        FundPrice::class,
        Favourite::class
),
        views = arrayOf(
                FundList::class),
        version = 11, exportSchema = false)
abstract class FundsRoomDatabase : RoomDatabase() {
    abstract fun fundDao(): FundDao
    abstract fun fundPricesDao(): FundPriceDao

    companion object {

        private var INSTANCE: FundsRoomDatabase? = null

        internal fun getDatabase(context: Context): FundsRoomDatabase {
            if (INSTANCE == null) {
                synchronized(FundsRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                FundsRoomDatabase::class.java, "funds_database")
                                .fallbackToDestructiveMigration() //todo temporary
                                .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

}
    
