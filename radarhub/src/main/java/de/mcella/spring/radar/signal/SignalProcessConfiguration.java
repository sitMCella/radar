package de.mcella.spring.radar.signal;

import de.mcella.spring.radar.signal.dto.Signal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SignalProcessConfiguration {

  @Bean
  public BlockingQueue<Signal> requestQueue() {
    return new LinkedBlockingQueue<>(100);
  }

  @Bean
  public ExecutorService requestProcessorExecutor() {
    return Executors.newFixedThreadPool(10);
  }
}
