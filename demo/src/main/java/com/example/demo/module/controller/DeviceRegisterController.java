package com.example.demo.module.controller;

import com.example.demo.module.entity.DeviceRegister;
import com.example.demo.module.service.DeviceRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiaobao
 */
@RestController
@RequestMapping("deviceRegister")
@RequiredArgsConstructor
public class DeviceRegisterController {

    private final DeviceRegisterService deviceRegisterService;

    /**
     * 数据导出
     */
    @GetMapping("export")
    public void export(HttpServletResponse response) throws IOException {
        deviceRegisterService.export(response);
    }

    /**
     * 数据导出
     */
    @GetMapping("info/{id}")
    public DeviceRegister statistics(@PathVariable String id) throws IOException {
        return deviceRegisterService.queryById(id);
    }

    /**
     * 数据导入
     */
    @PostMapping("import")
    public void importExcel(MultipartFile file) throws IOException {
        deviceRegisterService.importExcel(file);
    }

    /**
     * 下载
     */
    @GetMapping("download")
    public void download(HttpServletResponse response) throws IOException {
        deviceRegisterService.download(response);
    }
}

