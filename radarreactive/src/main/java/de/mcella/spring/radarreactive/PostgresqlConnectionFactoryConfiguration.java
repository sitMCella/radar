package de.mcella.spring.radarreactive;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostgresqlConnectionFactoryConfiguration {

  @Bean
  public PostgresqlConnectionFactory postgresqlConnectionFactory() {
    return new PostgresqlConnectionFactory(
        PostgresqlConnectionConfiguration.builder()
            .host("db")
            .database("radarhub")
            .username("postgres")
            .password("mysecretpassword")
            .build());
  }
}
