package com.sensonet.service;

//import com.baomidou.mybatisplus.extension.service.IService;
//import com.sensonet.dto.DeviceDetailDTO;
//import com.sensonet.dto.DeviceLocation;
//import com.sensonet.entity.GPSEntity;
//
//import java.util.List;
//import java.util.Map;
//
//public interface GpsService extends IService<GPSEntity>{
//
//    /**
//     * 修改gps指标
//     * @param gpsEntity
//     * @return
//     */
//    boolean update(GPSEntity gpsEntity);
//
//    GPSEntity getGps();
//
//
//    /**
//     * 分析gps
//     * @param topic
//     * @param payloadMap
//     * @return
//     */
//    DeviceLocation analysis(String topic,Map<String,Object> payloadMap);
//
//
//    /**
//     * 根据经纬度获取一定范围内的设备信息
//     * @param lat
//     * @param lon
//     * @param distance
//     * @return
//     */
//    List<DeviceDetailDTO> getDeviceFullInfo(Double lat, Double lon, Integer distance );
//
//}
