package com.contagion.control;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ScheduledExecution {
    private static ScheduledExecutorService instance;

    public static ScheduledExecutorService getInstance() {
        if(instance == null) {
            instance = Executors.newScheduledThreadPool(50);
        }

        return instance;
    }
}
