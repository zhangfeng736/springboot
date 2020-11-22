package com.per.prometheus;

import io.prometheus.client.CollectorRegistry;
import java.util.Random;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Job {
  @Autowired
  CollectorRegistry registry;
  static final Random random = new Random();
  io.prometheus.client.Counter requests=null;
  @PostConstruct
  private void init() {
//    requests=Counter.builder("zhangfeng").register(registry);


    requests = io.prometheus.client.Counter.build()
        .name("requests_total").help("Total requests.").register(registry);
    new Thread(new RunJob()).start();
  }


  public class RunJob implements Runnable {

    @Override
    public void run() {
      while (true) {
        try {
          requests.inc();
          Thread.sleep(random.nextInt(10000));
        } catch (Exception e) {
          log.error("error", e);
        }
      }
    }
  }
}
