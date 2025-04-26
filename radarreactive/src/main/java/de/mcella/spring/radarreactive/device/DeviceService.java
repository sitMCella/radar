package de.mcella.spring.radarreactive.device;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mcella.spring.radarreactive.device.dto.Device;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.api.PostgresqlConnection;
import io.r2dbc.postgresql.api.PostgresqlResult;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@EnableR2dbcRepositories(considerNestedRepositories = true)
public class DeviceService {

  private final PostgresqlConnection connection;

  private final ObjectMapper objectMapper;

  private final Logger logger;

  DeviceService(PostgresqlConnectionFactory connectionFactory, ObjectMapper objectMapper) {
    this.connection =
        Mono.from(connectionFactory.create()).cast(PostgresqlConnection.class).block();
    this.objectMapper = objectMapper;
    this.logger = LoggerFactory.getLogger(DeviceService.class);
  }

  @PostConstruct
  private void postConstruct() {
    connection
        .createStatement("LISTEN devices_notification")
        .execute()
        .flatMap(PostgresqlResult::getRowsUpdated)
        .subscribe();
  }

  @PreDestroy
  private void preDestroy() {
    connection.close().subscribe();
  }

  public Flux<Device> streamDevices() {
    return connection
        .getNotifications()
        .onBackpressureBuffer(1000, dropped -> logger.warn("Dropped notification: " + dropped))
        .map(
            notification -> {
              try {
                return objectMapper.readValue(notification.getParameter(), Device.class);
              } catch (JsonProcessingException e) {
                logger.error("Cannot send device", e);
              }
              return null;
            });
  }
}
