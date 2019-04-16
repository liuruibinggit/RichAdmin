package cn.richinfo.richadmin.Service.finance;


import cn.richinfo.richadmin.Entity.project.KpiFinishAnalyze;
import cn.richinfo.richadmin.common.model.finance.KpiAnalyzeModel;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/9 0009.
 */
public interface KpiFinishService {
    List<KpiFinishAnalyze> list(KpiAnalyzeModel kpiAnalyzeModel);
    List<KpiFinishAnalyze> totalAnalyzeList(KpiAnalyzeModel kpiAnalyzeModel);
}
