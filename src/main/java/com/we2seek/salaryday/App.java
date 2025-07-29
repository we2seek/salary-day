package com.we2seek.salaryday;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class App {

    static {
        // налаштування логування, має бути виконано перед використанням логера
        String path = App.class.getClassLoader().getResource("logging.properties").getPath();   
        System.setProperty("java.util.logging.config.file", path);
    }

    private static final int CHECK_HOUR = 9;
    private static final int CHECK_MINUTE = 0;
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable dailyCheckTask = App::runDailyCheck;

        // Обчислюємо початкову затримку до наступних 09:00
        long initialDelay = calculateInitialDelay(CHECK_HOUR, CHECK_MINUTE);
        // long initialDelay = 15;
        logger.info("Запуск щоденної перевірки. Наступна перевірка через " + initialDelay + " секунд.");

        // Плануємо виконання завдання щодня
        scheduler.scheduleAtFixedRate(dailyCheckTask, initialDelay, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
        // scheduler.scheduleAtFixedRate(dailyCheckTask, initialDelay, 15, TimeUnit.SECONDS);

        logger.info("Програма запущена і очікує наступної перевірки.");
    }

    private static void runDailyCheck() {
        LocalDate now = LocalDate.now();
        LocalDate secondWorkingDay = SpecialDateChecker.getSecondWorkingDayOfMonth(now);
        LocalDate firstAvance = SpecialDateChecker.getClosestWorkingDay(now, 15);
        LocalDate secondAvance = SpecialDateChecker.getClosestWorkingDay(now, 25);

        logger.info("Перевірка дня: " + now);

        // Find the closest event and days left
        long daysToSecondWorkingDay = java.time.temporal.ChronoUnit.DAYS.between(now, secondWorkingDay);
        long daysToFirstAvance = java.time.temporal.ChronoUnit.DAYS.between(now, firstAvance);
        long daysToSecondAvance = java.time.temporal.ChronoUnit.DAYS.between(now, secondAvance);

        String eventName = "День зарплати";
        long minDays = daysToSecondWorkingDay;

        if (daysToFirstAvance >= 0 && (minDays < 0 || daysToFirstAvance < minDays)) {
            eventName = "Перший аванс";
            minDays = daysToFirstAvance;
        }
        if (daysToSecondAvance >= 0 && (minDays < 0 || daysToSecondAvance < minDays)) {
            eventName = "Другий аванс";
            minDays = daysToSecondAvance;
        }

        if (minDays == 0) {
            logger.info("Сьогодні: " + eventName);
        } else if (minDays > 0) {
            logger.info("До події \"" + eventName + "\" залишилось " + minDays + " днiв.");
        } else {
            logger.info("Немає майбутніх подій у цьому місяці.");
        }
    }

    /**
     * Обчислює початкову затримку (у секундах) до наступного моменту часу, у секундах
     * CHECK_HOUR:CHECK_MINUTE.
     */
    private static long calculateInitialDelay(int checkHour, int checkMinute) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextCheckTime = now.withHour(checkHour).withMinute(checkMinute).withSecond(0).withNano(0);

        // Якщо поточний час вже пройшов 09:00 сьогодні, плануємо на 09:00 завтра
        if (now.isAfter(nextCheckTime)) {
            nextCheckTime = nextCheckTime.plusDays(1);
        }

        return java.time.Duration.between(now, nextCheckTime).getSeconds();
    }
}
