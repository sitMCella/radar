package de.mcella.spring.radarreactive.signal;

import de.mcella.spring.radarreactive.signal.dto.Signal;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/signals")
public class SignalController {

  private final SignalService signalService;

  SignalController(SignalService signalService) {
    this.signalService = signalService;
  }

  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @CrossOrigin(origins = {"http://localhost", "http://localhost:3000"})
  public Flux<Signal> streamSignals() {
    return this.signalService.streamSignals();
  }
}
