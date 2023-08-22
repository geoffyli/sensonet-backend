package com.sensonet.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import com.sensonet.dto.DeviceInfoDTO;
import com.sensonet.dto.QuotaDTO;
import com.sensonet.dto.QuotaInfo;
import com.sensonet.entity.QuotaEntity;

import java.util.List;
import java.util.Map;

public interface QuotaService extends IService<QuotaEntity>{

    IPage<QuotaEntity> queryPage(Long page, Long pageSize,String name);
    List<String> getAllSubject();


    /**
     * 指标分析
     * @param topic 主题
     * @param payloadMap 报文
     * @return
     */
    DeviceInfoDTO analysis(String topic,Map<String,Object> payloadMap);


    /**
     * Save the quota info to influxdb
     * @param quotaDTOList The quota info list
     */
    void saveQuotaToInflux(List<QuotaDTO> quotaDTOList);


    /**
     * Get the last quota list by device id
     * @param deviceId The device id
     * @return The last quota list
     */
    List<QuotaInfo> getLastQuotaList(String deviceId);


//    /**
//     * 获取数值型指标列表
//     * @param page
//     * @param pageSize
//     * @return
//     */
//    IPage<QuotaEntity> queryNumberQuota(Long page,Long pageSize);


}