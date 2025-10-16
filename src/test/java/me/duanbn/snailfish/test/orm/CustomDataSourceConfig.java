package me.duanbn.snailfish.test.orm;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class CustomDataSourceConfig {

    public static final String DS_TWO = "dsTwo";
    public static final String DS_THREE = "dsThree";
    public static final String DS_FOUR = "dsFour";

    @ConfigurationProperties(prefix = "spring.datasource.two")
    @ConditionalOnProperty(prefix = "spring.datasource.two", name = { "driver-class-name", "url", "username",
            "password" })
    @Bean(name = "dsTwo")
    public DataSource dsTwo() {
        return new HikariDataSource();
    }

    @ConfigurationProperties(prefix = "spring.datasource.three")
    @ConditionalOnProperty(prefix = "spring.datasource.three", name = { "driver-class-name", "url", "username",
            "password" })
    @Bean(name = "dsThree")
    public DataSource dsThree() {
        return new HikariDataSource();
    }

    @ConfigurationProperties(prefix = "spring.datasource.four")
    @ConditionalOnProperty(prefix = "spring.datasource.four", name = { "driver-class-name", "url", "username",
            "password" })
    @Bean(name = "dsFour")
    public DataSource dsFour() {
        return new HikariDataSource();
    }

}
