package com.yikekong.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yikekong.emq.EmqClient;
import com.yikekong.entity.AlarmEntity;
import com.yikekong.exception.BussinessException;
import com.yikekong.service.AlarmService;
import com.yikekong.vo.Pager;
import com.yikekong.vo.QuotaVO;
import com.yikekong.entity.QuotaEntity;
import com.yikekong.service.QuotaService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quota")
public class QuotaController{

    @Autowired
    private QuotaService quotaService;
    @Autowired
    private AlarmService alarmService;

    @Autowired
    private EmqClient emqClient;

    /**
     * This method is used to create a new quota.
     * @param vo the quota to be created, which is received from the request body
     * @return true if the quota is created successfully, false otherwise
     */
    @PostMapping
    public boolean create(@RequestBody QuotaVO vo){
        try {
            // Create a new QuotaEntity object
            QuotaEntity quotaEntity = new QuotaEntity();
            // Copy properties from the QuotaVO (DTO) to the QuotaEntity (entity) using BeanUtils
            BeanUtils.copyProperties(vo,quotaEntity);
            // Subscribe to a topic based on the 'subject' field in the QuotaVO
            // The topic will be "$queue/{subject}" for the MQTT broker
            try {
                emqClient.subscribe("$queue/"+vo.getSubject());
            } catch (MqttException e) {
                e.printStackTrace();
            }
            // Save the QuotaEntity using the QuotaService object
            // The QuotaService object is responsible for handling CRUD operations for quotas
            return quotaService.save(quotaEntity);
        }catch (DuplicateKeyException e){
            // If a DuplicateKeyException occurs, it means there is already a quota with the same name
            // Throw a BussinessException with a custom message to indicate that the name already exists
            throw new BussinessException("已存在该名称");
        }
    }

    /**
     * 分页获取所有指标
     * @param page
     * @param pageSize
     * @param quotaName
     * @return
     */
    @GetMapping
    public Pager<QuotaEntity> queryPage(@RequestParam(value = "page",required = false,defaultValue = "1") Long page,
                                        @RequestParam(value = "pageSize",required = false,defaultValue = "10") Long pageSize,
                                        @RequestParam(value = "quotaName",required = false) String quotaName){
        return new Pager<>(quotaService.queryPage(page,pageSize,quotaName));
    }


    /**
     * 更新指标
     * @param vo
     * @return
     */
    @PutMapping
    public Boolean update(@RequestBody QuotaVO vo){
        try {
            QuotaEntity entity = new QuotaEntity();
            BeanUtils.copyProperties(vo,entity);

            return quotaService.updateById(entity);
        }catch (DuplicateKeyException e){
            throw new BussinessException("已存在该名称");
        }

    }

    /**
     * 删除指标
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id){
        QueryWrapper<AlarmEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(AlarmEntity::getQuotaId,id);
        Integer count = alarmService.count(queryWrapper);
        if(count>0)
            throw new BussinessException("该指标使用中");
        return quotaService.removeById(id);
    }


    /**
     * 分页获取数值型指标
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/numberQuota")
    public Pager<QuotaEntity> queryNumberQuota(
            @RequestParam(value = "page",required = false ,defaultValue = "1") Long page,
            @RequestParam(value = "pageSize",required = false ,defaultValue = "10")  Long pageSize ){

        IPage<QuotaEntity> pageResult = quotaService.queryNumberQuota(page, pageSize);
        return new Pager<>( pageResult );
    }

}
