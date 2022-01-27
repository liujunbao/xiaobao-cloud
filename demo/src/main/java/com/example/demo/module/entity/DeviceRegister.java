package com.example.demo.module.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备注册表(DeviceRegister)实体类
 *
 * @author xiaobao
 * @since 2022-01-08 14:21:45
 */
@Data
@Accessors(chain = true)
public class DeviceRegister implements Serializable {
    private static final long serialVersionUID = -76405935080811585L;
    /**
     * 主键
     */
    private String id;
    /**
     * 所属项目ID
     */
    private String projectId;
    /**
     * 所属项目名称
     */
    private String projectName;
    /**
     * 所属应用ID
     */
    private Integer applicationId;
    /**
     * 所属应用名称
     */
    private String applicationName;
    /**
     * 所属租户ID
     */
    private Integer tenantId;
    /**
     * 设备类型ID
     */
    private Integer deviceTypeId;
    /**
     * 设备类型名称
     */
    private String deviceTypeName;
    /**
     * 设备厂商ID
     */
    private Integer deviceSupplierId;
    /**
     * 设备型号ID
     */
    private Integer deviceDictId;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备编号
     */
    private String deviceId;
    /**
     * 设备序列号
     */
    private String deviceSeriesNumber;
    /**
     * 硬件版本号
     */
    private String hardwareVersionNumber;
    /**
     * 软件版本号
     */
    private String softwareVersionNumber;
    /**
     * 协议版本
     */
    private String protocolVersion;
    /**
     * CCID
     */
    private String deviceCcid;
    /**
     * IMEI
     */
    private String deviceImei;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 联网类型
     */
    private String networkingType;
    /**
     * sim卡套餐
     */
    private String simPackage;
    /**
     * 单位名称
     */
    private String unitName;
    /**
     * 安装位置
     */
    private String address;
    /**
     * 设备上传间隔时间（毫秒）
     */
    private Integer uploadInterval;
    /**
     * 最后一次心跳时间
     */
    private Date lastHeartbeatTime;
    /**
     * 在线状态（0：未连接，1：在线，2：离线）
     */
    private Integer onlineStatus;
    /**
     * 状态（0：有效，1：无效）
     */
    private Integer status;
    /**
     * 是否报警  0 正常 1 故障  2 报警
     */
    private Integer policeStatus;
    /**
     * 设备所属项目的到期时间
     */
    private Date serviceExpirationTime;
    /**
     * 多个电池以逗号分隔（电量以百分比存储）
     */
    private String battery;
    /**
     * 信号
     */
    private String signalStrength;
    /**
     * sim是否异常 0 正常  1 异常
     */
    private Integer simAbnormal;
    /**
     * 是否可升级 0 不可  1 可
     */
    private Integer isUpgrade;
    /**
     * 创建时间（设备注册绑定时间）
     */
    private Date createTime;
    /**
     * 最后一次更新时间
     */
    private Date updateTime;
    /**
     * 单位id
     */
    private String companyId;
    /**
     * 租户id
     */
    private String tentId;
    /**
     * 创建时间
     */
    private Date crtTime;
    /**
     * 创建人编号
     */
    private String crtUser;
    /**
     * 创建人IP
     */
    private String crtHost;
    /**
     * 修改时间
     */
    private Date updTime;
    /**
     * 修改人编号
     */
    private String updUser;
    /**
     * 修改人地址
     */
    private String updHost;
    /**
     * 设备注册的同步状态
     */
    private Boolean syncStatus;
    /**
     * 设备注册的同步失败原因
     */
    private String syncFailureReason;
    /**
     * NB设备在NB平台的设备ID
     */
    private String nbDeviceId;
    /**
     * 设备的绑定状态（与业务端）: 0-未绑定，1-绑定
     */
    private Integer bindingStatus;
    /**
     * 是否是测试设备 0 不是  1是
     */
    private Integer isTest;
    /**
     * 视频平台
     */
    private String videoPlatform;
    /**
     * 视频通道id
     */
    private String videoChannelid;


    private Integer accessType;
}

