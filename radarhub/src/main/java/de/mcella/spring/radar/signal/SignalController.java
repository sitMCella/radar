package de.mcella.spring.radar.signal;

import de.mcella.spring.radar.signal.dto.Signal;
import java.util.List;
import org.springframework.http.HttpStatus;
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

  SignalController(SignalService signalService) {
    this.signalService = signalService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Signal create(@RequestBody Signal signal) {
    return this.signalService.create(signal);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Signal> list() {
    return this.signalService.list();
  }
}
