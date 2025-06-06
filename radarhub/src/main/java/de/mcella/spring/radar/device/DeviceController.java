package de.mcella.spring.radar.device;

import de.mcella.spring.radar.device.dto.Device;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

  private final DeviceService deviceService;

  DeviceController(DeviceService deviceService) {
    this.deviceService = deviceService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Device register(@RequestBody Device device) {
    return this.deviceService.create(device);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Device> list() {
    return this.deviceService.list();
  }
}
