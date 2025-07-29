package com.we2seek.salaryday;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.Test;

public class SpecialDateCheckerTest {

    // --- Тести для getSecondWorkingDayOfMonth ---
    @Test
    void testGetSecondWorkingDayOfMonth() {
        // arrange
        LocalDate jan = LocalDate.of(2025, Month.JANUARY, 1);
        LocalDate feb = LocalDate.of(2025, Month.FEBRUARY, 1);
        LocalDate mar = LocalDate.of(2025, Month.MARCH, 1);
        LocalDate apr = LocalDate.of(2025, Month.APRIL, 1);
        LocalDate may = LocalDate.of(2025, Month.MAY, 1);
        LocalDate jun = LocalDate.of(2025, Month.JUNE, 1);
        LocalDate jul = LocalDate.of(2025, Month.JULY, 1);
        LocalDate aug = LocalDate.of(2025, Month.AUGUST, 1);
        LocalDate sep = LocalDate.of(2025, Month.SEPTEMBER, 1);
        LocalDate oct = LocalDate.of(2025, Month.OCTOBER, 1);
        LocalDate nov = LocalDate.of(2025, Month.NOVEMBER, 1);
        LocalDate dec = LocalDate.of(2025, Month.DECEMBER, 1);

        // act & assert
        assertNull(SpecialDateChecker.getSecondWorkingDayOfMonth(null));
        assertEquals(LocalDate.of(2025, Month.JANUARY, 2), SpecialDateChecker.getSecondWorkingDayOfMonth(jan));
        assertEquals(LocalDate.of(2025, Month.FEBRUARY, 4), SpecialDateChecker.getSecondWorkingDayOfMonth(feb));
        assertEquals(LocalDate.of(2025, Month.MARCH, 4), SpecialDateChecker.getSecondWorkingDayOfMonth(mar));
        assertEquals(LocalDate.of(2025, Month.APRIL, 2), SpecialDateChecker.getSecondWorkingDayOfMonth(apr));
        assertEquals(LocalDate.of(2025, Month.MAY, 2), SpecialDateChecker.getSecondWorkingDayOfMonth(may));
        assertEquals(LocalDate.of(2025, Month.JUNE, 3), SpecialDateChecker.getSecondWorkingDayOfMonth(jun));
        assertEquals(LocalDate.of(2025, Month.JULY, 2), SpecialDateChecker.getSecondWorkingDayOfMonth(jul));
        assertEquals(LocalDate.of(2025, Month.AUGUST, 4), SpecialDateChecker.getSecondWorkingDayOfMonth(aug));
        assertEquals(LocalDate.of(2025, Month.SEPTEMBER, 2), SpecialDateChecker.getSecondWorkingDayOfMonth(sep));
        assertEquals(LocalDate.of(2025, Month.OCTOBER, 2), SpecialDateChecker.getSecondWorkingDayOfMonth(oct));
        assertEquals(LocalDate.of(2025, Month.NOVEMBER, 4), SpecialDateChecker.getSecondWorkingDayOfMonth(nov));
        assertEquals(LocalDate.of(2025, Month.DECEMBER, 2), SpecialDateChecker.getSecondWorkingDayOfMonth(dec));
    }

    // --- Тести для isWorkingDay ---
    @Test
    public void testIsWorkingDay() {
        assertTrue(SpecialDateChecker.isWorkingDay(LocalDate.of(2025, Month.JULY, 21)));
        assertTrue(SpecialDateChecker.isWorkingDay(LocalDate.of(2025, Month.JULY, 22)));
        assertTrue(SpecialDateChecker.isWorkingDay(LocalDate.of(2025, Month.JULY, 23)));
        assertTrue(SpecialDateChecker.isWorkingDay(LocalDate.of(2025, Month.JULY, 24)));
        assertTrue(SpecialDateChecker.isWorkingDay(LocalDate.of(2025, Month.JULY, 25)));
        assertFalse(SpecialDateChecker.isWorkingDay(LocalDate.of(2025, Month.JULY, 26)));
        assertFalse(SpecialDateChecker.isWorkingDay(LocalDate.of(2025, Month.JULY, 27)));
    }

    // --- Тести для getClosestWorkingDay ---
    @Test
    void getClosestWorkingDay_targetIsWeekday() {
        // Жовтень 2024: 15 (Вт) - робочий день
        LocalDate dateInOctober = LocalDate.of(2024, 10, 1);
        LocalDate expectedDate = LocalDate.of(2024, 10, 15);
        assertEquals(expectedDate, SpecialDateChecker.getClosestWorkingDay(dateInOctober, 15));
    }

    @Test
    void getClosestWorkingDay_targetIsSaturday() {
        // Лютий 2025: 15 (Сб) -> 17 (Пн)
        LocalDate dateInFebruary = LocalDate.of(2025, 2, 1);
        LocalDate expectedDate = LocalDate.of(2025, 2, 17); // Наступний робочий день
        assertEquals(expectedDate, SpecialDateChecker.getClosestWorkingDay(dateInFebruary, 15));
    }

    @Test
    void getClosestWorkingDay_targetIsSunday() {
        // 27 липня 2025 це неділя, отже, 28-ме
        LocalDate dateInJuly = LocalDate.of(2025, 7, 1);
        LocalDate expectedDate = LocalDate.of(2025, 7, 28); 
        assertEquals(expectedDate, SpecialDateChecker.getClosestWorkingDay(dateInJuly, 27));
    }

    @Test
    void getClosestWorkingDay_targetIsFridayAndNextDayIsSaturday() {
        // Січень 2025: 31 (Пт)
        LocalDate dateInJanuary = LocalDate.of(2025, 1, 1);
        LocalDate expectedDate = LocalDate.of(2025, 1, 31);
        assertEquals(expectedDate, SpecialDateChecker.getClosestWorkingDay(dateInJanuary, 31));
    }

    @Test
    void getClosestWorkingDay_shouldHandleNullDate() {
        assertNull(SpecialDateChecker.getClosestWorkingDay(null, 15), "Повертає null для null дати");
    }
}
