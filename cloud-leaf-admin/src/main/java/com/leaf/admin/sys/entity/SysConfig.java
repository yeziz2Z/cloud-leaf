package com.leaf.admin.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.leaf.admin.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 参数配置表
 * </p>
 *
 * @author liuk
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_config")
public class SysConfig extends BaseEntity implements Serializable {


    /**
     * 参数主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数键名
     */
    private String key;

    /**
     * 参数键值
     */
    private String value;

    /**
     * 系统内置（Y是 N否）
     */
    private String type;

    /**
     * 备注
     */
    private String remark;


}
