package com.example.demo.module.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class WsdExportDTO {
    @Excel(name = "设备编号")
    private String deviceNumber;
    @Excel(name = "设备名称", width = 20.0)
    private String deviceName;
    @Excel(name = "所属单位")
    private String unitName;
    @Excel(name = "具体安装位置")
    private String location;
    @Excel(name = "温度")
    private Double temp;
    @Excel(name = "湿度")
    private Double humidity;
    @Excel(name = "采集时间", exportFormat = "yyyy-MM-dd HH:mm:ss", width = 20.0)
    private Date createDate;
}
