package org.trx109.demo.stopwatch;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * 默认秒表结果报告，可以扩展
 *
 * @author TRX-109
 * @since 2019/8/27
 */
public class DefaultStopWatchReporter implements IStopWatchReporter
{
    public static final TimeUnit[] TIME_UNITS = new TimeUnit[]{TimeUnit.DAYS, TimeUnit.HOURS, TimeUnit.MINUTES, TimeUnit.SECONDS, TimeUnit.MILLISECONDS};

    /**
     * 计算耗时
     *
     * @param startTime 起始时刻，单位 ns
     * @param stopTime  结束时刻，单位 ns
     * @return 返回的数组，根据下标表示对应单位经过的时长
     * @throws IllegalArgumentException 结束时间比开始时间早抛出异常
     */
    public int[] calcElapsedTime(final long startTime, final long stopTime)
    {
        if (stopTime < startTime) throw new IllegalArgumentException("StopTime must be larger or equal to startTime");

        long elapsedTime = TimeUnit.NANOSECONDS.toMillis(stopTime - startTime);
        final int[] result = new int[TIME_UNITS.length];
        for (int i = 0; i < TIME_UNITS.length; i++) {
            result[i] = (int) (elapsedTime / TIME_UNITS[i].toMillis(1));
            elapsedTime %= TIME_UNITS[i].toMillis(1);
        }

        return result;
    }

    /**
     * 为单个单位的经过时间进行字符串描述
     *
     * @param duration 经过时间
     * @param unit     单位
     * @return 返回描述后的字符串，尾部不带空格
     */
    public String describeTime(final int duration, final String unit)
    {
        final StringBuilder sb = new StringBuilder();
        if (duration > 1) {
            sb.append(duration).append(" ").append(unit).append("s");
        } else if (duration == 1) {
            sb.append(duration).append(" ").append(unit);
        }
        // else do nothing

        return sb.toString();
    }

    /**
     * 格式化经过时间
     *
     * @param elapsedTime 按单位划分后的经过时间数组
     * @return 返回经过时间字符串，形如 xx day(s) yy hour(s) zz minute(s) ss second(s) kk millisecond(s)
     */
    public String formatElapseTime(final int[] elapsedTime)
    {
        final StringBuilder sb = new StringBuilder();

        final String[] units = new String[]{"day", "hour", "minute", "second", "millisecond"};
        for (int i = 0; i < TIME_UNITS.length; i++) {
            final String timeStr = describeTime(elapsedTime[i], units[i]);
            if (!StringUtils.isEmpty(timeStr)) {
                sb.append(timeStr);

                if (i < TIME_UNITS.length - 1) sb.append(" ");
            }
        }

        final String result = sb.toString().trim();
        if (StringUtils.isEmpty(result)) return "0 millisecond";
        else return result;
    }

    /**
     * {@inheritDoc}
     */
    public String shortSummary(final StopWatch stopWatch)
    {
        if (null == stopWatch) return "stopWatch is null";

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        final StringBuilder sb = new StringBuilder();
        sb.append("StopWatch ").append(stopWatch.getName());
        sb.append(": start at: ").append(df.format(new Date(stopWatch.getStartTime())));

        final LinkedList<ClickData> data = stopWatch.getClickData();

        sb.append(", click times: ").append(data.size());

        // 取出第一个和最后一个元素，即开始和结束时刻
        final long startTime = data.getFirst().getTs();
        final long stopTime = data.getLast().getTs();
        final int[] elapsedTime = calcElapsedTime(startTime, stopTime);

        sb.append(", elapsed time: ").append(formatElapseTime(elapsedTime));

        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    public String summary(final StopWatch stopWatch)
    {
        final StringBuilder sb = new StringBuilder();
        sb.append(shortSummary(stopWatch)).append(System.lineSeparator());
        sb.append("Click details:").append(System.lineSeparator());

        final LinkedList<ClickData> data = stopWatch.getClickData();

        final long startTime = data.getFirst().getTs();
        long lastClickTime = startTime;
        int clickCnt = 1;
        for (final ClickData clickData : data) {
            final String log = clickData.getLog();
            final long currTime = clickData.getTs();

            final int[] elapsedTimeToLastClick = calcElapsedTime(lastClickTime, currTime);
            final int[] elapsedTimeSoFar = calcElapsedTime(startTime, currTime);

            sb.append("Click[").append(String.format("%-3d", clickCnt))
                    .append("]: ").append(String.format("%-32s", log))
                    .append("|+").append(String.format("%-64s", formatElapseTime(elapsedTimeToLastClick)))
                    .append("|Total: ").append(formatElapseTime(elapsedTimeSoFar))
                    .append(System.lineSeparator());

            lastClickTime = currTime;
            clickCnt++;
        }

        return sb.toString();
    }
}
