package com.example.demo.module.service;

import com.example.demo.module.entity.DeviceRegister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 设备注册表(DeviceRegister)表服务接口
 *
 * @author xiaobao
 * @since 2022-01-08 14:21:45
 */
public interface DeviceRegisterService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DeviceRegister queryById(String id);

    /**
     * 分页查询
     *
     * @param deviceRegister 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<DeviceRegister> queryByPage(DeviceRegister deviceRegister, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param deviceRegister 实例对象
     * @return 实例对象
     */
    DeviceRegister insert(DeviceRegister deviceRegister);

    /**
     * 修改数据
     *
     * @param deviceRegister 实例对象
     * @return 实例对象
     */
    DeviceRegister update(DeviceRegister deviceRegister);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

    void updateBatch(List<DeviceRegister> collect);
    void updateBatchByBatch(List<DeviceRegister> collect);
    void updateBatchFor(List<DeviceRegister> collect);

    void export(HttpServletResponse response) throws IOException;

    void importExcel(MultipartFile file) throws IOException;

    void download(HttpServletResponse response) throws IOException;
}
