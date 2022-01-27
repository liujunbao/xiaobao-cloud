package com.example.demo.module.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * easyExcel不可以用这个注解@Accessors(chain = true)
 */
@Data
//@Accessors(chain = true)
public class DeviceImportDTO {
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备编号
     */
    private String deviceId;
    /**
     * CCID
     */
    private String deviceCcid;
    /**
     * test
     */
    private String test;
}
