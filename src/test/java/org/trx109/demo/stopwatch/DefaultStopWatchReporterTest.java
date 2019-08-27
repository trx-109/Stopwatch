package org.trx109.demo.stopwatch;

import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultStopWatchReporterTest
{

    @Test
    public void calcElapsedTime()
    {
        final DefaultStopWatchReporter reporter = new DefaultStopWatchReporter();

        // 123ms
        int[] result = reporter.calcElapsedTime(0L, 123 * 1000000L);
        assertEquals(0, result[0]);
        assertEquals(0, result[1]);
        assertEquals(0, result[2]);
        assertEquals(0, result[3]);
        assertEquals(123, result[4]);

        // 2day 7hour 5min 20s 398ms
        result = reporter.calcElapsedTime(0L, ((((2 * 24L + 7) * 60 + 5) * 60 + 20) * 1000 + 398) * 1000000L);
        assertEquals(2, result[0]);
        assertEquals(7, result[1]);
        assertEquals(5, result[2]);
        assertEquals(20, result[3]);
        assertEquals(398, result[4]);

        // 2day 5min 398ms
        result = reporter.calcElapsedTime(0L, ((2 * 24L * 60 + 5) * 60 * 1000 + 398) * 1000000L);
        assertEquals(2, result[0]);
        assertEquals(0, result[1]);
        assertEquals(5, result[2]);
        assertEquals(0, result[3]);
        assertEquals(398, result[4]);
    }

    @Test
    public void describeTime()
    {
        final DefaultStopWatchReporter reporter = new DefaultStopWatchReporter();

        assertEquals("", reporter.describeTime(0, "minute"));
        assertEquals("1 minute", reporter.describeTime(1, "minute"));
        assertEquals("2 minutes", reporter.describeTime(2, "minute"));
        assertEquals("20 minutes", reporter.describeTime(20, "minute"));
        assertEquals("", reporter.describeTime(-5, "minute"));
    }

    @Test
    public void formatElapseTime()
    {
        final DefaultStopWatchReporter reporter = new DefaultStopWatchReporter();

        int[] result = reporter.calcElapsedTime(0L, ((((2 * 24L + 1) * 60 + 5) * 60 + 20) * 1000 + 398) * 1000000L);
        assertEquals("2 days 1 hour 5 minutes 20 seconds 398 milliseconds", reporter.formatElapseTime(result));

        result = reporter.calcElapsedTime(0L, ((2 * 24L * 60 + 5) * 60 * 1000 + 398) * 1000000L);
        assertEquals("2 days 5 minutes 398 milliseconds", reporter.formatElapseTime(result));

        // 进位测试
        result = reporter.calcElapsedTime(0L, ((((23L * 60) + 59) * 60 + 59) * 1000 + 1000) * 1000000);
        assertEquals("1 day", reporter.formatElapseTime(result));
    }
}