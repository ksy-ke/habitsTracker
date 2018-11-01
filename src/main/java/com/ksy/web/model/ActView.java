package com.ksy.web.model;

import com.ksy.impl.model.Act;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ActView {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.YY");
    private final int dayNumber;
    private final String executed;
    private final LocalDate firstDay;
    private String completionDate;

    public ActView(Act act, LocalDate firstDay) {
        this.dayNumber = act.getDayNumber();
        this.executed = act.getExecuted();
        this.firstDay = firstDay;
        calculatedCompletionDate();
    }

    private void calculatedCompletionDate() { completionDate = FORMATTER.format(firstDay.plusDays(dayNumber - 1)); }

    public int getDayNumber() { return dayNumber; }

    public String getExecuted() { return executed; }

    public String getCompletionDate() { return completionDate; }
}
