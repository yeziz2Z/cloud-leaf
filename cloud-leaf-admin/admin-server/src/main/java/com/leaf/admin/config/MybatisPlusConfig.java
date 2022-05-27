package com.leaf.admin.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.leaf.admin.common.SystemConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@MapperScan("com.leaf.admin.sys.mapper")
@Slf4j
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setMaxLimit(SystemConst.MAX_LIMIT);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        // 避免全表更新
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

//    @Bean
    public MetaObjectHandler saveAndUpdateHandler() {

        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
//                this.strictInsertFill(metaObject, "createBy", () -> SecurityContextHolder.getContext().getAuthentication().getName(), String.class);
                this.strictInsertFill(metaObject, "createBy", () -> "admin", String.class);
                this.strictInsertFill(metaObject, "createTime", () -> LocalDateTime.now(), LocalDateTime.class);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
//                this.strictUpdateFill(metaObject, "updateBy", () -> SecurityContextHolder.getContext().getAuthentication().getName(), String.class);
                this.strictUpdateFill(metaObject, "updateBy", () -> "admin", String.class);
                this.strictUpdateFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class);
            }
        };

    }
}