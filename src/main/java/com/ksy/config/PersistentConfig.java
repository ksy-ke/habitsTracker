package com.ksy.config;

import com.ksy.impl.persistant.ConnectionProvider;
import com.ksy.impl.persistant.HabitsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.sql.SQLException;


@Configuration
@PropertySource("database.properties")
public class PersistentConfig {
    @Value("${db.url}")
    String url;
    @Value("${db.user}")
    String user;
    @Value("${db.password}")
    String password;

    @Bean
    public ConnectionProvider connectionProvider() throws SQLException {
       return new ConnectionProvider(url, user, password);
    }

    @Bean
    public HabitsService habitsService() throws SQLException {
        return new HabitsService(connectionProvider());
    }
}