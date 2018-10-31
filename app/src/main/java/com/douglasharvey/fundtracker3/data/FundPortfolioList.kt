package com.douglasharvey.fundtracker3.data

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView(value = "select fund_code, fund_name, " +
                "(unitsbought - unitssold) unit, " +
                "current_price, " +
                "(current_price * (unitsbought - unitssold) - cost + sold_proceeds) profit_loss, " +
                "current_price * (unitsbought - unitssold) current_value, " +
                "cost, " +
                "sold_proceeds " +
                " from " +
                "(SELECT p.fund_code         fund_code,  " +
                "    f.fund_name fund_name, " +
                "               p.fund_price        current_price,  " +
                "      IFNULL(Round((select sum(fund_price * unit)  from fund_transaction where fund_code = f.fund_code and transaction_type = 'B'),2),0) cost, " +
                "      (select sum(unit)  from fund_transaction where fund_code = f.fund_code and transaction_type = 'B')  unitsbought, " +
                "IFNULL(Round((select sum(fund_price * unit)  from fund_transaction where fund_code = f.fund_code and transaction_type = 'S'),2),0) sold_proceeds     , " +
                "IFNULL(Round((select sum(unit)  from fund_transaction where fund_code = f.fund_code and transaction_type = 'S'),2),0) unitssold      " +
                "        FROM   fund_prices p , " +
                "      fund f  " +
                "        WHERE  f.fund_code = p.fund_code " +
                "    AND p.price_date = (SELECT Max(price_date)  " +
                "                                   FROM   fund_prices  " +
                "                                   WHERE  fund_prices.fund_code = p.fund_code)" +
        ") " +
        "GROUP  BY fund_code " +
        "ORDER  BY fund_code ",
        viewName = "fund_portfolio_list")
data class FundPortfolioList(
        @ColumnInfo(name = "fund_code") val fundCode: String,
        @ColumnInfo(name = "fund_name") val fundName: String,
        @ColumnInfo(name = "unit") val unit: Double,
        @ColumnInfo(name = "current_price") val currentPrice: Double,
        @ColumnInfo(name = "profit_loss") val profitLoss: Double,
        @ColumnInfo(name = "current_value") val currentValue: Double,
        @ColumnInfo(name = "cost") val cost: Double,
        @ColumnInfo(name = "sold_proceeds") val soldProceeds: Double
)
