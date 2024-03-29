package com.sensonet.mapper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName(value = "tb_admin")
@Data
public class AdminEntity implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String adminName;
    private String password;
}
