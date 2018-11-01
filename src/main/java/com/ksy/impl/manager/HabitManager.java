package com.ksy.impl.manager;

import com.ksy.impl.model.Habit;
import com.ksy.impl.persistant.HabitsService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class HabitManager {
    private final HabitsService service;
    private final int habitDuration;

    public HabitManager(HabitsService service, int habitDuration) {
        this.service = service;
        this.habitDuration = habitDuration;
    }

    public List<Habit> getAll() { return service.retrieveAll(); }

    public Optional<Habit> getHabit(int habitId) { return service.retrieve(habitId); }

    public void add(String name, LocalDate firstDay) { service.add(name, firstDay, habitDuration); }

    public void setCheck(int habitId, int dayNumber, String executed) { service.performanceCheck(habitId, dayNumber, executed); }
}
