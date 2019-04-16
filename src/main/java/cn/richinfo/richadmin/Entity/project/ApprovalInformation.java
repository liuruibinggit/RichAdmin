package cn.richinfo.richadmin.Entity.project;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Data
public class ApprovalInformation implements Serializable {

    private Integer projectId;

    private String projectNum;

    private String applicant;

    private String applicantDate;

    private String projectCategory;

    private String projectLevel;

    private String projectName;

    private String projectType;

    private String projectManager;

    private String pmoDept;

    private String financialIncome;

    private String salesManager;

    private String salesDept;

    private String salesIncome;

    private Date startTime;

    private Date stopTime;

    private String projectProducts;

    private Date winningDate;

    private Date signingDate;

    private Double expectCost;

    private Double initialCostHours;

    private Double projectEstimatedCost;

    private String balance;

    private String balanceRate;

    private Double contractAmount;

    private String taxRate;

    private Double unitPoolCoefficient;

    private Double costHours;

    private Double purchasingCost;

    private Double outsourcingCost;

    private Double estimatedCost;

    private Double targetProfit;

    private String targetProfitMargin;

    private Date createTime;

    private Date modifiyTime;

    private Integer isProject;

    private Double investmentMonth;

    private Double pestimatedCost;

    private String budgetShare;

    private String projectState;

    private Integer isOver;

    private String tag;

    private String isDept;

    private String room;
    private String dept;

    private Double successRate;

    private String[] projectNums;

    private Double rollCostHours;

    private String businessNum;

    private Integer state;

    private Double latestEstimatedIncome; //最新暂估收入

    private String[] financialIncomes; //业务单元列表

    private String[] isDepts; //部门列表

    private Double sumCost;//预立项总成本

    private Double sumWork;//预立项总工时

    private Double bonusBalance;//项目实际奖金与计提奖金差额

    private int isHandTrans; //是否手动转立项   0 否  1 是

    private Double thirdPartyCost;//第三方实际采购费用
}
