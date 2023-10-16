package com.example.scheduler.info;

public class TimerInfo {
    private int totalFireCount;
    private boolean runForever;
    private int repeatIntervalS;
    private long initialOffsetS;
    private String callbackData;



    public int getTotalFireCount() {
        return this.totalFireCount;
    }

    public void setTotalFireCount(int totalFireCount) {
        this.totalFireCount = totalFireCount;
    }

    public boolean isRunForever() {
        return this.runForever;
    }

    public boolean getRunForever() {
        return this.runForever;
    }

    public void setRunForever(boolean runForever) {
        this.runForever = runForever;
    }

    public int getRepeatIntervalS() {
        return this.repeatIntervalS;
    }

    public void setRepeatIntervalS(int repeatIntervalS) {
        this.repeatIntervalS = repeatIntervalS;
    }

    public long getInitialOffsetS() {
        return this.initialOffsetS;
    }

    public void setInitialOffsetS(long initialOffsetS) {
        this.initialOffsetS = initialOffsetS;
    }

    public String getCallbackData() {
        return this.callbackData;
    }

    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }
    

}
