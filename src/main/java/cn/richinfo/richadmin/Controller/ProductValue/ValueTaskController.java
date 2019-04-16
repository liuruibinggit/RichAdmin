package cn.richinfo.richadmin.Controller.ProductValue;

import cn.richinfo.richadmin.Entity.project.ResultVo;
import cn.richinfo.richadmin.SchedualConfigs.ValueTask;
import cn.richinfo.richadmin.common.ConstantClassField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;

/**
 * Created by LiuRuibing on 2019/4/16 0016.
 */
@Controller
@RequestMapping(value = "/valueTask")
public class ValueTaskController {

    @Autowired
    private ValueTask valueTask;

    /**
     * #定时计算项目考核
     * valueTask=0 0 1 5 * ?
     */

    @RequestMapping(value = "/valueTask")
    @ResponseBody
    @Scheduled(cron = ConstantClassField.VALUETASK)
    public ResultVo valueTask() throws ParseException {
        valueTask.run();
        return new ResultVo("0","valueTask任务结束..................................");
    }
}
