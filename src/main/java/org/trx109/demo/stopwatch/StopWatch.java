package org.trx109.demo.stopwatch;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 秒表类
 *
 * @author TRX-109
 * @since 2019/08/27
 */
public class StopWatch
{
    private static final String LOG_START = "Start";
    private static final String LOG_STOP = "Stop";

    private volatile boolean running = false;

    // 表秒的实例名称
    private String name = "";

    private final Object lock = new Object();

    // 存储 <日志, 时刻> 的队列，其中时刻保存的是纳秒
    private final ConcurrentLinkedQueue<ClickData> clickData = new ConcurrentLinkedQueue<ClickData>();

    // 秒表启动的时刻，单位毫秒
    private final AtomicLong startTime = new AtomicLong(0L);

    private IStopWatchReporter reporter;

    /**
     * 构造函数，创建秒表实例，使用默认的统计结果汇报器
     *
     * @param watchName 表秒实例名，不能为空
     * @throws IllegalArgumentException 参数非法时抛出异常
     */
    public StopWatch(final String watchName)
    {
        this(watchName, new DefaultStopWatchReporter());
    }

    /**
     * 构造函数，创建秒表实例
     *
     * @param watchName 表秒实例名，不能为空
     * @param reporter  表秒统计结果汇报器实例
     */
    public StopWatch(final String watchName, final IStopWatchReporter reporter)
    {
        if (StringUtils.isEmpty(watchName))
            throw new IllegalArgumentException("A stopwatch must have a non-null name.");

        this.name = watchName;

        if (reporter == null) this.reporter = new DefaultStopWatchReporter();
        else this.reporter = reporter;
    }

    /**
     * 检测秒表是否处于运行状态
     *
     * @return true 运行中，false 已停止
     */
    public boolean isRunning()
    {
        return running;
    }

    /**
     * 获取表秒的名称
     *
     * @return 返回秒表名称
     */
    public String getName()
    {
        return name;
    }

    /**
     * 返货秒表启动的时刻
     *
     * @return 启动时刻，单位 ms
     */
    public long getStartTime()
    {
        return startTime.get();
    }

    /**
     * 获取秒表记录的时间点
     *
     * @return 秒表记录的数据点，从开始到结束，每个 pair 由 <描述, 时间戳> 组成，时间戳单位 ns
     */
    public LinkedList<ClickData> getClickData()
    {
        // 复制一份数据返回，防止暴露内部对象
        return new LinkedList<ClickData>(clickData);
    }

    /**
     * 秒表进行一次计时打点，打点后秒表继续运行
     *
     * @param log 打点时的日志，可以为空
     * @throws IllegalStateException 秒表未启动时调用抛出该异常
     */
    public void click(final String log)
    {
        if (!running) throw new IllegalStateException("StopWatch [" + name + "] is not running.");

        if (StringUtils.isEmpty(log)) {
            clickData.add(new ClickData("", System.nanoTime()));
        } else {
            clickData.add(new ClickData(log, System.nanoTime()));
        }
    }

    /**
     * 重置秒表
     *
     * @throws IllegalStateException 未停止时调用 reset 抛出异常
     */
    public void reset()
    {
        if (running) throw new IllegalStateException("StopWatch " + name + " is still running, stop it before reset.");

        clickData.clear();
        startTime.set(0L);
    }

    /**
     * 启动秒表，处于运行中的秒表调用启动方法无效，不产生影响。启动的日志为 Start
     */
    public void start()
    {
        start(LOG_START);
    }

    /**
     * 启动表秒，处于运行中的秒表调用启动方法无效，不产生影响
     *
     * @param log 启动时希望记录的说明，为空则使用默认值 Start
     */
    public void start(final String log)
    {
        if (!running) {
            synchronized (lock) {
                if (!running) {
                    running = true;
                    startTime.set(System.currentTimeMillis());

                    String startLog = log;
                    if (StringUtils.isEmpty(startLog)) startLog = LOG_STOP;

                    click(startLog);
                }
            }
        }
    }

    /**
     * 停止表秒，以第一次调用为准
     */
    public void stop()
    {
        stop(LOG_STOP);
    }

    /**
     * 停止表秒，以第一次调用为准
     *
     * @param log 停止时的日志，为空则使用默认值
     */
    public void stop(final String log)
    {
        if (running) {
            synchronized (lock) {
                if (running) {
                    running = false;

                    String stopLog = log;
                    if (StringUtils.isEmpty(stopLog)) stopLog = LOG_STOP;

                    clickData.add(new ClickData(stopLog, System.nanoTime()));
                }
            }
        }
    }

    /**
     * 打印短报告，打印内容由报告器实现决定
     *
     * @return 返回简短报告
     */
    public String shortSummary()
    {
        return reporter.shortSummary(this);
    }

    /**
     * 打印完整报告，打印内容由报告器实现决定
     *
     * @return 返回完整报告
     */
    public String summary()
    {
        return reporter.summary(this);
    }
}
