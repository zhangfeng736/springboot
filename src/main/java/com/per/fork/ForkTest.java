package com.per.fork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkTest extends RecursiveTask<List<String>> {

  private int start;
  private int end;
  private int maxRecord;
  private List<String> recordList;

  public ForkTest(int start, int end, int maxRecord, List<String> recordList) {
    this.start = start;
    this.maxRecord = maxRecord;
    this.recordList = recordList;
    this.end = end;
  }


  @Override
  protected List<String> compute() {
    try {
      List<String> featureList = new ArrayList<>();
      if (end - start < maxRecord + 1) {
        List<String> stringList = new ArrayList<>();
        for (int i = start; i < end; i++) {
          stringList.add(recordList.get(i));
        }
        System.out.println(String.join(",", stringList));
        return Collections.emptyList();
      }
      int mid = (end - start) / 2 + start;
      ForkTest left = new ForkTest(start, mid, maxRecord, recordList);
      ForkTest right = new ForkTest(mid, end, maxRecord, recordList);
      ForkJoinTask<List<String>> leftTask = left.fork();
      ForkJoinTask<List<String>> rightTask = right.fork();
      featureList.addAll(leftTask.get());
      featureList.addAll(rightTask.get());
      return featureList;
    } catch (Exception e) {
      e.printStackTrace();
      return Collections.emptyList();

    }
  }

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    List<String> arr = new ArrayList<>();
    for (int i = 0; i < 99; i++) {
      arr.add(i + "");
    }
    ForkJoinPool commonPool = ForkJoinPool.commonPool();

    ForkTest forkTask = new ForkTest(0, arr.size(), 10, arr);
    List<String> rest = commonPool.submit(forkTask).get();
  }
}
