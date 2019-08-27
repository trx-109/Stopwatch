package org.trx109.demo.stopwatch;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StopWatchTest
{

    @Test
    public void summary() throws InterruptedException
    {
        final StopWatch stopWatch = new StopWatch("StopWatch for test");

        assertEquals("StopWatch for test", stopWatch.getName());
        stopWatch.start();

        assertTrue(stopWatch.isRunning());
        assertEquals(1, stopWatch.getClickData().size());

        // 无效的调用，验证对秒表无影响
        stopWatch.start("testStart");
        assertTrue(stopWatch.isRunning());
        assertEquals(1, stopWatch.getClickData().size());

        Thread.sleep(500L);
        stopWatch.click("First Finished");

        assertTrue(stopWatch.isRunning());
        assertEquals(2, stopWatch.getClickData().size());

        Thread.sleep(500L);
        stopWatch.click("Second Finished");

        assertTrue(stopWatch.isRunning());
        assertEquals(3, stopWatch.getClickData().size());

        Thread.sleep(600L);
        stopWatch.click("Third Finished");

        assertTrue(stopWatch.isRunning());
        assertEquals(4, stopWatch.getClickData().size());

        Thread.sleep(300L);
        stopWatch.stop();

        assertFalse(stopWatch.isRunning());
        assertEquals(5, stopWatch.getClickData().size());

        System.out.println(stopWatch.summary());

        assertFalse(stopWatch.isRunning());
        assertEquals(5, stopWatch.getClickData().size());

        stopWatch.reset();
        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.getClickData().isEmpty());
    }
}