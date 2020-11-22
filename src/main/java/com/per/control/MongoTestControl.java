package com.per.control;

import com.per.fork.SplitListUtil1;
import com.per.fork.SplitListUtilList;
import com.per.vo.Feature;
import io.swagger.annotations.Api;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/mongo")
@Api("Mongo测试")
@Slf4j
public class MongoTestControl {

  @Autowired
  private MongoTemplate mongoTemplate;

  @PostMapping("queryList")
  public void queryList(@RequestBody List<String> keyList) {
    List<Feature> features = mongoTemplate
        .find(Query.query(Criteria.where("MPSA_FEATURE_NO").in(keyList)), Feature.class);
    features.forEach(item -> System.out.println(item));
  }

  @PostMapping("queryArray")
  public void queryArray(@RequestBody String[] keyList) {
    List<Feature> features = mongoTemplate
        .find(Query.query(Criteria.where("MPSA_FEATURE_NO").in(keyList)), Feature.class);
    features.forEach(item -> System.out.println(item));
  }

  @PostMapping("queryArraySplit")
  public void queryArraySplit(@RequestBody List<String> keyList) {
    SplitListUtilList.<Feature>split(keyList, (paraList) -> mongoTemplate
        .find(
            Query.query(Criteria.where("MPSA_FEATURE_NO").in(paraList)),
            Feature.class)).forEach(feature -> {
              log.info("mpsaNo={}",feature.getMpsaFeatureNo());
    });
  }
}
