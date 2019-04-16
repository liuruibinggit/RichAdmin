package cn.richinfo.richadmin.Config;

import cn.richinfo.richadmin.Entity.project.ResultVo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by LiuRuibing
 * date:2019/4/2 .
 * desc:统一日志处理AOP
 */

@Component
@Aspect
public class LogAspect {

    private final static Logger logger = LoggerFactory.getLogger(LogAspect.class);
    //对Controller层的所有方法进行横切
    @Pointcut("execution(public * cn.richinfo.richadmin.Controller.*.*(..))")
    public void controllerImplAopMethod() {

    }

    @Before("controllerImplAopMethod()")
    public void LogRequestInfo(JoinPoint joinPoint) throws Exception {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        StringBuffer requestLog = new StringBuffer();
        requestLog.append("请求信息：")
                .append("URL = {" + request.getRequestURI() + "},\t")
                .append("HTTP_METHOD = {" + request.getMethod() + "},\t")
                .append("IP = {" + request.getRemoteAddr() + "},\t")
                .append("CLASS_METHOD = {" + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "},\t");

        if(joinPoint.getArgs().length == 0) {
            requestLog.append("ARGS = {} ");
        } else {
            requestLog.append("ARGS = " + new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .writeValueAsString(joinPoint.getArgs()[0]) + "");
        }
        logger.info(requestLog.toString());
    }

    @AfterReturning(returning = "resultVO", pointcut = "controllerImplAopMethod()")
    public void logResultVOInfo(ResultVo resultVO) throws Exception {
        logger.info("请求结果：" + resultVO.getCode() + "\t" + resultVO.getMsg());
    }

}
