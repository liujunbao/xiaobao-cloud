package com.example.demo.module.dao;

import com.example.demo.module.entity.DeviceRegister;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 设备注册表(DeviceRegister)表数据库访问层
 *
 * @author xiaobao
 * @since 2022-01-08 14:21:44
 */
@Mapper
public interface DeviceRegisterDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DeviceRegister queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param deviceRegister 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<DeviceRegister> queryAllByLimit(DeviceRegister deviceRegister, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param deviceRegister 查询条件
     * @return 总行数
     */
    long count(DeviceRegister deviceRegister);

    /**
     * 新增数据
     *
     * @param deviceRegister 实例对象
     * @return 影响行数
     */
    int insert(DeviceRegister deviceRegister);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceRegister> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DeviceRegister> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DeviceRegister> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<DeviceRegister> entities);

    /**
     * 修改数据
     *
     * @param deviceRegister 实例对象
     * @return 影响行数
     */
    int update(DeviceRegister deviceRegister);

    int updateByDeviceId(DeviceRegister deviceRegister);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

    int updateBatchDeviceId(List<DeviceRegister> list);

}

