package com.contagion.control;

import java.util.concurrent.Phaser;

public class PhaserExecution {
    private static Phaser INSTANCE;

    public static Phaser getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Phaser(1);
        }
        return INSTANCE;
    }
}
