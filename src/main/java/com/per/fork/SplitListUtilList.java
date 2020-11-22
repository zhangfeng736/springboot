package com.per.fork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SplitListUtilList {

  public static Integer maxBatchSize = 50;

  public static <T> List<T> split(List<String> recordList, Gan gan) {

    if (recordList.size() <= maxBatchSize) {
      return gan.doWork(recordList);
    }
    int pageNum = (recordList.size() - 1) / maxBatchSize + 1;
    List<T> result = new ArrayList<>();
    for (int i = 0; i < pageNum; i++) {
      result.addAll(gan.doWork(recordList.subList(i * maxBatchSize,
          i < pageNum - 1 ? i * maxBatchSize + maxBatchSize : recordList.size())));
    }
    return result;
  }

  @FunctionalInterface
  public interface Gan<T> {

    List<T> doWork(List recordList);
  }

  public static void main(String[] args) {
    List<String> integerList = new ArrayList<>();
    for (int i = 0; i < 60; i++) {
      integerList.add(i + "");
    }
    SplitListUtilList.split(integerList, (abc) -> {
      for (int i = 0; i < abc.size(); i++) {
        log.warn("i={}", abc.get(i));
      }
      return Collections.emptyList();
    });
  }

}