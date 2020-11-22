package com.per;

import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Slf4j
public class AnalysisApplication {

  public static void main(String[] args) throws UnknownHostException {
    SpringApplication app = new SpringApplicationBuilder(
        com.per.AnalysisApplication.class).web(true)
        .build();
    Environment env = app.run(args).getEnvironment();
    String protocol = "http";
    if (env.getProperty("server.ssl.key-store") != null) {
      protocol = "https";
    }
    log.info(
        "\n---------------------------------------------------------------------------------------\n\t"
            +
            "Application '{}' is running! Access URLs:\n\t" +
            "Local: \t\t{}://localhost:{}/swagger-ui.html\n\t" +
            "External: \t{}://{}:{}/swagger-ui.html\n\t" +
            "\n---------------------------------------------------------------------------------------",
        env.getProperty("spring.application.name"),
        protocol,
        env.getProperty("server.port"),
        protocol,
        InetAddress.getLocalHost().getHostAddress(),
        env.getProperty("server.port"));
  }

}
