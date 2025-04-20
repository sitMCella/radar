package de.mcella.spring.radarreactive.signal;

import de.mcella.spring.radarreactive.signal.dto.Signal;
import de.mcella.spring.radarreactive.signal.storage.SignalRepository;
import java.time.Duration;
import java.time.Instant;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class SignalService {

  private final SignalRepository signalRepository;

  SignalService(SignalRepository signalRepository) {
    this.signalRepository = signalRepository;
  }

  public Flux<Signal> pollLatestEntries() {
    return Flux.defer(
            () -> {
              Instant lastChecked = Instant.now().minusSeconds(2);
              return signalRepository.findNewerThan(lastChecked);
            })
        .repeatWhen(longFlux -> longFlux.delayElements(Duration.ofSeconds(2)));
  }
}
