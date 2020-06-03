package at.tugraz.ist.sw20.mam3.cook.ui.utils

import android.content.Context
import at.tugraz.ist.sw20.mam3.cook.R
import kotlin.math.floor

class TimeFormatter {
    fun formatMinutesIntoTimeStampString(minutes: Int, ctx: Context) : String {
        val hours = floor(minutes / 60.0).toInt()
        val restMinutes = minutes % 60
        if (restMinutes < 10) {
            return ctx.getString(R.string.time_format_minutes_leading_zero, hours, restMinutes)
        } else {
            return ctx.getString(R.string.time_format, hours, restMinutes)
        }
    }
}