package cn.richinfo.richadmin.Controller.Financial;

import cn.richinfo.richadmin.Entity.project.KpiFinishAnalyze;
import cn.richinfo.richadmin.Service.finance.KpiFinishService;
import cn.richinfo.richadmin.common.model.finance.KpiAnalyzeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by： LiuRuibing
 * date:2019/4/9
 * desc:kpi相关计算
 */
@RestController
@RequestMapping(value = "/kpi")
public class KpiController {

    @Autowired
    private KpiFinishService kpiFinishService;

    @RequestMapping(value = "/listKpiFinishAnalyze")
    public List<KpiFinishAnalyze> listKpiFinishAnalyze(KpiAnalyzeModel kpiAnalyzeModel){
        kpiFinishService.list(kpiAnalyzeModel);
        return null;
    }
}
