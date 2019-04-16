package cn.richinfo.richadmin.Entity.finance;

import lombok.Data;

/**
 * Created by LiuRuibing on 2019/4/9 0009.
 */
@Data
public class BusinessIncomeBudget {

    private Integer businessIncomeId;

    private String signingSubject;

    private String customerName;

    private String customerType;

    private String businessType;

    private String projectName;

    private String projectType;

    private String productType;

    private String expectProjectCycle;

    private String signingBusinessUnit;

    private String financialIncome;

    private String team;

    private String budgetName;

    private Double businessIncomeTotal;

    private String remark;

    private String date;
}
