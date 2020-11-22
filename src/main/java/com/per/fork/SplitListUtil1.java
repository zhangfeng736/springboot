package com.per.fork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.formula.functions.T;


@Slf4j
public class SplitListUtil1 {

  public static Integer maxBatchSize = 50;

  public static <T> List<T> split(List<String> recordList, Gan gan) {

    if (recordList.size() <= maxBatchSize) {
      return gan.doWork(recordList.stream().toArray(String[]::new));
    }
    int pageNum = (recordList.size() - 1) / maxBatchSize + 1;
    List<T> result = new ArrayList<>();
    for (int i = 0; i < pageNum; i++) {
      String[] subList = ArrayUtils
          .subarray(recordList.toArray(new String[recordList.size()]), i * maxBatchSize,
              i < pageNum - 1 ? i * maxBatchSize + maxBatchSize : recordList.size());
      result.addAll(gan.doWork(subList));
    }
    return result;
  }

  @FunctionalInterface
  public interface Gan<T> {

    List<T> doWork(String[] recordList);
  }

  public static void main(String[] args) {
    List<String> integerList = new ArrayList<>();
    for (int i = 0; i < 60; i++) {
      integerList.add(i + "");
    }
    SplitListUtil1.split(integerList, (abc) -> {
      for (int i = 0; i < abc.length; i++) {
        log.warn("i={}", i);
      }
      return null;
    });
  }

}