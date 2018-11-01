package com.ksy.web.controller;

import com.ksy.impl.manager.HabitManager;
import com.ksy.impl.model.Habit;
import com.ksy.web.model.HabitView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.*;

import static java.util.Optional.empty;


@Controller
public class HabitsController {
    private static final String HABITS_PARAMETER = "habits";
    private final HabitManager manager;

    public HabitsController(HabitManager habitManager) {
        this.manager = habitManager;
    }

    @RequestMapping(value = "/habits/all", method = RequestMethod.GET)
    public String getAll(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Map<Integer, HabitView> idToHabitView = new HashMap<>();
        for (Habit habit : manager.getAll()) { idToHabitView.put(habit.getId(), new HabitView(habit)); }
        model.addAttribute(HABITS_PARAMETER, idToHabitView.values());
        session.setAttribute(HABITS_PARAMETER, idToHabitView);
        return "AllHabits";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addNew(HttpServletRequest request) {
        if (request.getParameter("habitName") != null && request.getParameter("firstDay") != null) {
            manager.add(request.getParameter("habitName"), LocalDate.parse(request.getParameter("firstDay"))); //TODO check is date or not
        }
        return "redirect:/habits/all";
    }

    @RequestMapping(value = "/habits/habit", method = RequestMethod.GET)
    public String getHabit(HttpServletRequest request, Model model) {
        Optional<Integer> habitId = toPositiveInt(request.getParameter("habitId"));
        if (!habitId.isPresent()) {
            model.addAttribute("message", "Неверный формат ID привычки");
            return "Exception";
        }

        HabitView habit;
        @SuppressWarnings("unchecked")
        Map<Integer, HabitView> habitsInSession = (Map<Integer, HabitView>) request.getSession().getAttribute(HABITS_PARAMETER);
        if (habitsInSession != null) {
            habit = habitsInSession.get(habitId.get());  // FIXME null check (?)
        } else {
            Optional<Habit> getHabit = manager.getHabit(habitId.get());
            if (getHabit.isPresent()) {
                habit = new HabitView(getHabit.get());
            } else {
                model.addAttribute("message", "Неверный ID привычки");
                return "Exception";
            }
        }

        model.addAttribute("habit", habit);
        return "Habit";
    }

    @RequestMapping(value = "/habits/habit", method = RequestMethod.POST)
    public String updateCheck(HttpServletRequest request, Model model) {
        Optional<Integer> habitId = toPositiveInt(request.getParameter("habitId"));
        Optional<Integer> dayNumber = toPositiveInt(request.getParameter("dayNumber"));
        if (!habitId.isPresent() || !dayNumber.isPresent()) {
            model.addAttribute("message", "Невозможно найти указанный день выполнения или привычку");
            return "Exception";
        }

        manager.setCheck(habitId.get(),
                dayNumber.get(),
                request.getParameter("done"));

        request.getSession().removeAttribute(HABITS_PARAMETER);
        return "redirect:/habits/habit?habitId=" + habitId.get();
    }

    private Optional<Integer> toPositiveInt(String value) {
        Integer habitId;
        try {
            habitId = Integer.parseInt(value);
            if (habitId < 0) { return empty(); }
        } catch (NumberFormatException e) {
            return empty();
        }
        return Optional.of(habitId);
    }


}