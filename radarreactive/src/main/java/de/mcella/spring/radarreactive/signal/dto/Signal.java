package de.mcella.spring.radarreactive.signal.dto;

import java.time.Instant;

public record Signal(
    Long id,
    Instant creationtime,
    String deviceId,
    Long objId,
    Double latitude,
    Double longitude) {}
