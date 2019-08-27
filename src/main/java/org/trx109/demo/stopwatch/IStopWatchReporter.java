package org.trx109.demo.stopwatch;

/**
 * 秒表的统计结果报表接口
 *
 * @author TRX-109
 * @since 2019/08/27
 */
public interface IStopWatchReporter
{
    /**
     * 生成简短报告
     *
     * @param stopWatch 表秒实例对象
     * @return 返回报告字符串，包括换行符等，没有结果应当返回空字符串而不是 null
     */
    String shortSummary(final StopWatch stopWatch);

    /**
     * 生成完整报告
     *
     * @param stopWatch 表秒实例对象
     * @return 返回完整报告，包括换行符等，没有结果应当返回空字符串而不是 null
     */
    String summary(final StopWatch stopWatch);
}
