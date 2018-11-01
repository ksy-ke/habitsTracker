package com.ksy.impl.persistant;

import com.ksy.impl.model.Act;
import com.ksy.impl.model.Habit;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class HabitsService {
    private static final String QUERY_GET_ALL = "SELECT * FROM habits";
    private static final String QUERY_GET_HABIT = "SELECT * FROM habits WHERE id_habit = (?)";
    private static final String QUERY_GET_ACTS = "SELECT * FROM acts WHERE fk_habit = (?)";
    private static final String QUERY_CHECK_HABIT = "SELECT * FROM habits WHERE name_habit = (?) AND first_day = (?)";
    private static final String QUERY_ADD_HABIT = "INSERT INTO habits (name_habit, first_day) VALUES (?, ?)";
    private static final String QUERY_ADD_ACT = "INSERT INTO acts (fk_habit, num) VALUES (?, ?)";
    private static final String QUERY_ADD_EXECUTED = "UPDATE acts SET executed = (?) WHERE fk_habit = (?) AND num = (?)";

    private static final Logger LOG = Logger.getLogger(HabitsService.class);

    private final ConnectionProvider provider;

    public HabitsService(ConnectionProvider provider) {
        this.provider = provider;
    }

    public List<Habit> retrieveAll() {
        List<Habit> habits = new ArrayList<>();
        Connection connection = provider.getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet habitsResultSet = statement.executeQuery(QUERY_GET_ALL);
            while (habitsResultSet.next()) {
                int habitId = habitsResultSet.getInt("id_habit");

                habits.add(new Habit(habitId,
                        habitsResultSet.getString("name_habit"),
                        habitsResultSet.getDate("first_day").toLocalDate(),
                        retrieveActs(connection, habitId)));
            }
        } catch (SQLException e) {
            LOG.error("Extraction habits list error", e);
            return Collections.emptyList();
        }
        if (LOG.isDebugEnabled()) { LOG.debug("Retrieved " + habits); }
        return habits;
    }

    public Optional<Habit> retrieve(int habitId) {
        Connection connection = provider.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_HABIT)) {
            statement.setInt(1, habitId);
            ResultSet habitResult = statement.executeQuery();
            habitResult.next();
            return Optional.of(new Habit(habitId,
                    habitResult.getString("name_habit"),
                    habitResult.getDate("first_day").toLocalDate(),
                    retrieveActs(connection, habitId)));
        } catch (SQLException e) {
            LOG.error("Extraction habit error", e);
            return Optional.empty();
        }
    }

    public void add(String name, LocalDate firstDay, int duration) {
        Connection connection = provider.getConnection();
        try (PreparedStatement checkStatement = connection.prepareStatement(QUERY_CHECK_HABIT);
             PreparedStatement addHabitStatement = connection.prepareStatement(QUERY_ADD_HABIT, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement addActStatement = connection.prepareStatement(QUERY_ADD_ACT)) {
            checkStatement.setString(1, name);
            checkStatement.setDate(2, Date.valueOf(firstDay.plusDays(1))); //TODO: Fix timeZone and use setDate without plusDays
            ResultSet checkResult = checkStatement.executeQuery();
            if (checkResult.next()) { return; }

            connection.setAutoCommit(false);
            addHabitStatement.setString(1, name);
            addHabitStatement.setDate(2, Date.valueOf(firstDay.plusDays(1)));//TODO: Fix timeZone and use setDate without plusDays
            addHabitStatement.executeUpdate();
            ResultSet keys = addHabitStatement.getGeneratedKeys();
            keys.next();
            addActStatement.setInt(1, keys.getInt(1));
            for (int i = 1; i <= duration; i++) {
                addActStatement.setInt(2, i);
                addActStatement.executeUpdate();
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            LOG.error("Add error", e);
            rollBackConnection(connection);
        }
    }

    public void performanceCheck(int habitId, int dayNumber, String executed) {
        Connection connection = provider.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(QUERY_ADD_EXECUTED)) {
            statement.setString(1, executed);
            statement.setInt(2, habitId);
            statement.setInt(3, dayNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("PerformanceCheck error", e);
        }
    }

    private List<Act> retrieveActs(Connection connection, int habitId) {
        try (PreparedStatement actsStatement = connection.prepareStatement(QUERY_GET_ACTS)) {
            actsStatement.setInt(1, habitId);
            ResultSet actsResultSet = actsStatement.executeQuery();

            List<Act> acts = new ArrayList<>();
            while (actsResultSet.next()) {
                acts.add(new Act(actsResultSet.getInt("num"),
                        actsResultSet.getString("executed")));
            }
            return acts;
        } catch (SQLException e) {
            LOG.error("Extraction acts list error", e);
            return Collections.emptyList();
        }
    }

    private void rollBackConnection(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            LOG.error("fail on rollback ", e);
        }
    }
}