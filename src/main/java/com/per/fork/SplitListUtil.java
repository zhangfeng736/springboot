package com.per.fork;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class SplitListUtil<T> {

  private int maxBatchSize = 500;

  public List<T[]> split(List<T> recordList, Class<T> z) {
    T[] array = (T[]) Array
        .newInstance(z, recordList.size());
    recordList.toArray(array);
    List<T[]> result = new ArrayList<>();

    if (recordList.size() <= maxBatchSize) {
      T[] arrayRes = (T[]) Array
          .newInstance(z, recordList.size());
      recordList.toArray(arrayRes);
      T[] subResult = arrayRes;
      result.add(subResult);
      return result;
    }
    int pageNum = (recordList.size() - 1) / maxBatchSize + 1;
    for (int i = 0; i < pageNum; i++) {

      T[] abc = ArrayUtils.subarray(array, i * maxBatchSize,
          i < pageNum - 1 ? i * maxBatchSize + maxBatchSize : recordList.size());
      result.add(abc);
    }
    return result;
  }

  public static void main(String[] args) {
    List<String> integerList = new ArrayList<>();
    for (int i = 0; i < 60; i++) {
      integerList.add(i + "");
    }
    SplitListUtil splitListUtil = new SplitListUtil();
    List<String[]> resultList = splitListUtil.split(integerList, String.class);
//    splitListUtil.split(integerList, String.class).
    resultList.
        forEach(intArray ->
        {
          System.out.println("length0=" + Arrays.asList(intArray).size());

          Query query = Query.query(Criteria.where("asdf").in(intArray));
          System.out.println("query=" + query.toString());
          String[] af = (String[]) intArray;
          List i = Arrays.asList(af);
          System.out.println("length=" + i.size());
        });

  }

}
