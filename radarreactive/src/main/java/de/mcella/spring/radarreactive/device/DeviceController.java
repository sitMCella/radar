package de.mcella.spring.radarreactive.device;

import de.mcella.spring.radarreactive.device.dto.Device;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

  private final DeviceService deviceService;

  DeviceController(DeviceService deviceService) {
    this.deviceService = deviceService;
  }

  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @CrossOrigin(origins = {"http://localhost", "http://localhost:3000"})
  public Flux<Device> streamDevices() {
    return this.deviceService.streamDevices();
  }
}
