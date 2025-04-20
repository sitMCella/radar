package de.mcella.spring.radarreactive.device;

import de.mcella.spring.radarreactive.device.dto.Device;
import de.mcella.spring.radarreactive.device.storage.DeviceRepository;
import java.time.Duration;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class DeviceService {

  private final DeviceRepository deviceRepository;

  DeviceService(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
  }

  public Flux<Device> getDevices() {
    return Flux.defer(
            () -> {
              return deviceRepository.getAll();
            })
        .repeatWhen(longFlux -> longFlux.delayElements(Duration.ofSeconds(10)));
  }
}
