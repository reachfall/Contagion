package com.contagion.control;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ScheduledExecution {
    private static ScheduledThreadPoolExecutor instance;

    public static ScheduledThreadPoolExecutor getInstance() {
        if (instance == null) {
            instance = new ScheduledThreadPoolExecutor(205);
            instance.setRemoveOnCancelPolicy(true);
        }

        return instance;
    }
}
