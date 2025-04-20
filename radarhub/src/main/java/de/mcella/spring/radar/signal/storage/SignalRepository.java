package de.mcella.spring.radar.signal.storage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SignalRepository extends JpaRepository<SignalEntity, Long> {}
