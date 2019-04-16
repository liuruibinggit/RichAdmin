package cn.richinfo.richadmin.ExceptionHandle;

import cn.richinfo.richadmin.Entity.project.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by :LiuRuibing
 * date:2019/4/9 .
 * desc:全局异常类
 */
@ControllerAdvice
public class GlobalExceptionResolver {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionResolver.class);
    /**
     * 处理所有不可知异常
     * @param e 异常
     * @return json结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultVo handleException(Exception e) {
        // 打印异常堆栈信息
        LOG.error(e.getMessage(), e);
        return new ResultVo(ResultCode.UNKNOWN_ERROR);
    }

    //测试算数异常
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public ResultVo handleException(ArithmeticException e) {
        LOG.error(e.getMessage(), e);
        return new ResultVo(ResultCode.ART_EXCEPTION);
    }

}
