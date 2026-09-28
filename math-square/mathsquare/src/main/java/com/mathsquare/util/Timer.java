package com.mathsquare.util;

public class Timer {
    private long startTime;

    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public long stopTimer() {
        return System.currentTimeMillis() - startTime;
    }

    public static String printTime(long duration) {
        String commonTime = duration > 120000l ? String.format("%.2f min", (float)duration / 60000f) : String.format("%.2f sec", (float)duration / 1000f);
        return "TIME: " + duration + " ms / " + commonTime;
    }
}
