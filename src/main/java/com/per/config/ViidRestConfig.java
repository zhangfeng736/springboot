package com.per.config;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.megvii.framework.util.jackson.MegviiInstantSerializer;
import com.megvii.framework.util.jackson.ObjectMapperProducer;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ViidRestConfig {


  private static final ObjectMapper OBJECT_MAPPER;

  static {
    Map<Class, JsonSerializer> serializerMap = new HashMap<>();
    serializerMap.put(Instant.class, MegviiInstantSerializer.INSTANCE.withFormat(false, null));
    OBJECT_MAPPER = ObjectMapperProducer.produce(serializerMap, null, null, null);
  }

  @Bean
  public RestTemplate viidAdapterRestTemplate() {
    OkHttpClient okClient = new OkHttpClient.Builder()
        .connectionPool(new ConnectionPool(20, 2L, TimeUnit.MINUTES)).build();

    OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory(okClient);
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters()
        .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
    restTemplate.setRequestFactory(factory);
    List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
    interceptors.add((request, body, execution) -> {
      HttpHeaders headers = request.getHeaders();
      headers.set("Accept", "application/VIID+JSON;charset=UTF-8");
      headers.set("Content-Type", "application/VIID+JSON;charset=UTF-8");
      headers.add("User-Identify", "12332222");
      return execution.execute(request, body);
    });
    setJacksonMapper(restTemplate);
    restTemplate.setInterceptors(interceptors);
    return restTemplate;
  }

  private void setJacksonMapper(RestTemplate restTemplate) {
    restTemplate.getMessageConverters().forEach(httpMessageConverter -> {
      if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
        MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) httpMessageConverter;
        jsonConverter.setObjectMapper(OBJECT_MAPPER);
      }
    });
  }

}
