package de.mcella.spring.radar.signal;

import de.mcella.spring.radar.signal.dto.Signal;
import de.mcella.spring.radar.signal.dto.SignalModelMapper;
import de.mcella.spring.radar.signal.storage.SignalEntity;
import de.mcella.spring.radar.signal.storage.SignalRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SignalService {

  private final SignalRepository signalRepository;

  private final BlockingQueue<Signal> requestQueue;

  private final ExecutorService executorService;

  private final SignalModelMapper signalModelMapper;

  SignalService(
      SignalRepository signalRepository,
      BlockingQueue<Signal> requestQueue,
      ExecutorService executorService) {
    this.signalRepository = signalRepository;
    this.requestQueue = requestQueue;
    this.executorService = executorService;
    this.signalModelMapper = new SignalModelMapper();
  }

  @PostConstruct
  public void startProcessing() {
    Runnable worker =
        () -> {
          while (true) {
            try {
              Signal signal = requestQueue.take();
              create(signal);
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              break;
            }
          }
        };

    for (int i = 0; i < 10; i++) {
      executorService.submit(worker);
    }
  }

  Signal create(Signal signal) {
    SignalEntity signalEntity = new SignalEntity(signal);
    SignalEntity signalEntityGenerated = this.signalRepository.save(signalEntity);
    return signalModelMapper.map(signalEntityGenerated);
  }

  List<Signal> list() {
    return this.signalRepository.findAll().stream()
        .map(signalEntity -> signalModelMapper.map(signalEntity))
        .collect(Collectors.toList());
  }
}
