package cn.richinfo.richadmin.Entity.project;

import lombok.Data;

/**
 * Created by LiuRuibing on 2019/4/9 0009.
 */
@Data
public class KpiFinishAnalyze {

    private int id;

    private String kpiIndex;

    private String businessLine;

    private String businessLineId;

    private Double concurrentActual; //累计总计

    private Double concurrentKpi;//同期KPI

    private Double concurrentKpiRate; //同期KPI完成率

    private Double currentMargin; //同期差额

    private Double monthActual; //本月实际KPI

    private Double monthKpi ;//本月KPI

    private Double monthMargin;//本月差额

    private Double monthRate;//本月完成率

    private Double halfYearPredict;  //半年预测

    private Double halfYearKPI;  //半年KPI

    private Double halfYearKpiRate;//半年KPI完成率

    private Double halfYearKpiMargin; //半年差额

    private Double annualPredict; //年度预测

    private Double annualKpi; //年度KPI

    private Double annualKpiRate; //年度kpi完成率

    private Double annualMargin; //年度差额

    private Double yearEndPrediction;//年底前预测
}
