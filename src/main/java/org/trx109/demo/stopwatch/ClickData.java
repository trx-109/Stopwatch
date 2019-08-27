package org.trx109.demo.stopwatch;

/**
 * 秒表点击数据对象，因为 JAVA 不像 scala 有 tuple，自行封装一个
 *
 * @author TRX-109
 * @since 2019/8/27
 */
public final class ClickData
{
    private String log = "";
    private long ts = 0L;

    /**
     * 构造函数
     *
     * @param log 秒表点击时的日志
     * @param ts  秒表点击时的时间戳，单位ns
     */
    public ClickData(final String log, final long ts)
    {
        this.log = log;
        this.ts = ts;
    }

    public String getLog()
    {
        return log;
    }

    public long getTs()
    {
        return ts;
    }

    @Override
    public String toString()
    {
        return "log: " + log + ", ts: " + ts;
    }
}
