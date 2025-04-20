package de.mcella.spring.radar.device.dto;

import de.mcella.spring.radar.device.storage.DeviceEntity;

public class DeviceModelMapper {

  public Device map(DeviceEntity deviceEntity) {
    return new Device(
        deviceEntity.getId(),
        deviceEntity.getLatitude(),
        deviceEntity.getLongitude(),
        deviceEntity.getRadius());
  }
}
