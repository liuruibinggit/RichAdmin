package cn.richinfo.richadmin.Service.finance.financeImpl;


import cn.richinfo.richadmin.Entity.project.KpiFinishAnalyze;
import cn.richinfo.richadmin.Service.finance.KpiFinishService;
import cn.richinfo.richadmin.common.model.finance.KpiAnalyzeModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/9 0009.
 */
@Service
public class KpiFinishServiceImpl implements KpiFinishService {

    @Override
    public List<KpiFinishAnalyze> list(KpiAnalyzeModel kpiAnalyzeModel) {
        List<KpiFinishAnalyze> list = new ArrayList<>();


        return null;
    }

    @Override
    public List<KpiFinishAnalyze> totalAnalyzeList(KpiAnalyzeModel kpiAnalyzeModel) {
        return null;
    }

}
