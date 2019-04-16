package cn.richinfo.richadmin.Controller.project;

import cn.richinfo.richadmin.Entity.project.ResultVo;
import cn.richinfo.richadmin.SchedualConfigs.GetIncreaseHours;
import cn.richinfo.richadmin.Service.q5.Q5ProjectService;
import cn.richinfo.richadmin.common.ConstantClassField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by LiuRuibing
 * 2019/4/8.
 */
@Controller
@RequestMapping("/manHour")
public class ManHourController {

    @Autowired
    private Q5ProjectService q5ProjectService;

    @Autowired
    private GetIncreaseHours getIncreaseHours;

    //工时同步程序：支持手动同步和定时器自动同步
    @RequestMapping(value = "/manHourSynchronized")
    @ResponseBody
    //@Scheduled(cron = "0/5 * * ? * *")  //若啟用定時自動同步，可以打開
    public ResultVo manHourSynchronized(){
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        String month = String.valueOf(date.get(Calendar.MONTH));
        if("0".equals(month)){
            month="12";
            year=date.get(Calendar.YEAR)-1+"";
        }
        if(month.length() ==1){
            month="0"+month;
        }
        q5ProjectService.addQ5ProjectInit(year, month);
        return new ResultVo("1","同步工时完成");
    }

    //滚动工时同步程序：支持手动同步和定时器自动同步
    @RequestMapping(value = "/AddRollingCost")
    @ResponseBody
    @Scheduled(cron = ConstantClassField.GETINCREASEHOURSDATE)
    public ResultVo addRollingCost(){
        try {
            getIncreaseHours.run();
            return new ResultVo("1","同步滚动工时完成");
        }catch (IOException e) {
            e.printStackTrace();
            return new ResultVo("0","同步滚动工时出现IO异常",e);
        }catch (ParseException e){
            e.printStackTrace();
            return new ResultVo("0","同步滚动工时出现解析异常",e);
        }
    }
}
