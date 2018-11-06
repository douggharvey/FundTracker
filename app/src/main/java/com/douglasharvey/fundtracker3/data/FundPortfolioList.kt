package com.douglasharvey.fundtracker3.data

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView(value="SELECT portfolio_code, " +
        "    account_number, " +
        "    fund_code,  " +
        "       fund_name,  " +
        "       ( unitsbought - unitssold )                 unit,  " +
        "       current_price,  " +
        "       ( current_price * ( unitsbought - unitssold ) - cost + sold_proceeds )  " +
        "                             profit_loss,  " +
        "       current_price * ( unitsbought - unitssold ) current_value,  " +
        "       cost,  " +
        "       sold_proceeds  " +
        "    FROM   (SELECT  a.account_number account_number, " +
        "  a.portfolio_code portfolio_code, " +
        "      p.fund_code  " +
        "           fund_code,  " +
        "           f.fund_name  " +
        "           fund_name,  " +
        "           p.fund_price  " +
        "           current_price,  " +
        "           Ifnull(Round((SELECT Sum(fund_price * unit)  " +
        "                   FROM   fund_transaction  " +
        "                   WHERE  fund_code = f.fund_code  " +
        "                      AND transaction_type = 'B'  " +
        "                      AND account = a.account_number), 2), 0) cost,  " +
        "           (SELECT Sum(unit)  " +
        "            FROM   fund_transaction  " +
        "            WHERE  fund_code = f.fund_code  " +
        "               AND transaction_type = 'B'  " +
        "                      AND account = a.account_number)  " +
        "           unitsbought,  " +
        "           Ifnull(Round((SELECT Sum(fund_price * unit)  " +
        "                   FROM   fund_transaction  " +
        "                   WHERE  fund_code = f.fund_code  " +
        "                      AND transaction_type = 'S'  " +
        "                      AND account = a.account_number), 2), 0)  " +
        "           sold_proceeds,  " +
        "           Ifnull(Round((SELECT Sum(unit)  " +
        "                   FROM   fund_transaction  " +
        "                   WHERE  fund_code = f.fund_code  " +
        "                      AND transaction_type = 'S'  " +
        "                      AND account = a.account_number), 2), 0)  " +
        "           unitssold  " +
        "        FROM   fund_prices p,  " +
        "           fund f,  " +
        "          account a " +
        "        WHERE  f.fund_code = p.fund_code  " +
        "          AND f.fund_code in (select distinct(fund_code) from fund_transaction) " +
        "           AND p.price_date = (SELECT Max(price_date)  " +
        "                     FROM   fund_prices  " +
        "                     WHERE  fund_prices.fund_code =  " +
        "       p.fund_code))  " +
        "       where cost!=0 " +
        "    GROUP  BY portfolio_code, account_number, fund_code  " +
        "    ORDER  BY portfolio_code, account_number, fund_code ",
        viewName = "fund_portfolio_list")

data class FundPortfolioList(
        @ColumnInfo(name = "portfolio_code") val portfolioCode: String,
        @ColumnInfo(name = "account_number") val accountNumber: String,
        @ColumnInfo(name = "fund_code") val fundCode: String,
        @ColumnInfo(name = "fund_name") val fundName: String,
        @ColumnInfo(name = "unit") val unit: Double,
        @ColumnInfo(name = "current_price") val currentPrice: Double,
        @ColumnInfo(name = "profit_loss") val profitLoss: Double,
        @ColumnInfo(name = "current_value") val currentValue: Double,
        @ColumnInfo(name = "cost") val cost: Double,
        @ColumnInfo(name = "sold_proceeds") val soldProceeds: Double
)
