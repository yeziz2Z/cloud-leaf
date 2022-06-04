package com.leaf.admin.config;

import cn.hutool.extra.spring.SpringUtil;
import com.leaf.admin.sys.service.ISysRoleService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 服务启动后执行初始化配置
 *
 * @author liuk
 */
@Configuration
public class InitConfig {

    /**
     * 刷新角色权限
     * @return
     */
    @Order(value = 1)
    @Bean
    public ApplicationRunner initRolePermissionApplicationRunner() {
        return args -> SpringUtil.getBean(ISysRoleService.class).refreshRolePermission();
    }
}
