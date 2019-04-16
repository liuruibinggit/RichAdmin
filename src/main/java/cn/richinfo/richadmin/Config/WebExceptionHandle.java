package cn.richinfo.richadmin.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 * springmvc 全局异常处理
 */
@ControllerAdvice
@ResponseBody
public class WebExceptionHandle {

    private static Logger logger = LoggerFactory.getLogger(WebExceptionHandle.class);

          /**
           * 400 - Bad Request
           */
     // @ResponseStatus(HttpStatus.BAD_REQUEST)
    //  @ExceptionHandler(HttpMessageNotReadableException.class)
//       public ServiceResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
//               logger.error("参数解析失败", e);
//                return ServiceResponseHandle.failed("could_not_read_json");
//           }

}
