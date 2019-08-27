# Stopwatch
秒表工具，可用于程序中的计时

## 使用方法
```java
    final StopWatch stopWatch = new StopWatch("MyStopWatch");

    // your code

    stopWatch.start();

    // run your code

    // 多次调用 click 方法打点时间戳，启动后可以在多个线程中调用 click，参数用于跟踪 click 信息
    stopWatch.click("some progress finished.");

    // your clean code

    // 全部运行完成后可以停止秒表并打印汇总信息，也可以运行过程中打印汇总
    stopWatch.stop();
    stopWatch.summary();
```

* start、stop 方法支持传入一个日志字符串
* shortSummary 默认只打印到调用时的耗时和 click 次数
* 可以自行扩展 ```IStopWatchReporter``` 类自定义输出的报告内容
