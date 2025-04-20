package de.mcella.spring.radar.signal;

import de.mcella.spring.radar.signal.dto.Signal;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/signals")
public class SignalController {

  private final SignalService signalService;
  private final BlockingQueue<Signal> requestQueue;

  SignalController(SignalService signalService, BlockingQueue<Signal> requestQueue) {
    this.signalService = signalService;
    this.requestQueue = requestQueue;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<String> create(@RequestBody Signal signal) {
    boolean added = requestQueue.offer(signal);
    if (!added) {
      return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
    return ResponseEntity.accepted().build();
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Signal> list() {
    return this.signalService.list();
  }
}
