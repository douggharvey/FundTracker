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
        FundTransaction::class,
        FundPriceOverview::class,
        Favourite::class,
        Account::class,
        Portfolio::class
),
        views = arrayOf(
                FundList::class,
                FundPortfolioList::class
        ),
        version = 38, exportSchema = false)
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
                populateDB(db)
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
             //   populateDB(db)
            }

            private fun populateDB(db: SupportSQLiteDatabase) {
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

                db.execSQL("INSERT INTO PORTFOLIO VALUES (1,'YAPI KREDI')")
                db.execSQL("INSERT INTO PORTFOLIO VALUES (2,'GARANTİ')")
                db.execSQL("INSERT INTO FAVOURITE VALUES ('IST'), ('YAS'),('TTE'),('TI3'),('YLB')")
                db.execSQL("INSERT INTO ACCOUNT VALUES ('TR94 0006 7010 0000 0047 4517 76','1','GENERAL SAVINGS')," +
                        " ('TR22 0006 7010 0000 0025 9055 89','1','WORKING ACCOUNT'), " +
                        " ('TR63 0006 2001 3200 0006 6967 54','2','GARANTI ELMA'), " +
                        "('TR06 0006 7010 0000 0092 3877 37','1','OTHER')")
                db.execSQL("INSERT INTO FUND_TRANSACTION " +
                        "(fund_code, fund_price, unit, transaction_date, transaction_type, account) " +
                        "VALUES  " +
                        "('TTE',0.033592,65000,'2016-04-20','B','TR94 0006 7010 0000 0047 4517 76')," +
                        "('TTE',0.032869,3045000,'2016-04-26','B','TR94 0006 7010 0000 0047 4517 76' )  ," +
                        "('TTE',0.030083,660000,'2016-07-28','B','TR94 0006 7010 0000 0047 4517 76'  )  ," +
                        "('TTE',0.043911,5000,'2017-04-27','B','TR94 0006 7010 0000 0047 4517 76'    )  ," +
                        "('TTE',0.043737,1370000,'2017-04-28','B','TR94 0006 7010 0000 0047 4517 76' )  ," +
                        "('TTE',0.051407,195000,'2017-08-07','B','TR94 0006 7010 0000 0047 4517 76'  )  ," +
                        "('TTE',0.051505,155000,'2017-09-11','B','TR94 0006 7010 0000 0047 4517 76'  )  ," +
                        "('TTE',0.03273,5000,'2016-04-07','B','TR22 0006 7010 0000 0025 9055 89'     )  ," +
                        "('TTE',0.033577,55000,'2016-04-18','B','TR22 0006 7010 0000 0025 9055 89'   )  ," +
                        "('TTE',0.051277,5000,'2018-04-12','B','TR22 0006 7010 0000 0025 9055 89'    )  ," +
                        "('TTE',0.050044,25000,'2017-07-05','B','TR06 0006 7010 0000 0092 3877 37'   )  ," +
                        "('TTE',0.054448,15000,'2017-12-07','B','TR06 0006 7010 0000 0092 3877 37'   )  ," +
                        "('TTE',0.056793,20000,'2018-04-04','B','TR06 0006 7010 0000 0092 3877 37'   )  ," +
                        "('TTE',0.045527,20000,'2018-05-15','B','TR06 0006 7010 0000 0092 3877 37'   )  ," +
                        "('TTE',0.042563,30000,'2018-06-04','B','TR06 0006 7010 0000 0092 3877 37'   )  ," +
                        "('TTE',0.043117,25000,'2018-07-05','B','TR06 0006 7010 0000 0092 3877 37'   )  ," +
                        "('TTE',0.043564,25000,'2018-08-02','B','TR06 0006 7010 0000 0092 3877 37'   )  ," +
                        "('IST',0.019998,100010,'2018-01-18','B','TR22 0006 7010 0000 0025 9055 89'  )  ," +
                        "('IST',0.021546,60336,'2018-07-20','B','TR22 0006 7010 0000 0025 9055 89'   )  ," +
                        "('IST',0.018163,145901,'2017-04-24','B','TR06 0006 7010 0000 0092 3877 37'  )  ," +
                        "('IST',0.018216,43917,'2017-05-03','B','TR06 0006 7010 0000 0092 3877 37'   )  ," +
                        "('IST',0.018615,9606,'2017-07-04','B','TR06 0006 7010 0000 0092 3877 37'    )  ," +
                        "('IST',0.018814,69760,'2017-08-04','B','TR06 0006 7010 0000 0092 3877 37'   )  ," +
                        "('IST',0.019047,68673,'2017-09-08','B','TR06 0006 7010 0000 0092 3877 37'   )  ," +
                        "('IST',0.019424,61779,'2017-11-02','B','TR06 0006 7010 0000 0092 3877 37'   )  ," +
                        "('IST',0.019667,19204,'2017-12-06','B','TR06 0006 7010 0000 0092 3877 37'   )  ," +
                        "('IST',0.01989,60571,'2018-01-04','B','TR06 0006 7010 0000 0092 3877 37'    )  ," +
                        "('IST',0.020132,59587,'2018-02-05','B','TR06 0006 7010 0000 0092 3877 37'   )  ," +
                        "('IST',0.020359,437153,'2018-03-05','S','TR06 0006 7010 0000 0092 3877 37' )  ," +
                        "('IST',0.020604,7968,'2018-04-05','B','TR06 0006 7010 0000 0092 3877 37'    )  ," +
                        "('IST',0.020918,8082,'2018-05-14','B','TR06 0006 7010 0000 0092 3877 37'    )  ," +
                        "('IST',0.020952,10504,'2018-05-18','B','TR06 0006 7010 0000 0092 3877 37'   )  ," +
                        "('IST',0.021382,1946,'2018-07-04','B','TR06 0006 7010 0000 0092 3877 37'    )  ," +
                        "('IST',0.021686,9210,'2018-08-02','B','TR06 0006 7010 0000 0092 3877 37'    )  ," +
                        "('IST',0.017718,246591,'2017-02-07','B','TR94 0006 7010 0000 0047 4517 76'  )  ," +
                        "('IST',0.017753,23641,'2017-02-13','B','TR94 0006 7010 0000 0047 4517 76'   )  ," +
                        "('IST',0.018133,27574,'2017-04-19','B','TR94 0006 7010 0000 0047 4517 76'   )  ," +
                        "('IST',0.018181,2404372,'2017-04-27','B','TR94 0006 7010 0000 0047 4517 76' )  ," +
                        "('IST',0.018395,54363,'2017-05-31','S','TR94 0006 7010 0000 0047 4517 76'  )  ," +
                        "('IST',0.022871,173536,'2018-11-09','B','TR94 0006 7010 0000 0047 4517 76'  )  ," +
                        "('YAS',0.726842,6879,'2015-11-03','B','TR22 0006 7010 0000 0025 9055 89'    )  ," +
                        "('YAS',0.792286,378,'2016-04-06','B','TR22 0006 7010 0000 0025 9055 89'     )  ," +
                        "('YAS',0.83378,2398,'2016-04-12','B','TR22 0006 7010 0000 0025 9055 89'     )  ," +
                        "('YAS',0.825383,1211,'2016-04-14','B','TR22 0006 7010 0000 0025 9055 89'    )  ," +
                        "('YAS',0.829459,1205,'2016-04-18','B','TR22 0006 7010 0000 0025 9055 89'    )  ," +
                        "('YAS',0.826638,203,'2016-04-20','B','TR94 0006 7010 0000 0047 4517 76'     )  ," +
                        "('YAS',0.822235,121619,'2016-04-25','B','TR94 0006 7010 0000 0047 4517 76'  )  ," +
                        "('YAS',0.820966,72,'2016-04-26','B','TR94 0006 7010 0000 0047 4517 76'      )  ," +
                        "('YAS',0.755421,26475,'2016-07-27','B','TR94 0006 7010 0000 0047 4517 76'   )  ," +
                        "('YAS',0.937677,27728,'2017-04-27','B','TR94 0006 7010 0000 0047 4517 76'   )  ," +
                        "('YAS',1.017497,99,'2017-07-27','S','TR94 0006 7010 0000 0047 4517 76'     )  ," +
                        "('TI3',51.737286,386,'2017-04-28','B','TR94 0006 7010 0000 0047 4517 76'    )  ," +
                        "('TI3',58.412213,119,'2017-08-07','B','TR94 0006 7010 0000 0047 4517 76'    )  ," +
                        "('YLB',0.221881,4506,'2018-08-10','B','TR22 0006 7010 0000 0025 9055 89'   )  ," +
                        "('YLB',0.20812,9609,'2018-02-22','B','TR22 0006 7010 0000 0025 9055 89'   )  ," +
                        "('YLB',0.218475,938,'2018-07-06','B','TR06 0006 7010 0000 0092 3877 37'     )  ," +
            //Garanti transactions
                        "('IST',0.019374,9294,  '2017-10-26','S','TR63 0006 2001 3200 0006 6967 54')	,  " +
                        "('TTE',0.051505,15000, '2017-09-11','B','TR63 0006 2001 3200 0006 6967 54') ,     " +
                        "('IST',0.019331,18630, '2017-10-20','S','TR63 0006 2001 3200 0006 6967 54') ,     " +
                        "('IST',0.018408,146739,'2017-06-02','S','TR63 0006 2001 3200 0006 6967 54'),     " +
                        "('IST',0.018497,86500, '2017-06-16','B','TR63 0006 2001 3200 0006 6967 54') ,     " +
                        "('IST',0.018615,86128, '2017-07-04','S','TR63 0006 2001 3200 0006 6967 54') ,     " +
                        "('IST',0.018222,5487,  '2017-05-04','B','TR63 0006 2001 3200 0006 6967 54')  ,     " +
                        "('IST',0.018222,5487,  '2017-05-04','B','TR63 0006 2001 3200 0006 6967 54')  ,     " +
                        "('IST',0.018222,504884,'2017-05-04','B','TR63 0006 2001 3200 0006 6967 54'),     " +
                        "('TTE',0.045066,665000,'2017-05-05','B','TR63 0006 2001 3200 0006 6967 54'),     " +
                        "('IST',0.018043,155504,'2017-04-04','S','TR63 0006 2001 3200 0006 6967 54'),     " +
                        "('IST',0.017718,352592,'2017-02-07','B','TR63 0006 2001 3200 0006 6967 54'),     " +
                        "('IST',0.017753,487801,'2017-02-13','B','TR63 0006 2001 3200 0006 6967 54'),     " +
                        "('IST',0.01785, 100880,'2017-02-03','S','TR63 0006 2001 3200 0006 6967 54'),     " +
                        "('IST',0.021804,4587,  '2018-08-13','S','TR63 0006 2001 3200 0006 6967 54')  ,     " +
                        "('IST',0.022615,301258,'2018-10-22','S','TR63 0006 2001 3200 0006 6967 54')    "
                )
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

//test values
//INSERT INTO FUND_TRANSACTION VALUES ('IST',.021228,2947716,'2018-06-16'),
// ('TI3',58.875781,505,'2018-06-16'),
// ('TTE',0.038319,5720000,'2018-06-16'),
// ('YLB',0.216718,15053,'2018-06-16' ),
// ('YAS',0.844465,188069,'2018-06-16');