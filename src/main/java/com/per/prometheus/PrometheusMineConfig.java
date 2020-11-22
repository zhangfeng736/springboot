package com.per.prometheus;

import io.micrometer.spring.autoconfigure.export.prometheus.PrometheusMetricsExportAutoConfiguration;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

@Aspect
@Configuration
@Slf4j
public class PrometheusMineConfig {

  private final Map<String, Counter> prometheusMap = new ConcurrentHashMap<>();
  @Autowired
  private CollectorRegistry collectorRegistry;
  
  @PostConstruct
  private void init(){
    log.info("hello");
  }

  @Pointcut("@annotation(com.per.prometheus.PrometheusCounter)")
  public void annotatedMethod() {
  }

  @Pointcut("execution(* com.per.service.TestService.*(..))") 
  public void annotatedMethod1() {
  }

//  @Around("annotatedMethod1()")
  public Object dealCounter(ProceedingJoinPoint pjp) throws Throwable {

    PrometheusCounter prometheusCounter = getAnnotation(pjp);
    Counter counter = getCounter(prometheusCounter);
    counter.inc();
    return pjp.proceed();
  }

//  @Around("annotatedMethod()")
  public Object dealCounter1(ProceedingJoinPoint pjp) throws Throwable {

    PrometheusCounter prometheusCounter = getAnnotation(pjp);
    Counter counter = getCounter(prometheusCounter);
    counter.inc();
    return pjp.proceed();
  }

  @AfterReturning("@annotation(com.per.prometheus.PrometheusCounter)")
  public void dealCounter2(JoinPoint pjp) throws Throwable {
    PrometheusCounter prometheusCounter = getAnnotation(pjp);
    Counter counter = getCounter(prometheusCounter);
    counter.inc();
  }

  private PrometheusCounter getAnnotation(JoinPoint pjp) {
    MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
    PrometheusCounter prometheusCounter = AnnotationUtils
        .getAnnotation(methodSignature.getMethod(), PrometheusCounter.class);
    return prometheusCounter;
  }

  private Counter getCounter(PrometheusCounter prometheusCounter) {
    if (prometheusMap.get(prometheusCounter.name()) == null) {
      synchronized (this) {
        if (prometheusMap.get(prometheusCounter.name()) == null) {
          prometheusMap.put(prometheusCounter.name(), Counter.build()
              .name(prometheusCounter.name()).help(prometheusCounter.help())
              .register(collectorRegistry));
          return prometheusMap.get(prometheusCounter.name());
        }
      }
    }
    return prometheusMap.get(prometheusCounter.name());
  }
}
