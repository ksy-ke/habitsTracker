package com.ksy.web.model;

import com.ksy.impl.model.Act;
import com.ksy.impl.model.Habit;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HabitView {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.YY");
    private final int id;
    private final String name;
    private final String firstDay;
    private final List<ActView> acts = new ArrayList<>();

    public HabitView(Habit habit) {
        LocalDate date = habit.getDate();

        this.id = habit.getId();
        this.name = habit.getName();
        this.firstDay = FORMATTER.format(habit.getDate());

        for (Act act : habit.getActs()) { acts.add(new ActView(act, date)); }
    }

    public String getName() { return name; }

    public String getFirstDay() { return firstDay; }

    public int getId() { return id; }

    public List<ActView> getActs() { return acts; }
}