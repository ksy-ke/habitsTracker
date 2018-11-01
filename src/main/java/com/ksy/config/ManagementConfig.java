package com.ksy.config;

import com.ksy.impl.manager.HabitManager;
import com.ksy.impl.persistant.HabitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("config.properties")
public class ManagementConfig {
    @Bean
    @Autowired
    public HabitManager habitManager(HabitsService habitsService,
                                     @Value("${habit.duration}") int habitDuration) {
        return new HabitManager(habitsService, habitDuration);
    }
}
