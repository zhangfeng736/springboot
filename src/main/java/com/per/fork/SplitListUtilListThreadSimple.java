package com.per.fork;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SplitListUtilListThreadSimple {

  public static Integer maxBatchSize = 50;

  public static <T> List<T> split(List<String> recordList, Gan gan) {

    if (recordList.size() <= maxBatchSize) {
      return gan.doWork(recordList);
    }
    int pageNum = (recordList.size() - 1) / maxBatchSize + 1;
    List<T> result = new ArrayList<>();
    ForkJoinPool commonPool = ForkJoinPool.commonPool();
    List<ForkJoinTask<List<T>>> forkJoinTasks = new ArrayList<>();
    for (int i = 0; i < pageNum; i++) {
      List<String> list = recordList.subList(i * maxBatchSize,
          i < pageNum - 1 ? i * maxBatchSize + maxBatchSize : recordList.size());
      forkJoinTasks.add(commonPool.submit(() -> gan.doWork(list)));
    }
    forkJoinTasks.forEach(forkJoinTask -> {
      try {
        result.addAll(forkJoinTask.get());
      } catch (Exception e) {
        log.info("并发查询失败", e);
        throw new RuntimeException(e);
      }
    });
    return result;
  }

  @FunctionalInterface
  public interface Gan<T> {

    List<T> doWork(List recordList);
  }

}