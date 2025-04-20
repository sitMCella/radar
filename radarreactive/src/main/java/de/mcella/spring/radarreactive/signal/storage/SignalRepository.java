package de.mcella.spring.radarreactive.signal.storage;

import de.mcella.spring.radarreactive.signal.dto.Signal;
import java.time.Instant;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class SignalRepository {

  private final DatabaseClient databaseClient;

  public SignalRepository(DatabaseClient databaseClient) {
    this.databaseClient = databaseClient;
  }

  public Flux<Signal> findNewerThan(Instant lastKnown) {
    return databaseClient
        .sql("SELECT * FROM signals WHERE creationtime > :lastKnown ORDER BY creationtime ASC")
        .bind("lastKnown", lastKnown)
        .map(
            (row, metadata) ->
                new Signal(
                    row.get("id", Long.class),
                    row.get("creationtime", Instant.class),
                    row.get("device_id", String.class),
                    row.get("obj_id", Long.class),
                    row.get("latitude", Double.class),
                    row.get("longitude", Double.class)))
        .all();
  }
}
