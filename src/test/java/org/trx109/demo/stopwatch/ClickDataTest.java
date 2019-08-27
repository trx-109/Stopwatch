package org.trx109.demo.stopwatch;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClickDataTest
{

    @Test
    public void getLog()
    {
        final ClickData data = new ClickData("abc", 123L);
        assertEquals("abc", data.getLog());
    }

    @Test
    public void getTs()
    {
        final ClickData data = new ClickData("abc", 123L);
        assertEquals(123L, data.getTs());
    }
}