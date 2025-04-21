package de.mcella.spring.radarreactive.signal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Signal(
    Long id,
    Instant creationtime,
    String deviceId,
    Long objId,
    Double latitude,
    Double longitude) {}
