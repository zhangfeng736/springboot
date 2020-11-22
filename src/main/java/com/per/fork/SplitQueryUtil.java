package com.per.fork;


import io.swagger.models.auth.In;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class SplitQueryUtil<T> {

  private int maxBatchSize = 30;

  public List<String[]> split(List<String> recordList) {
    String[] recordArray = new String[recordList.size()];
    recordList.toArray(recordArray);
    List<String[]> resultList = new ArrayList<>();
    if (recordList.size() <= maxBatchSize) {
      String[] subArray = new String[recordList.size()];
      recordList.toArray(subArray);
      resultList.add(subArray);
    }
    int pageNum = (recordList.size() - 1) / maxBatchSize + 1;
    for (int i = 0; i < pageNum; i++) {
      String[] abc = ArrayUtils.subarray(recordArray, i * maxBatchSize,
          i < pageNum - 1 ? i * maxBatchSize + maxBatchSize : recordList.size());
      resultList.add(abc);
    }
    return resultList;
  }

  public static void main(String[] args) {
    List<String> integerList = new ArrayList<>();
    for (int i = 0; i < 60; i++) {
      integerList.add(i + "");
    }
    SplitQueryUtil splitQueryUtil = new SplitQueryUtil();
    splitQueryUtil.split(integerList).
        forEach(subFeatureNo -> {
          Query
              .query(
                  Criteria.where("MPSA_FEATURE_NO").in(Arrays.asList(subFeatureNo))
                      .and("DELETE_STATUS").is(0));
          return;
        });


  }

}
