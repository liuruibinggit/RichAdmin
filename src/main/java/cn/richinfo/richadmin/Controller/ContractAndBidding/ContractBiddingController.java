package cn.richinfo.richadmin.Controller.ContractAndBidding;

import cn.richinfo.richadmin.Entity.project.ResultVo;
import cn.richinfo.richadmin.SchedualConfigs.NoContractWinBiddingTask;
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
@RequestMapping(value = "/ContractBidding")
public class ContractBiddingController {

    @Autowired
    private NoContractWinBiddingTask noContractWinBiddingTask;

    /**
     * #定时计算项目考核

     *winBiddingDate=0 0 0/4 * * ?
     */
    @RequestMapping(value = "/winBiddingDate")
    @ResponseBody
    @Scheduled(cron = ConstantClassField.WINBIDDINGDATE)
    public ResultVo winBiddingDate() throws ParseException {
        noContractWinBiddingTask.run();
        return new ResultVo("0","定时计算考核信息任务结束..................................");
    }
}
