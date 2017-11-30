package com.rokid.reporter;

import com.rokid.logger.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by fanfeng on 2017/5/9.
 */

public class ReporterManager {

    private static final int POOL_CORE_SIZE = 1;
    private static final int POOL_MAX_SIZE = 10;
    private static final long POOL_KEEP_TIME = 30L;
    private static final int BLOCKING_QUEUE_CAPACITY = 30;

    ExecutorService threadPoolExecutor = new ThreadPoolExecutor(POOL_CORE_SIZE, POOL_MAX_SIZE, POOL_KEEP_TIME,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(BLOCKING_QUEUE_CAPACITY));

    private ReporterManager() {

    }

    public void executeReporter(BaseReporter baseReporter) {
        if (baseReporter != null) {
            Logger.d("executeReporter ");
            threadPoolExecutor.execute(baseReporter);
        }
    }

    public static ReporterManager getInstance() {
        return SingleHolder.instance;
    }

    private static class SingleHolder {
        private static final ReporterManager instance = new ReporterManager();
    }

}

