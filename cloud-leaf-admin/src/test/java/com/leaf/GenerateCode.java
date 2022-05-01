package com.leaf;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class GenerateCode {

    /*public static void main(String[] args) {
        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder("jdbc:mysql://127.0.0.1:3306/cloud-leaf-admin",
                "root",
                "root").build();

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig.Builder()
                .outputDir("E:\\code-gen")
                .author("liuk")
                .commentDate("yyyy-MM-dd")
                .dateType(DateType.TIME_PACK)
                .build();

        // 模板引擎
        TemplateConfig templateConfig = new TemplateConfig
                .Builder()
                .entity("templates/entity.java")
                .mapper("templates/mapper.java")
                .service("templates/service.java")
                .serviceImpl("templates/serviceImpl.java")
                .xml("/templates/mapper.xml")
                .controller(null)
                .build();

        //生成策略配置
        StrategyConfig strategyConfig = new StrategyConfig
                .Builder()
                .addTablePrefix("t")
                .addInclude("t_sys_menu")
                .entityBuilder()
                .enableLombok()
                .idType(IdType.ASSIGN_ID)
                .naming(NamingStrategy.underline_to_camel)
                .columnNaming(NamingStrategy.underline_to_camel)
                .mapperBuilder()
                .enableBaseColumnList()
                .enableBaseResultMap()
                .build();

        //包配置
        PackageConfig packageConfig = new PackageConfig
                .Builder()
                .parent("com.leaf.admin")
                .moduleName("sys")
                .mapper("mapper")
                .pathInfo(Collections.singletonMap(OutputFile.xml, "E:\\code-gen\\mapper"))
                .build();


        ConfigBuilder config = new ConfigBuilder(packageConfig, dataSourceConfig, strategyConfig, templateConfig, globalConfig, null);
//        config.getPathInfo().put(ConstVal.XML_PATH, "E:\\code-gen\\mapper");
        AutoGenerator ag = new AutoGenerator(dataSourceConfig);
        ag.config(config);
        ag.execute(new FreemarkerTemplateEngine());


    }*/

}
