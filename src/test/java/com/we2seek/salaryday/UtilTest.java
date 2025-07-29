package com.we2seek.salaryday;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilTest {

    @Test
    void testToTimeAsText() {
        // Test days format
        assertEquals("2 d 4 h 45 m 30 sec", Util.toTimeAsText(189930));

        // Test hours format
        assertEquals("3 h 45 m 30 sec", Util.toTimeAsText(13530));

        // Test minutes format
        assertEquals("45 m 30 sec", Util.toTimeAsText(2730));

        // Test seconds format
        assertEquals("30 sec", Util.toTimeAsText(30));

        // Test zero
        assertEquals("0 ms", Util.toTimeAsText(0));

        // Test edge cases
        assertEquals("1 d 0 h 0 m 0 sec", Util.toTimeAsText(86400));  // Exactly one day
        assertEquals("0 ms", Util.toTimeAsText(-1));  // Negative value
    }

    @Test
    void testGetDaysPlural() {
        // Test singular form (ends with 1, except 11)
        assertEquals("день", Util.getDaysPlural(1));
        assertEquals("день", Util.getDaysPlural(21));
        assertEquals("день", Util.getDaysPlural(31));

        // Test plural form for 2-4 (except 12-14)
        assertEquals("дні", Util.getDaysPlural(2));
        assertEquals("дні", Util.getDaysPlural(3));
        assertEquals("дні", Util.getDaysPlural(4));
        assertEquals("дні", Util.getDaysPlural(22));
        assertEquals("дні", Util.getDaysPlural(23));
        assertEquals("дні", Util.getDaysPlural(24));

        // Test plural form for 5-20
        assertEquals("днів", Util.getDaysPlural(5));
        assertEquals("днів", Util.getDaysPlural(10));
        assertEquals("днів", Util.getDaysPlural(11));
        assertEquals("днів", Util.getDaysPlural(12));
        assertEquals("днів", Util.getDaysPlural(13));
        assertEquals("днів", Util.getDaysPlural(14));
        assertEquals("днів", Util.getDaysPlural(15));
        assertEquals("днів", Util.getDaysPlural(20));

        // Test zero
        assertEquals("днів", Util.getDaysPlural(0));

        // Test larger numbers
        assertEquals("днів", Util.getDaysPlural(111));
        assertEquals("день", Util.getDaysPlural(101));
        assertEquals("дні", Util.getDaysPlural(102));
    }

}