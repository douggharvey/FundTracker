package com.douglasharvey.fundtracker3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.douglasharvey.fundtracker3.R
import com.douglasharvey.fundtracker3.data.FundPortfolioList
import kotlinx.android.synthetic.main.list_item_portfolio.view.*

// Refer https://github.com/antoniolg/Kotlin-for-Android-Developers/blob/master/app/src/main/java/com/antonioleiva/weatherapp/ui/adapters/ForecastListAdapter.kt
// https://antonioleiva.com/recyclerview-adapter-kotlin/
class FundPortfolioListAdapter(private val itemClick: (FundPortfolioList) -> Unit) :
        androidx.recyclerview.widget.RecyclerView.Adapter<FundPortfolioListAdapter.ViewHolder>() {
    private var fundPortfolioList: List<FundPortfolioList>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_portfolio, parent, false), itemClick)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindFund(fundPortfolioList!![position])
    }

    fun setFundPortfolioList(fundList: List<FundPortfolioList>?) {
        this.fundPortfolioList = fundList
        notifyDataSetChanged()
    }

    override fun getItemCount() = if (fundPortfolioList == null) 0 else fundPortfolioList!!.size //todo

    class ViewHolder(itemView: View, private val itemClick: (FundPortfolioList) -> Unit)
        : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bindFund(fund: FundPortfolioList) = with(itemView) {
            fund_code.text = fund.fundCode
            fund_name.text = fund.fundName
            unit.text = "%.0f".format(fund.unit)
            cost.text = "%.2f".format(fund.cost)
            total.text = "%.2f".format(fund.currentValue)
            current_price.text = "%.6f".format(fund.currentPrice)
            profit_loss.text = "%.2f".format(fund.profitLoss)
            sold_proceeds.text = "%.2f".format(fund.soldProceeds)
//        @ColumnInfo(name = "current_price") val fundPrice: Double,
//        @ColumnInfo(name = "profit_loss") val priceDate: Double,
//        @ColumnInfo(name = "current_value") val oneDay: Double,
//        @ColumnInfo(name = "cost") val sevenDays: Double

        }
    }
}
