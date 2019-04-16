package cn.richinfo.richadmin.Entity.project;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Data
public class DiagnosticReport implements Serializable {
    private Integer diagnosticReportId;
    private String projectNum;
    private String projectName;
    private Integer cost;
    private Integer speed;
    private Integer manhour;
    private Integer payment;
    private Integer grossprofit;
    private Integer settlementr;
    private Integer costlevel;
    private String speedlevel;
    private Integer manhourlevel;
    private String paymentlevel;
    private Integer grossprofitlevel;
    private Integer settlementrlevel;
    private Double rollingMaoriScore;
    private Double acceptanceProgressScore;
    private Double returnProgressScore;
    private Double settlementRateScore;
    private Double score;
    private String projectStatus;
    private Date month;
    private Integer costComparedWithLastMonth; //成本和上个月的对比
    private Integer speedComparedWithLastMonth;  //进度和上个月的对比
    private Integer manhourComparedWithLastMonth; //工时和上个月的对比
    private Integer paymentComparedWithLastMonth;
    private Integer grossprofitComparedWithLastMonth;
    private Integer settlementrComparedWithLastMonth;
}
