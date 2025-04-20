package de.mcella.spring.radarreactive.device.storage;

import de.mcella.spring.radarreactive.device.dto.Device;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class DeviceRepository {

  private final DatabaseClient databaseClient;

  public DeviceRepository(DatabaseClient databaseClient) {
    this.databaseClient = databaseClient;
  }

  public Flux<Device> getAll() {
    return databaseClient
        .sql("SELECT * FROM devices")
        .map(
            (row, metadata) ->
                new Device(
                    row.get("id", String.class),
                    row.get("latitude", Double.class),
                    row.get("longitude", Double.class),
                    row.get("radius", Double.class)))
        .all();
  }
}
