package cn.richinfo.richadmin.Entity.finance;

import lombok.Data;

/**
 * Created by LiuRuibing on 2019/4/9 0009.
 */
@Data
public class BusinessIncomeMargin {
    private Integer id;

    private String customerName;

    private String businessType;

    private String projectName;

    private String projectNum;

    private String  projectType;

    private String signingBusinessUnit;

    private String team;

    private String saleName;

    private String orderType;

    private Double monthActual;

    private Double monthKpi;

    private Double monthMargin;

    private Double monthRate;

    private String date;

    private String year;

    private String month;

    private String businessLine;

    private Integer startMonth;

    private Integer endMonth;

    private String order;//排序类型
}
