//package com.example.demo.runner;
//
//import cn.hutool.core.collection.CollectionUtil;
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import com.example.demo.module.entity.DeviceRegister;
//import com.example.demo.module.service.DeviceRegisterService;
//import com.example.demo.util.RedisUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * 批量操作测试 - update
// *
// * @author xiaobao
// */
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class BatchTestRunner implements CommandLineRunner {
//
//    private final RedisUtil redisUtil;
//
//    private final DeviceRegisterService deviceRegisterService;
//
//    @Override
//    public void run(String... args) {
//        log.info("更新设备状态 定时任务开始");
//
//        List<Object> list = redisUtil.hgetAllList("device:status:device.number");
//        if (CollectionUtil.isNotEmpty(list)) {
//            List<DeviceRegister> collect = list.stream().map(l -> {
//                JSONObject jsonObject = JSONUtil.parseObj(l);
//                String sn = jsonObject.getStr("device_imei");
//                Boolean status = jsonObject.getBool("device_status");
//                Date date = jsonObject.getDate("device_date");
//                if (!status) {
//                    date = null;
//                }
//                Integer onlineStatus = status ? 1 : 2;
//                Integer accessType = jsonObject.getInt("access_type");
//
//                return new DeviceRegister()
//                        .setDeviceId(sn)
//                        .setOnlineStatus(onlineStatus)
//                        .setAccessType(accessType)
//                        .setLastHeartbeatTime(date);
//            }).collect(Collectors.toList());
//
//            //1w条数据。1w感觉还是太少，差不了多少时间
//            //14s
//            //deviceRegisterService.updateBatchByBatch(collect);
//            //7s
//            //deviceRegisterService.updateBatch(collect);
//            //25s，这个时间不长感觉得益与HikariPool连接池
//            deviceRegisterService.updateBatchFor(collect);
//        }
//
//    }
//}
