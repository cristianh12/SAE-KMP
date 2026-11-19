package com.sae.multiplatform.calendar

/** Fecha sin dependencia de Android ni Java: reutilizable en commonMain. */
data class CalendarDate(val year: Int, val month: Int, val day: Int) {
    init {
        require(year in 1..9999 && month in 1..12 && day in 1..daysInMonth(year, month)) {
            "Fecha no válida: $year-$month-$day"
        }
    }
}

data class CalendarMonth(val year: Int, val month: Int) {
    init { require(year in 1..9999 && month in 1..12) }
    fun previous(): CalendarMonth = if (month == 1 && year > 1) CalendarMonth(year - 1, 12)
        else if (month > 1) CalendarMonth(year, month - 1) else this
    fun next(): CalendarMonth = if (month == 12 && year < 9999) CalendarMonth(year + 1, 1)
        else if (month < 12) CalendarMonth(year, month + 1) else this
}

fun isLeapYear(year: Int): Boolean = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)

fun daysInMonth(year: Int, month: Int): Int = when (month) {
    2 -> if (isLeapYear(year)) 29 else 28
    4, 6, 9, 11 -> 30
    else -> 31
}

/** 0=lunes, 1=martes, ..., 6=domingo. Algoritmo gregoriano de Sakamoto. */
fun mondayBasedWeekday(year: Int, month: Int, day: Int): Int {
    val offsets = intArrayOf(0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4)
    val adjustedYear = if (month < 3) year - 1 else year
    val sundayBased = (adjustedYear + adjustedYear / 4 - adjustedYear / 100 +
        adjustedYear / 400 + offsets[month - 1] + day) % 7
    return (sundayBased + 6) % 7
}

/** Devuelve 35 o 42 casillas con días vacíos de alineación para una semana que inicia en lunes. */
fun calendarCells(month: CalendarMonth): List<CalendarDate?> {
    val leading = mondayBasedWeekday(month.year, month.month, 1)
    val total = ((leading + daysInMonth(month.year, month.month) + 6) / 7) * 7
    return List(total) { index ->
        val day = index - leading + 1
        if (day in 1..daysInMonth(month.year, month.month))
            CalendarDate(month.year, month.month, day)
        else null
    }
}

val monthNamesEs = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
    "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
