package com.leaf.admin.aspectj;


import cn.hutool.json.JSONUtil;
import com.leaf.admin.annotation.OperationLog;
import com.leaf.admin.async.factory.LogTaskFactory;
import com.leaf.admin.sys.entity.SysOperLog;
import com.leaf.common.enums.YesOrNoEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class OperationLogAspectj {

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Pointcut("@annotation(com.leaf.admin.annotation.OperationLog)")
    public void operationLogControllerPointCut() {
    }

    @Around(value = "operationLogControllerPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long currentTimeMillis = System.currentTimeMillis();
        SysOperLog operLog = new SysOperLog();
        try {
            Object result = null;
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            OperationLog operationLog = method.getAnnotation(OperationLog.class);
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            operLog.setModule(operationLog.module());
            operLog.setBusinessType(operationLog.businessType().getCode());
            operLog.setStatus(YesOrNoEnum.YES.getCode());
            operLog.setOperIp(request.getLocalAddr());
            operLog.setOperUrl(request.getRequestURI());
            operLog.setMethod(method.getName());
            operLog.setRequestMethod(request.getMethod());
//            operLog.setOperName(SecurityContextHolder.getContext().getAuthentication().getName());
            operLog.setOperName("admin");
            operLog.setOperTime(LocalDateTime.now());

            operLog.setOperParam(getParams(joinPoint.getArgs()));

            result = joinPoint.proceed();

            operLog.setExecuteTime(System.currentTimeMillis() - currentTimeMillis);
            operLog.setJsonResult(JSONUtil.toJsonStr(result));
            return result;
        } catch (Throwable throwable) {
            operLog.setExecuteTime(System.currentTimeMillis() - currentTimeMillis);
            operLog.setErrorMsg(throwable.getMessage());
            operLog.setStatus(YesOrNoEnum.NO.getCode());
            throw throwable;
        } finally {
            threadPoolTaskExecutor.execute(LogTaskFactory.operationLog(operLog));
        }

    }

    private String getParams(Object[] paramsArray) {
        StringBuilder res = new StringBuilder();
        if (paramsArray != null && paramsArray.length != 0) {
            res.append(Arrays.stream(paramsArray).filter(o -> !Objects.isNull(o)).map(JSONUtil::toJsonStr).collect(Collectors.joining(" ")));
        }
        return res.toString().trim();
    }
}
