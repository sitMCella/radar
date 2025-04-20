package de.mcella.spring.radar.device.storage;

import de.mcella.spring.radar.device.dto.Device;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "devices")
public class DeviceEntity {

  @Id private String id;
  private Double latitude;
  private Double longitude;

  DeviceEntity() {}

  public DeviceEntity(Device device) {
    this.id = device.id();
    this.latitude = device.latitude();
    this.longitude = device.longitude();
  }

  public String getId() {
    return this.id;
  }

  public Double getLatitude() {
    return this.latitude;
  }

  public Double getLongitude() {
    return this.longitude;
  }
}
