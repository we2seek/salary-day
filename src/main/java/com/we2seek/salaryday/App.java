package com.we2seek.salaryday;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class App {

    static {
        // налаштування логування, має бути виконано перед використанням логера
        try (InputStream is = App.class.getClassLoader().getResourceAsStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(is);
        } catch (IOException e) {
            System.err.println("Unable to load logging.properties file: " + e.getMessage());
        }
    }

    private static final int CHECK_HOUR = 9;
    private static final int CHECK_MINUTE = 0;
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable dailyCheckTask = App::runDailyCheck;

        // Обчислюємо початкову затримку до наступних 09:00
        int initialDelay = calculateInitialDelay(CHECK_HOUR, CHECK_MINUTE);
        logger.info("Запуск щоденної перевірки. Наступна перевірка через " + Util.toTimeAsText(initialDelay));

        // Плануємо виконання завдання щодня
        scheduler.scheduleAtFixedRate(dailyCheckTask, initialDelay, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
        logger.info("Програма запущена і очікує наступної перевірки.");
    }

    private static void runDailyCheck() {
        LocalDate now = LocalDate.now();
        LocalDate salaryDay = SalaryDateChecker.getSecondWorkingDayOfMonth(now);
        LocalDate firstAvance = SalaryDateChecker.getClosestWorkingDay(now, 15);
        LocalDate secondAvance = SalaryDateChecker.getClosestWorkingDay(now, 25);

        logger.info("Перевірка дня: " + now);

        // Find the closest event and days left
        int daysToSalary = (int) ChronoUnit.DAYS.between(now, salaryDay);
        int daysToFirstAvance = (int) ChronoUnit.DAYS.between(now, firstAvance);
        int daysToSecondAvance = (int) ChronoUnit.DAYS.between(now, secondAvance);

        LocalDate closestDate = salaryDay;
        String eventName = "зарплати";
        int minDays = daysToSalary;

        if (daysToFirstAvance >= 0 && (minDays < 0 || daysToFirstAvance < minDays)) {
            closestDate = firstAvance;
            eventName = "першого авансу";
            minDays = daysToFirstAvance;
        }
        if (daysToSecondAvance >= 0 && (minDays < 0 || daysToSecondAvance < minDays)) {
            closestDate = secondAvance;
            eventName = "другого авансу";
            minDays = daysToSecondAvance;
        }

        if (minDays == 0) {
            logger.info("Сьогодні день " + eventName);
        } else if (minDays > 0) {
            logger.info(String.format("До \"%s\" (%s) залишилось %d %s", eventName, closestDate, minDays, Util.getDaysPlural(minDays)));
        } else {
            logger.info("Немає майбутніх подій у цьому місяці.");
        }
    }

    /**
     * Обчислює початкову затримку до наступного часу старту
     * @param checkHour Години
     * @param checkMinute Хвилини
     * @return Затримка в секундах
     */
    public static int calculateInitialDelay(int checkHour, int checkMinute) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextCheckTime = now.withHour(checkHour).withMinute(checkMinute).withSecond(0).withNano(0);

        // Якщо заданий час вже пройшов сьогодні, плануємо на завтра
        if (now.isAfter(nextCheckTime)) {
            nextCheckTime = nextCheckTime.plusDays(1);
        }

        return (int) Duration.between(now, nextCheckTime).toSeconds();
    }
}
