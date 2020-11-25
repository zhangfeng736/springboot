package com.per.mongoBatch;


import com.per.vo.Feature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeatureRepository extends MongoRepository<Feature, String> {

}
