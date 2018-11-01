package com.ksy.impl.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Habit {
    private final int id;
    private final String name;
    private final LocalDate firstDay;
    private final List<Act> acts;

    public Habit(int id, String name, LocalDate firstDay, List<Act> acts) {
        this.id = id;
        this.name = name;
        this.firstDay = firstDay;
        this.acts = acts;
    }

    public String getName() { return name; }

    public LocalDate getDate() { return firstDay; }

    public int getId() { return id; }

    public List<Act> getActs() { return acts; }
}