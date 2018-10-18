package com.douglasharvey.fundtracker3.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import timber.log.Timber

//todo add separate table for favourites
@Database(entities = arrayOf(
        Fund::class,
        FundPrice::class,
        FundPriceSummary::class,
        FundPriceOverview::class,
        Favourite::class
),
        views = arrayOf(
                FundList::class),
        version = 22, exportSchema = false)
abstract class FundsRoomDatabase : RoomDatabase() {
    abstract fun fundDao(): FundDao
    abstract fun fundPricesDao(): FundPriceDao
    abstract fun fundPriceSummaryDao(): FundPriceSummaryDao
    abstract fun fundPriceOverviewDao(): FundPriceOverviewDao

    companion object {

        private var INSTANCE: FundsRoomDatabase? = null

        internal fun getDatabase(context: Context): FundsRoomDatabase {
            if (INSTANCE == null) {
                synchronized(FundsRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                FundsRoomDatabase::class.java, "funds_database")
                                .fallbackToDestructiveMigration() //todo temporary
                                .addCallback(CALLBACK)
                                .build()
                    }
                }
            }
            return INSTANCE!!
        }
//todo this also needs to be executed when db version is upgraded?
        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Timber.d("TRIGGER CREATED") // NOT BEING CREATED ON PHONE (MARSHMALLOW 23 VS EMULATOR 28)   - WORKS ON TABLET (NOUGAT 25) - but OPEN works!
                //maybe drop & recreate trigger or only create if it does not exist. but not a good solution, what if a change is required. In any case, need to consider migration
                db.execSQL("CREATE TRIGGER update_summary_table AFTER INSERT ON fund_prices " +
                        "BEGIN " +
                        "   DELETE FROM fund_price_summary WHERE fund_code = new.fund_code; " +
                        "   INSERT INTO fund_price_summary " +
                        "       SELECT fund_code, (latest_price/fund_price-1) * 100, price_date FROM " +
                        "           (SELECT fund_code, fund_price, price_date, (SELECT fund_price FROM fund_prices WHERE " +
                        "                   fund_code = po.fund_code and price_date = (SELECT MAX(price_date) FROM fund_prices " +
                        "                   WHERE fund_code = po.fund_code)) latest_price FROM fund_prices po WHERE po.fund_code = new.fund_code ORDER BY price_date); " +
                        "   DELETE FROM fund_price_overview WHERE fund_code = new.fund_code; " +
                        "   INSERT INTO fund_price_overview " +
                        "       SELECT ps.fund_code, " +
                        "       IFNULL((SELECT fund_price FROM fund_price_summary WHERE fund_code = ps.fund_code and price_date = (SELECT max(price_date) FROM fund_price_summary WHERE fund_code = ps.fund_code and  price_date < max(ps.price_date))),0) one_day, " +
                        "       IFNULL((SELECT fund_price FROM fund_price_summary WHERE fund_code = ps.fund_code and price_date = (SELECT min(price_date) FROM fund_price_summary WHERE fund_code = ps.fund_code and price_date > (SELECT date(max(ps.price_date),'-7 days')))),0) seven_days,  " +
                        "       IFNULL((SELECT fund_price FROM fund_price_summary WHERE fund_code = ps.fund_code and price_date = (SELECT min(price_date) FROM fund_price_summary WHERE fund_code = ps.fund_code and price_date > (SELECT date(max(ps.price_date),'-1 months')))),0) one_month, " +
                        "       IFNULL((SELECT fund_price FROM fund_price_summary WHERE fund_code = ps.fund_code and price_date = (SELECT min(price_date) FROM fund_price_summary WHERE fund_code = ps.fund_code and price_date > (SELECT date(max(ps.price_date),'-3 months')))),0) three_months,  " +
                        "       IFNULL((SELECT fund_price FROM fund_price_summary WHERE fund_code = ps.fund_code and price_date = (SELECT min(price_date) FROM fund_price_summary WHERE fund_code = ps.fund_code and price_date > (SELECT date(max(ps.price_date),'-6 months')))),0) six_months, " +
                        "       IFNULL((SELECT fund_price FROM fund_price_summary WHERE fund_code = ps.fund_code and price_date = (SELECT min(price_date) FROM fund_price_summary WHERE fund_code = ps.fund_code and price_date > (SELECT date(max(ps.price_date),'-12 months')))),0) one_year , " +
                        "       IFNULL((SELECT fund_price FROM fund_price_summary WHERE fund_code = ps.fund_code and price_date = (SELECT min(price_date) FROM fund_price_summary WHERE fund_code = ps.fund_code and price_date > (SELECT date(max(ps.price_date),'-3 years')))),0) three_years, " +
                        "       IFNULL((SELECT fund_price FROM fund_price_summary WHERE fund_code = ps.fund_code and price_date = (SELECT min(price_date) FROM fund_price_summary WHERE fund_code = ps.fund_code and price_date > (SELECT date(max(ps.price_date),'-5 years')))),0) five_years, " +
                        "       IFNULL((SELECT fund_price FROM fund_price_summary WHERE fund_code = ps.fund_code and price_date = (SELECT min(price_date) FROM fund_price_summary WHERE fund_code = ps.fund_code and price_date > (SELECT date(max(ps.price_date),'start of year')))),0) start_of_year, " +
                        "       IFNULL((SELECT (month_end_price2 / month_end_price1 - 1)*100 FROM ( SELECT (SELECT fund_price FROM fund_prices WHERE fund_code = fund.fund_code AND price_date = (SELECT Max(price_date) FROM fund_prices WHERE fund_code = fund.fund_code AND Date(price_date, 'start of month') = ( SELECT Date(Max(price_date), 'start of month', '-2 months' ) FROM fund_prices WHERE fund_code = fund.fund_code))) month_end_price1, (SELECT fund_price FROM fund_prices WHERE fund_code = fund.fund_code AND price_date = (SELECT Max(price_date) FROM fund_prices WHERE fund_code = fund.fund_code AND Date(price_date, 'start of month') = ( SELECT Date(Max(price_date), 'start of month', '-1 months' ) FROM fund_prices WHERE fund_code = fund.fund_code))) month_end_price2 FROM fund WHERE fund_code = ps.fund_code )),0) last_month " +
                        "  FROM fund_price_summary ps WHERE ps.fund_code = new.fund_code; END; ")

//                        " END; ")
            }
        }
    }
}

//select date((select date((select max(price_date) from fund_price_summary where fund_code  = 'TTE'),'start of month','-1 day')) ,'start of month') - gets start of previous month
//inside query gives last day of previous month

//select * from fund_prices where fund_code = 'TTE' and price_date = (select min(price_date) from fund_prices where price_date > '2018-09-01') - todo this might be better
//review how iş te fon did it (assuming it may be wrong)
//TODO SUMMARIZE BY
// 1 day, 7 days, 1 month, 3 months, 6 months, 12 months, yeartodate, others?? 'GEÇEN AYIN GETIRISI'
// tefaş has 3 years & 5 years too!!
// OR store by date

// (SELECT fund_price FROM fund_price_summary WHERE fund_code = 'TTE' and price_date = (SELECT min(price_date) FROM fund_price_summary WHERE fund_code = 'TTE' and price_date > (SELECT date('now','-12 months'))))

// FOR DAILY: SELECT fund_price FROM fund_price_summary WHERE fund_code = 'TTE' and price_date = (SELECT max(price_date) FROM fund_price_summary WHERE fund_code = 'TTE' and  price_date < '2018-10-15')
// need one more table populated by trigger - each column will have a separate summary result.
// yeartodate & last month's income also needed

