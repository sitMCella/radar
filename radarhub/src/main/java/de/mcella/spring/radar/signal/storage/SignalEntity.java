package de.mcella.spring.radar.signal.storage;

import de.mcella.spring.radar.signal.dto.Signal;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "signals")
public class SignalEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreationTimestamp private Instant creationtime;
  private String deviceId;
  private Long objId;
  private Double latitude;
  private Double longitude;

  SignalEntity() {}

  public SignalEntity(Signal signal) {
    this.deviceId = signal.deviceId();
    this.objId = signal.objId();
    this.latitude = signal.latitude();
    this.longitude = signal.longitude();
  }

  public Long getId() {
    return this.id;
  }

  public Instant getCreationtime() {
    return this.creationtime;
  }

  public String getDeviceId() {
    return this.deviceId;
  }

  public Long getObjId() {
    return this.objId;
  }

  public Double getLatitude() {
    return this.latitude;
  }

  public Double getLongitude() {
    return this.longitude;
  }
}
