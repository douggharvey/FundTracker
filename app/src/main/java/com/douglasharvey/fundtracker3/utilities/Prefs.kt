@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.douglasharvey.fundtracker3.utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.douglasharvey.fundtracker3.constants.OLDESTFUNDVALUEDATE
import java.util.*

class Prefs (context: Context) {
    val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val PRICES_DATE = "prices_date"
    val NAMES_UPDATE_DATE = "names_update_date"
    var date = Date(System.currentTimeMillis())

    var pricesDate: String
        get() = prefs.getString(PRICES_DATE, OLDESTFUNDVALUEDATE)
        set(value) = prefs.edit().putString(PRICES_DATE, value).apply()

    var namesUpdateDate: Date
        get() = Date(prefs.getLong(NAMES_UPDATE_DATE, 0))
        set(value) = prefs.edit().putLong(NAMES_UPDATE_DATE, date.time).apply()


}