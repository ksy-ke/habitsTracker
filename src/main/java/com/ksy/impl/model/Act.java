package com.ksy.impl.model;

public class Act {
    private final int dayNumber;
    private final String executed;

    public Act(int dayNumber, String executed) {
        this.dayNumber = dayNumber;
        this.executed = executed;
    }

    public int getDayNumber() { return dayNumber; }

    public String getExecuted() { return executed; }
}