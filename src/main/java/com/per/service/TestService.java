package com.per.service;

import com.per.prometheus.PrometheusCounter;
import io.prometheus.client.Counter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestService {
  static final Counter requests = Counter.build()
      .name("zhangfeng").help("Total requests.").register();
  @PrometheusCounter(name = "name", help = "help")
  public void testAdd() {
    log.info("123={}", 123);
    requests.inc();
  }

}
