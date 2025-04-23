package com.scheduler.scheduler.config;

import jakarta.annotation.PostConstruct;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.sql.*;

@Configuration
@Order(1)
public class DatabaseConfig {

    @Value("${spring.datasource.name}")
    private String dbName;

    @Value("${spring.datasource.admin-url}")
    private String adminUrl;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @PostConstruct
    public void createDbIfNotExists() {
        try (Connection conn = DriverManager.getConnection(adminUrl, username, password);
            Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + dbName + "'");

            if (!rs.next()) {
                stmt.executeUpdate("CREATE DATABASE " + dbName);
                System.out.println("Database '" + dbName + "' created.");
            } else {
                System.out.println("Database '" + dbName + "' already exists.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create or check DB", e);
        }
    }

    @Bean
    public Flyway flywayMigration() {
        Flyway flyway = Flyway.configure()
                .dataSource(url, username, password)
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
        return flyway;
    }
}