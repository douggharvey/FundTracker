package com.douglasharvey.fundtracker3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.douglasharvey.fundtracker3.FundValue
import com.douglasharvey.fundtracker3.R
import com.douglasharvey.fundtracker3.api.FundInterface
import com.douglasharvey.fundtracker3.api.ServiceGenerator
import com.douglasharvey.fundtracker3.application.FundTrackerApplication
import com.douglasharvey.fundtracker3.constants.oldestFundValueDate
import com.douglasharvey.fundtracker3.data.Favourite
import com.douglasharvey.fundtracker3.data.FundList
import com.douglasharvey.fundtracker3.data.FundPrice
import com.douglasharvey.fundtracker3.data.FundsRepository
import kotlinx.android.synthetic.main.list_item_funds.view.*
import kotlinx.coroutines.*
import java.util.*

// Refer https://github.com/antoniolg/Kotlin-for-Android-Developers/blob/master/app/src/main/java/com/antonioleiva/weatherapp/ui/adapters/ForecastListAdapter.kt
// https://antonioleiva.com/recyclerview-adapter-kotlin/
class FundListAdapter(private val itemClick: (FundList) -> Unit) :
        androidx.recyclerview.widget.RecyclerView.Adapter<FundListAdapter.ViewHolder>() {
    private var fundList: List<FundList>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_funds, parent, false), itemClick)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindFund(fundList!![position])
    }

    fun setFunds(fundList: List<FundList>?) {
        this.fundList = fundList
        notifyDataSetChanged()
    }

    override fun getItemCount() = if (fundList == null) 0 else fundList!!.size //todo

    class ViewHolder(itemView: View, private val itemClick: (FundList) -> Unit)
        : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindFund(fund: FundList) = with(itemView) {
            fund_code.text = fund.fundCode
            fund_name.text = fund.fundName
            one_day.text = fund.oneDay
            seven_days.text = fund.sevenDays
            one_month.text = fund.oneMonth
            three_months.text = fund.threeMonths
            six_months.text = fund.sixMonths
            one_year.text = fund.oneYear
            three_years.text = fund.threeYears
            five_years.text = fund.fiveYears
            last_month.text = fund.lastMonth
            start_of_year.text = fund.startYear
            // todo override onInterceptTouchEvent, get X position and then apply to all views in recyclerview with  horizontal_scrollview.scrollTo(1000,0)

            if (fund.oneDay.equals("0")) horizontal_scrollview.visibility = View.GONE else horizontal_scrollview.visibility = View.VISIBLE
            if (fund.fundPrice.equals("0")) fund_price.text = " " else fund_price.text = fund.fundPrice
            favourites_button.isFavorite = (fund.fundCode.equals(fund.favFundCode))

            itemView.setOnClickListener { itemClick(fund) }

            //Avoiding using library's listener as other rows were fired unnecessarily
            //https@ //github.com/IvBaranov/MaterialFavoriteButton/issues/11
            favourites_button.setOnClickListener {
                favourites_button.isFavorite = !favourites_button.isFavorite //toggle

                val favouriteOnOff: Boolean = favourites_button.isFavorite

                if (favouriteOnOff) {
                    FundsRepository.getInstance(FundTrackerApplication.instance).insertFavourite(Favourite(fund.fundCode))
                    Toast.makeText(context, "${fund.fundCode} added to favourites", Toast.LENGTH_LONG).show()
                    GlobalScope.launch { readNewFavouriteValues(fund.fundCode) }
                } else {
                    FundsRepository.getInstance(FundTrackerApplication.instance).deleteFavourite(fund.fundCode)
                    Toast.makeText(context, "${fund.fundCode} removed from favourites", Toast.LENGTH_LONG).show()
                }
            }
        }

        suspend private fun readNewFavouriteValues(fundCode:String) = runBlocking {
            val fundInterface = ServiceGenerator.createService(FundInterface::class.java)
            launch(Dispatchers.IO) {
                val fundValueArrayList: ArrayList<FundValue> = fundInterface.values(fundCode, oldestFundValueDate, "2035-01-01").await()
                val fundPriceList: ArrayList<FundPrice> = arrayListOf()
                for (fundValue: FundValue in fundValueArrayList) {
                    fundPriceList.add(FundPrice(fundValue.fundCode, fundValue.unitValue, fundValue.valueDate))
                }
                FundsRepository.getInstance(FundTrackerApplication.instance).bulkInsertFundPrice(fundPriceList)
            }

        }

    }
}
