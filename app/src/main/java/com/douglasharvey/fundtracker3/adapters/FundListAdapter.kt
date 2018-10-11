package com.douglasharvey.fundtracker3.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.douglasharvey.fundtracker3.R
import com.douglasharvey.fundtracker3.application.FundTrackerApplication
import com.douglasharvey.fundtracker3.data.Favourite
import com.douglasharvey.fundtracker3.data.FundList
import com.douglasharvey.fundtracker3.data.FundsRepository
import kotlinx.android.synthetic.main.list_item_funds.view.*

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
            fund_price.text = fund.fundPrice
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
                } else {
                    FundsRepository.getInstance(FundTrackerApplication.instance).deleteFavourite(fund.fundCode)
                    Toast.makeText(context, "${fund.fundCode} removed from favourites", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
