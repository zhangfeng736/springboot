package com.per.control;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlarmSendRequest {

  @JsonIgnore
  private String alarmNo;
  @JsonIgnore
  private Boolean duplicated;
  @JsonProperty("DispositionNotificationListObject")
  private DispositionNotificationListObjectBean dispositionNotificationListObject;

  @Data
  @Builder
  public static class DispositionNotificationListObjectBean {

    @JsonProperty("DispositionNotificationObject")
    private List<DispositionNotificationObjectBean> dispositionNotificationObject;
  }

  @Data
  @Builder
  public static class DispositionNotificationObjectBean {

    @ApiModelProperty("通知id(按1400规则)")
    @JsonProperty("NotificationID")
    private String notificationID;
    @ApiModelProperty("布控id，全国重点库布控,固定值010000001250350000010000000009999")
    @JsonProperty("DispositionID")
    private String dispositionID;
    @ApiModelProperty("布控标题")
    @JsonProperty("Title")
    private String title;
    @ApiModelProperty("告警触发时间")
    @JsonProperty("TriggerTime")
    private String triggerTime;
    @ApiModelProperty("人像算法引擎id（参考字典）")
    @JsonProperty("RXSFYQID")
    private String vendor;
    @ApiModelProperty("布控人脸信息")
    @JsonProperty("DispositionFaceObject")
    private DispositionFaceObject dispositionFaceObject;
    @ApiModelProperty("抓拍人脸信息")
    @JsonProperty("FaceObject")
    private FaceObject faceObject;
  }

  @Data
  @Builder
  public static class DispositionFaceObject {

    @ApiModelProperty("重点人唯一标识")
    @JsonProperty("ZDRID")
    private String zdrid;
    @ApiModelProperty("库id，全国ZDR库ID，固定值010000001250350000010000000009999000009999")
    @JsonProperty("TabID")
    private String tabID;
  }

  @Data
  @Builder
  public static class FaceObject {

    @ApiModelProperty("人脸标识")
    @JsonProperty("FaceID")
    private String faceID;
    @ApiModelProperty("设备id")
    @JsonProperty("DeviceID")
    private String deviceID;
    @ApiModelProperty("设备名称")
    @JsonProperty("DeviceName")
    private String deviceName;
    @ApiModelProperty("行政区划码")
    @JsonProperty("PlaceCode")
    private String placeCode;
    @ApiModelProperty("行政区划名称")
    @JsonProperty("PlaceCodeName")
    private String placeCodeName;
    @ApiModelProperty("经度")
    @JsonProperty("Longitude")
    private Double longitude;
    @ApiModelProperty("纬度")
    @JsonProperty("Latitude")
    private Double latitude;
    @ApiModelProperty("相似度")
    @JsonProperty("Similaritydegree")
    private Double similaritydegree;
    @ApiModelProperty("抓拍时间")
    @JsonProperty("ShotTime")
    private String shotTime;
    @JsonProperty("SubImageList")
    private SubImageList subImageList;
  }

  @Data
  @Builder
  public static class SubImageList {

    @JsonProperty("SubImageInfoObject")
    private List<SubImageInfoObject> subImageInfoObjects;
  }

  @Data
  @Builder
  public static class SubImageInfoObject {

    @ApiModelProperty("图片Id")
    @JsonProperty("ImageID")
    private String imageID;
    @ApiModelProperty("11人脸小图，14人脸场景图")
    @JsonProperty("Type")
    private String type;
    @ApiModelProperty("Type为11时，必填，base64编码")
    @JsonProperty("Data")
    private String data;
    @ApiModelProperty("图片url，在公安网应可访问。Type为11时可选，Type为14时和Data二选一")
    @JsonProperty("StoragePath")
    private String storagePath;
    @ApiModelProperty("图片高度，默认填充，参照扬州样例报文")
    @JsonProperty("Height")
    private Integer height;
    @ApiModelProperty("图片宽度，默认填充，参照扬州样例报文")
    @JsonProperty("Width")
    private Integer width;
  }

}
