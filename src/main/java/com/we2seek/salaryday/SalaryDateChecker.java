package com.we2seek.salaryday;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class SalaryDateChecker {

    /**
     * Отримати дату зарплати (2-й робочий день місяця).
     * @param date Дата (місяць), для якої потрібно виконати перевірку
     * @return Дата зарплати
     */
    public static LocalDate getSecondWorkingDayOfMonth(LocalDate date) {
        if (date == null) {
            return null;
        }
        LocalDate firstDayOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
        int workingDaysCount = 0;
        LocalDate currentDay = firstDayOfMonth;
        while (workingDaysCount < 2) {
            if (isWorkingDay(currentDay)) {
                workingDaysCount++;
            }
            if (workingDaysCount < 2) { // Щоб не перевіряти наступний день, якщо ми вже знайшли другий робочий
                currentDay = currentDay.plusDays(1);
            }
        }
        return currentDay;
    }

    /**
     * Отримати дату найближчого робочого дня до заданої дати.
     * @param date Дата (місяць), для якої потрібно виконати перевірку
     * @param dayOfMonth День місяця, до якого потрібно знайти найближчий робочий день
     * @return Дата найближчого робочого дня
     */
    public static LocalDate getClosestWorkingDay(LocalDate date, int dayOfMonth) {
        if (date == null) {
            return null;
        }
        LocalDate result = date.withDayOfMonth(dayOfMonth);
        while (!isWorkingDay(result)) {
            result = result.plusDays(1);
        }
        return result;
    }

    /**
     * Перевірити, чи є дата робочим днем.
     * @param date Дата для перевірки
     * @return true, якщо дата є робочим днем, false - в іншому випадку
     */
    public static boolean isWorkingDay(LocalDate date) {
        if (date == null) {
            return false;
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
}