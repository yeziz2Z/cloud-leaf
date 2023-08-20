package com.leaf.common.web.config;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Configuration
@ConfigurationProperties(prefix = "cloud.leaf")
public class WebConfig {

    @ConditionalOnMissingBean(value = PasswordEncoder.class)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * LocalDate LocalDateTime 支持 以及 字符串 trim
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {

            // jdk8 日期格式
            builder.simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));

            //反序列化成 java 对象时 对 String 属性 trim处理
            builder.deserializerByType(String.class, new StdScalarDeserializer<String>(String.class) {
                @Override
                public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                    return StrUtil.trim(jsonParser.getValueAsString());
                }
            });
        };
    }

    /**
     * 表单类型 String 类型参数 trim
     * 此处不能 使用 lambda 表达式 由于泛型擦除 会导致无法启动
     */
    @Bean
    public Converter<String, String> trimStringConverter() {
        return new Converter<String, String>() {
            @Override
            public String convert(@Nullable String value) {
                return StrUtil.trim(value);
            }
        };
    }

}
