package de.mcella.spring.radar.signal.dto;

import de.mcella.spring.radar.signal.storage.SignalEntity;

public class SignalModelMapper {

  public Signal map(SignalEntity signalEntity) {
    return new Signal(
        signalEntity.getId(),
        signalEntity.getCreationtime(),
        signalEntity.getDeviceId(),
        signalEntity.getObjId(),
        signalEntity.getLatitude(),
        signalEntity.getLongitude());
  }
}
