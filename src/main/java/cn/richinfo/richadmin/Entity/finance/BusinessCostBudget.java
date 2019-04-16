package cn.richinfo.richadmin.Entity.finance;

import lombok.Data;

/**
 * Created by LiuRuibing on 2019/4/9 0009.
 */
@Data
public class BusinessCostBudget {
    private Integer businessCostId;

    private String signingSubject;

    private String supplierName;

    private String costType;

    private String costItem;

    private String projectName;

    private String productType;

    private String expectProjectCycle;

    private String signingBusinessUnit;

    private String costFinancialIncome;

    private String team;

    private Double businessCostTotal;

    private String remark;

    private String obligate;

    private String obligate2;

    private String date;

    private String budgetName;
}
