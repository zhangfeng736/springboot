package com.per.control;

import com.per.fork.SplitListUtilListThread;
import com.per.mongoBatch.FeatureRepository;
import com.per.vo.Feature;
import io.swagger.annotations.Api;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
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
  @Autowired
  private FeatureRepository featureRepository;

  @Autowired
  private MongoOperations mongoOperations;

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
    Set<String> mpsaSet = new HashSet<>();
    SplitListUtilListThread.<Feature>split(keyList, (paraList) -> mongoTemplate
        .find(
            Query.query(Criteria.where("MPSA_FEATURE_NO").in(paraList)),
            Feature.class)).forEach(feature -> {
      mpsaSet.add(feature.getMpsaFeatureNo());
      log.info("mpsaNo={}", feature.getMpsaFeatureNo());
    });
  }

  @PostMapping("testUpdate")
  public void update(@RequestBody List<String> keyList) {

    List<Feature> featureList = SplitListUtilListThread.<Feature>split(keyList,
        (paraList) -> mongoTemplate
            .find(
                Query.query(Criteria.where("MPSA_FEATURE_NO").in(paraList)),
                Feature.class)).stream().map(feature -> {
      feature.setLabel("123");
      return feature;
    }).collect(Collectors.toList());

    BulkOperations bulkOperations = mongoOperations
        .bulkOps(BulkOperations.BulkMode.ORDERED, Feature.class);

    long t0 = System.currentTimeMillis();
    for (int i = 0; i < 1; i++) {
      featureRepository.save(featureList);
    }
    log.info("duration-0={}", System.currentTimeMillis() - t0);

    List<Pair<Query, Update>> updateList = new ArrayList<>(featureList.size());
    featureList.forEach(feature -> {
      Update update = new Update();
      update.set("DELETE_STATUS", 0);
      update.set("CANCEL_TIME", feature.getCancelTime());
      update.set("LAST_UPDATE_TIME", feature.getLastUpdateTime());
      update.set("FEATURE", feature.getFeature());
      update.set("CONCERN_TYPE", feature.getConcernType());
      update.set("ALARM_LV", feature.getAlarmLv());
      update.set("FEATURE_QUATITY", feature.getFeatureQuatity());
      update.set("SERIAL_NO", feature.getSerialNo());
      update.set("MODIFY_TIME", feature.getModifyTime());
      update.set("CREATE_TIME", feature.getCreateTime());
      update.set("FACE_ENGINE_ID", feature.getFaceEngineId());
      update.set("TABID", feature.getTabId());
      Query query = Query.query(
          Criteria.where("MPSA_FEATURE_NO").is(feature.getMpsaFeatureNo()));
      Pair<Query, Update> updatePair = Pair.of(query, update);
      updateList.add(updatePair);
    });
    bulkOperations.upsert(updateList);
    long t1 = System.currentTimeMillis();
    for (int i = 0; i < 1; i++) {
      bulkOperations.execute();
    }

    log.info("duration-1={}", System.currentTimeMillis() - t1);

  }
}
