package de.mcella.spring.radar.device;

import de.mcella.spring.radar.device.dto.Device;
import de.mcella.spring.radar.device.dto.DeviceModelMapper;
import de.mcella.spring.radar.device.exceptions.DeviceAlreadyExistsException;
import de.mcella.spring.radar.device.storage.DeviceEntity;
import de.mcella.spring.radar.device.storage.DeviceRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

  private final DeviceRepository deviceRepository;

  private final DeviceModelMapper deviceModelMapper;

  DeviceService(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
    this.deviceModelMapper = new DeviceModelMapper();
  }

  Device create(Device device) {
    if (deviceRepository.existsById(device.id())) {
      throw new DeviceAlreadyExistsException(device.id());
    }
    DeviceEntity deviceEntity = new DeviceEntity(device);
    this.deviceRepository.save(deviceEntity);
    return device;
  }

  List<Device> list() {
    return this.deviceRepository.findAll().stream()
        .map(deviceEntity -> deviceModelMapper.map(deviceEntity))
        .collect(Collectors.toList());
  }
}
