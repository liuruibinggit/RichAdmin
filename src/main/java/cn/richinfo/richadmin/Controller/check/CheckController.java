package cn.richinfo.richadmin.Controller.check;

import cn.richinfo.richadmin.Entity.project.ResultVo;
import cn.richinfo.richadmin.SchedualConfigs.CheckTask;
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
@RequestMapping(value = "/check")
public class CheckController {

    @Autowired
    private CheckTask checkTask;

    /**
     * #定时计算项目考核
     *checkDate=0 0 12 * * ?
     */
    @RequestMapping(value = "/checkDate")
    @ResponseBody
    @Scheduled(cron = ConstantClassField.CHECKDATE)
    public ResultVo checkDate() throws ParseException {
        checkTask.run();
        return new ResultVo("0","定时计算考核信息任务结束..................................");
    }
}
