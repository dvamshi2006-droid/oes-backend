package com.example.examination.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DataSourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.isBlank()) {
            databaseUrl = System.getenv("INTERNAL_DATABASE_URL");
        }

        // If Render DATABASE_URL is present and starts with postgres:// or postgresql://
        if (databaseUrl != null && !databaseUrl.isBlank() && 
            (databaseUrl.startsWith("postgres://") || databaseUrl.startsWith("postgresql://"))) {
            try {
                logger.info("Detected Render PostgreSQL DATABASE_URL. Parsing connection details...");
                URI dbUri = new URI(databaseUrl);
                
                String username = "";
                String password = "";
                if (dbUri.getUserInfo() != null) {
                    String[] userInfo = dbUri.getUserInfo().split(":", 2);
                    username = userInfo[0];
                    if (userInfo.length > 1) {
                        password = userInfo[1];
                    }
                }

                String host = dbUri.getHost();
                int port = dbUri.getPort() == -1 ? 5432 : dbUri.getPort();
                String path = dbUri.getPath();
                if (path != null && path.startsWith("/")) {
                    path = path.substring(1);
                }

                String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + path;
                if (dbUri.getQuery() != null && !dbUri.getQuery().isBlank()) {
                    jdbcUrl += "?" + dbUri.getQuery();
                }

                HikariConfig hikariConfig = new HikariConfig();
                hikariConfig.setJdbcUrl(jdbcUrl);
                hikariConfig.setUsername(username);
                hikariConfig.setPassword(password);
                hikariConfig.setDriverClassName("org.postgresql.Driver");

                logger.info("Successfully configured PostgreSQL DataSource for {}:{}/{}", host, port, path);
                return new HikariDataSource(hikariConfig);
            } catch (Exception e) {
                logger.error("Failed to parse DATABASE_URL, falling back to default properties: {}", e.getMessage());
            }
        }

        // Default fallback to application.properties / Spring Boot auto-configuration
        return properties.initializeDataSourceBuilder().build();
    }
}
