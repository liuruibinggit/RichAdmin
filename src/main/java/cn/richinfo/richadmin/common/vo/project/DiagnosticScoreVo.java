package cn.richinfo.richadmin.common.vo.project;

import lombok.Data;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Data
public class DiagnosticScoreVo {
    private Double score;
    private Double rollingMaoriScore;//滚动毛利得分

    private Double acceptanceProgressScore;//验收进度得分

    private Double returnProgressScore; //回款进度得分

    private Double settlementRateScore;//结算率得分

    private Double settlementScore;//结算额得分

    private String projectStatus;
}
