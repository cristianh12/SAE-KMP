package com.sae.multiplatform.calendar

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CalendarDateTest {
    @Test fun leapYears() {
        assertTrue(isLeapYear(2024))
        assertFalse(isLeapYear(2100))
        assertTrue(isLeapYear(2000))
        assertEquals(29, daysInMonth(2024, 2))
        assertEquals(28, daysInMonth(2025, 2))
    }

    @Test fun weekStartsOnMonday() {
        assertEquals(0, mondayBasedWeekday(2026, 11, 16))
        assertEquals(6, mondayBasedWeekday(2026, 11, 1))
        assertEquals(3, mondayBasedWeekday(2026, 11, 19))
    }

    @Test fun monthSwitchesYear() {
        assertEquals(CalendarMonth(2027, 1), CalendarMonth(2026, 12).next())
        assertEquals(CalendarMonth(2025, 12), CalendarMonth(2026, 1).previous())
    }

    @Test fun monthHasCorrectGrid() {
        val cells = calendarCells(CalendarMonth(2026, 11))
        assertEquals(42, cells.size)
        assertEquals(CalendarDate(2026, 11, 1), cells[6])
        assertEquals(CalendarDate(2026, 11, 30), cells[35])
    }
}
