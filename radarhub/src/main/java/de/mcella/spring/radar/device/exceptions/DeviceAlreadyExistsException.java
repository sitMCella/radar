package de.mcella.spring.radar.device.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DeviceAlreadyExistsException extends ResponseStatusException {

  public DeviceAlreadyExistsException(String deviceId) {
    super(HttpStatus.CONFLICT, String.format("A device with id %s already exists.", deviceId));
  }
}
