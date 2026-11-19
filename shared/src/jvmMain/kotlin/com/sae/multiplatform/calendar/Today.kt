package com.sae.multiplatform.calendar

import java.util.Calendar

actual fun todayLocal(): CalendarDate {
    val current = Calendar.getInstance()
    return CalendarDate(
        current.get(Calendar.YEAR),
        current.get(Calendar.MONTH) + 1,
        current.get(Calendar.DAY_OF_MONTH)
    )
}
