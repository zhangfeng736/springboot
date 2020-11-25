package com.per.getAnno;


import com.google.common.base.CaseFormat;
import com.per.vo.Feature;
import java.beans.Introspector;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FiledUtil {

  private static final Pattern GET_PATTERN = Pattern.compile("^get[A-Z].*");
  private static Map<String, String> fieldMap = new ConcurrentHashMap<>();
  private static Map<String, String> classMap = new ConcurrentHashMap<>();

  private FiledUtil() {
  }

  public static <T> String fnToFieldName(MFn<T> fn) {
    try {

      Method method = fn.getClass().getDeclaredMethod("writeReplace");
      method.setAccessible(Boolean.TRUE);
      SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fn);

      String getter = serializedLambda.getImplMethodName();
      if (!GET_PATTERN.matcher(getter).matches()) {
        throw new RuntimeException("method not matched!");
      }
      String className = serializedLambda.getImplClass().replace("/", ".");
      String cacheKey = className + getter;
      if (fieldMap.containsKey(cacheKey)) {
        return fieldMap.get(cacheKey);
      }

      String fieldName = getter.substring("get".length());
      fieldName = fieldName
          .replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());

      Field field = Class.forName(className).getDeclaredField(fieldName);
      org.springframework.data.mongodb.core.mapping.Field mongoField = field
          .getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class);
      if (mongoField != null) {
        fieldMap.put(cacheKey, mongoField.value());
        return mongoField.value();
      }
      fieldMap.put(cacheKey, fieldName);
      return fieldName;

    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  @FunctionalInterface
  public interface MFn<T> extends Serializable {

    Object getValue(T source);

  }

  public static void main(String[] args) {
    log.info(FiledUtil.fnToFieldName(Feature::getMpsaFeatureNo));
  }
}
