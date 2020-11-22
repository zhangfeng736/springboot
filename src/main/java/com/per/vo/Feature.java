package com.per.vo;

import java.util.Date;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "feature")
@ToString
@CompoundIndexes({@CompoundIndex(name = "feature_query_INDEX", def = "{'LAST_UPDATE_TIME':-1,'FACE_ENGINE_ID':1,'CONCERN_TYPE':1,'DELETE_STATUS':1}",background = true)})
public class Feature {

    @Id
    @Field("id")
    private String id;
    /**
     * 管控特征编码
     */
    @Field("MPSA_FEATURE_NO")
    @Indexed(unique = true, background = true, direction = IndexDirection.ASCENDING)
    private String mpsaFeatureNo;
    /**
     * 特征
     */
    @Field("FEATURE")
    private String feature;
    /**
     * 报警级别
     */
    @Field("ALARM_LV")
    private String alarmLv;
    /**
     * 行政区域
     */
    @Field("AREA_CODE")
    private String areaCode;
    /**
     * 人员类型
     */
    @Field("TYPE")
    private String type;

    /**
     * 关注人员类型
     */
    @Field("CONCERN_TYPE")
    private String concernType;


    /**
     * 特征标签
     */
    @Field("LABEL")
    private String label;

    /**
     * 特征质量
     */
    @Field("FEATURE_QUATITY")
    private String featureQuatity;

    /**
     * 流水号码
     */
    @Field("SERIAL_NO")
    private String serialNo;
    /**
     * 特征状态
     */
    @Field("STATUS")
    private Integer status;
    /**
     * 更新时间
     */
    @Field("MODIFY_TIME")
    private Date modifyTime;
    /**
     * 创建时间
     */
    @Field("CREATE_TIME")
    private Date createTime;
    /**
     * 人像算法引擎id
     */
    @Field("FACE_ENGINE_ID")
    private String faceEngineId;
    /**
     * 库ID,表示全国重点人员库
     */
    @Field("TABID")
    private String tabId;

    /**
     * 库名称
     */
    @Field("TABNAME")
    private String tabName;
    /**
     * 最后修改时间
     */
    @Field("LAST_UPDATE_TIME")
    private Date lastUpdateTime;
    /**
     * 是否注销
     */
    @Field("DELETE_STATUS")
    private Integer deleteStatus;

    /**
     * 注销时间
     */
    @Field("CANCEL_TIME")
    private Date cancelTime;

}
