package de.mcella.spring.radar.signal;

import de.mcella.spring.radar.signal.dto.Signal;
import de.mcella.spring.radar.signal.dto.SignalModelMapper;
import de.mcella.spring.radar.signal.storage.SignalEntity;
import de.mcella.spring.radar.signal.storage.SignalRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SignalService {

  private final SignalRepository signalRepository;

  private final SignalModelMapper signalModelMapper;

  SignalService(SignalRepository signalRepository) {
    this.signalRepository = signalRepository;
    this.signalModelMapper = new SignalModelMapper();
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
