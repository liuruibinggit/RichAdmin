package cn.richinfo.richadmin.common.model.finance;

import lombok.Data;

/**
 * Created by LiuRuibing on 2019/4/9 0009.
 */
@Data
public class BusinessIncomeModel {

    private Integer pageNo;
    private Integer pageSize;
    private String financialIncome; //会计收入归属业务单元
    private String signingBusinessUnit; //签单业务单元
    private String costFinancialIncome; //成本归属单元
    private String  date;
    private String month;
    private Integer startMonth;
    private Integer endMonth;
    private String year;
    private Integer isExports;

    //高级搜索字段
    private String signingSubject;//签约主体
    private String customerName ; //客户名称
    private String customerType; //客户类型
    private String businessType; //业务类型
    private String projectName;  //项目名称
    private String projectType;  //项目类型
    private String productType;  //产品类型
    private String team;    //责任团队

    private String supplierName; //供应商名称
    private String costType; //成本类型
    private String costItem; //成本项目

    private String department;//部门
    private String feeAccount;//费用科目
    private String feeItem;  //费用项目
}
