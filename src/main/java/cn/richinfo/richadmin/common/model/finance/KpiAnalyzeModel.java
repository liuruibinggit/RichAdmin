package cn.richinfo.richadmin.common.model.finance;

import lombok.Data;

/**
 * Created by: LiuRuibing
 * date:2019/4/9
 * desc:KpiAnalyzeModel
 */
@Data
public class KpiAnalyzeModel {
    private String businessLine;
    private String startTime;
    private String endTime;
    private String targetIndex;
}
