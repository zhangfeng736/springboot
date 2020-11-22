//package com.per.config;
//
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.megvii.framework.util.jackson.MegviiInstantSerializer;
//import com.megvii.framework.util.jackson.ObjectMapperProducer;
//import java.time.Instant;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
//@Configuration
//public class JacksonConvertConfig extends WebMvcConfigurationSupport {
//
//  private static final ObjectMapper OBJECT_MAPPER;
//
//  static {
//    Map<Class, JsonSerializer> serializerMap = new HashMap<>();
//    serializerMap.put(Instant.class, MegviiInstantSerializer.INSTANCE.withFormat(false, null));
//    OBJECT_MAPPER = ObjectMapperProducer.produce(serializerMap, null, null, null);
//  }
//
//  @Bean
//  public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
//    MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
//    jsonConverter.setObjectMapper(OBJECT_MAPPER);
//    return jsonConverter;
//  }
//
////  @Override
//  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//    converters.add(customJackson2HttpMessageConverter());
//    super.addDefaultHttpMessageConverters(converters);
//  }
//
//}
