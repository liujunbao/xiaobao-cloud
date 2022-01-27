package com.example.demo.module.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.IoUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.example.demo.module.dao.DeviceRegisterDao;
import com.example.demo.module.dto.DeviceImportDTO;
import com.example.demo.module.dto.WsdExportDTO;
import com.example.demo.module.entity.DeviceRegister;
import com.example.demo.module.service.DeviceRegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备注册表(DeviceRegister)表服务实现类
 *
 * @author xiaobao
 * @since 2022-01-08 14:21:45
 */
@Service("deviceRegisterService")
@RequiredArgsConstructor
@Slf4j
public class DeviceRegisterServiceImpl implements DeviceRegisterService {

    private final DeviceRegisterDao deviceRegisterDao;
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public DeviceRegister queryById(String id) {
        return this.deviceRegisterDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param deviceRegister 筛选条件
     * @param pageRequest    分页对象
     * @return 查询结果
     */
    @Override
    public Page<DeviceRegister> queryByPage(DeviceRegister deviceRegister, PageRequest pageRequest) {
        long total = this.deviceRegisterDao.count(deviceRegister);
        return new PageImpl<>(this.deviceRegisterDao.queryAllByLimit(deviceRegister, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param deviceRegister 实例对象
     * @return 实例对象
     */
    @Override
    public DeviceRegister insert(DeviceRegister deviceRegister) {
        this.deviceRegisterDao.insert(deviceRegister);
        return deviceRegister;
    }

    /**
     * 修改数据
     *
     * @param deviceRegister 实例对象
     * @return 实例对象
     */
    @Override
    public DeviceRegister update(DeviceRegister deviceRegister) {
        this.deviceRegisterDao.update(deviceRegister);
        return this.queryById(deviceRegister.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.deviceRegisterDao.deleteById(id) > 0;
    }

    @Override
    public void updateBatch(List<DeviceRegister> list) {
        TimeInterval timer = DateUtil.timer();
        List<DeviceRegister> tcpList = list.stream().filter(l -> l.getAccessType() == null).collect(Collectors.toList());
        int i = deviceRegisterDao.updateBatchDeviceId(tcpList);
        log.info("原生批量更新花费了{}秒", timer.intervalSecond());
    }

    @Override
    public void updateBatchByBatch(List<DeviceRegister> list) {
        TimeInterval timer = DateUtil.timer();
        List<DeviceRegister> tcpList = list.stream().filter(l -> l.getAccessType() == null).collect(Collectors.toList());
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        DeviceRegisterDao mapper = session.getMapper(DeviceRegisterDao.class);
        for (int i = 0; i < tcpList.size(); i++) {
            mapper.updateByDeviceId(tcpList.get(i));
//            if (i % 1000 == 0 || i == tcpList.size() - 1) {
//                session.commit();
//                session.clearCache();
//            }
        }
        session.commit();
        session.close();
        log.info("mybatis Batch花费了{}秒", timer.intervalSecond());
    }

    @Override
    public void updateBatchFor(List<DeviceRegister> list) {
        TimeInterval timer = DateUtil.timer();
        for (DeviceRegister deviceRegister : list) {
            deviceRegisterDao.updateByDeviceId(deviceRegister);
        }
        log.info("mybatis Batch花费了{}秒", timer.intervalSecond());
    }

    @Override
    public void export(HttpServletResponse response) throws IOException {
        WsdExportDTO wsdExportDTO = new WsdExportDTO().setDeviceName("1")
                .setDeviceNumber("1")
                .setCreateDate(new Date())
                .setHumidity(2.1)
                .setTemp(2.1)
                .setUnitName("1")
                .setLocation("1");
        List<WsdExportDTO> rows = new ArrayList<>();
        rows.add(wsdExportDTO);
        ExportParams exportParams = new ExportParams("温湿度", "温湿度");
        //对时间类型不好用
        //exportParams.setAutoSize(true);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, WsdExportDTO.class, rows);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("温湿度.xls", "UTF-8"));
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
        IoUtil.close(out);
    }

    @Override
    public void importExcel(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), DeviceImportDTO.class, new ReadListener<DeviceImportDTO>() {
            /**
             * 单次缓存的数据量
             */
            public static final int BATCH_COUNT = 100;
            /**
             * 临时存储
             */
            private List<DeviceRegister> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

            @Override
            public void invoke(DeviceImportDTO o, AnalysisContext analysisContext) {
                DeviceRegister deviceRegister = BeanUtil.copyProperties(o, DeviceRegister.class);
                cachedDataList.add(deviceRegister);
                if (cachedDataList.size() >= BATCH_COUNT) {
                    cachedDataList.forEach(System.out::println);
                    deviceRegisterDao.insertBatch(cachedDataList);
                    // 存储完成清理 list
                    cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                cachedDataList.forEach(System.out::println);
                // 这里也要保存数据，确保最后遗留的数据也存储到数据库
                deviceRegisterDao.insertBatch(cachedDataList);
            }
        }).sheet().doRead();
    }

    @Override
    public void download(HttpServletResponse response) throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("/excel/设备导入模板.xlsx");
        if (inputStream == null) {
            return;
        }
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("设备导入模板.xls", "UTF-8"));
        ServletOutputStream out = response.getOutputStream();
        byte[] buff = new byte[1024];
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        int i = bis.read(buff);
        while (i != -1) {
            out.write(buff);
            out.flush();
            i = bis.read(buff);
        }
    }
}
