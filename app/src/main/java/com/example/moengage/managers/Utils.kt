package com.example.moengage.managers

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object Utils {

    /**
     * @param inputDateString The input date string to be formatted.
     * @return A formatted date string in the format "MMM d, yyyy 'at' h:mma"
     *         The "AM" and "PM" strings are replaced with "am" and "pm" respectively.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateString(inputDateString: String): String {
        val zonedDateTime = ZonedDateTime.parse(inputDateString)
        val outputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mma", Locale.ENGLISH)
        val outputDateString = zonedDateTime.format(outputFormatter)
        return outputDateString.replace("AM", "am").replace("PM", "pm")
    }
}