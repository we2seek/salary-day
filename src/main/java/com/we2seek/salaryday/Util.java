package com.we2seek.salaryday;

public class Util {

    /**
     * Перетворює секунди в читабельний текст
     *
     * @param seconds Секунди
     * @return Текст, наприклад "1 d 2 h 3 m 4 sec"
     */
    public static String toTimeAsText(int seconds) {
        if (seconds <= 0) {
            return "0 ms";
        }

        int days = seconds / (24 * 3600);
        int remainingSeconds = seconds % (24 * 3600);

        int hours = remainingSeconds / 3600;
        remainingSeconds = remainingSeconds % 3600;

        int minutes = remainingSeconds / 60;
        int sec = remainingSeconds % 60;

        if (days > 0) {
            return String.format("%d d %d h %d m %d sec", days, hours, minutes, sec);
        }
        if (hours > 0) {
            return String.format("%d h %d m %d sec", hours, minutes, sec);
        }
        if (minutes > 0) {
            return String.format("%d m %d sec", minutes, sec);
        }

        return String.format("%d sec", sec);
    }

    /**
     * Повертає правильний відмінник для кількості днів
     *
     * @param number Кількість днів
     * @return Правильний відмінник
     */
    public static String getDaysPlural(int number) {
        int lastDigit = number % 10;
        int lastTwoDigits = number % 100;
        // Особливий випадок для чисел що закінчуються на 11-14
        if (lastTwoDigits >= 11 && lastTwoDigits <= 14) {
            return "днів";
        }

        // Для інших чисел перевіряємо останню цифру
        return switch (lastDigit) {
            case 1 -> "день";
            case 2, 3, 4 -> "дні";
            default -> "днів";
        };
    }
}
