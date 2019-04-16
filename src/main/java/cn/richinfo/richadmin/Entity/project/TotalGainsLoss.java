package cn.richinfo.richadmin.Entity.project;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Data
public class TotalGainsLoss implements Serializable{

    private String projectNum;
    private String businessLineId;
    private String businessLine;
    private String projectName;
    private String productType;
    private Double accountIncome;
    private Double cost;
    private Double mainBusinessCost;
    private Double taxAddition;
    private Double operatingExpense;
    private Double salaryAddition;
    private Double performanceBonus;
    private Double socialInsurance;
    private Double outSourcingDevelopmentFee;
    private Double depreciationCost;
    private Double rentalExpense;
    private Double welfareExpense;
    private Double officeExpense;
    private Double travelExpense;
    private Double serveExpense;
    private Double cityTransportationExpense;
    private Double carExpense;
    private Double lowValueConsumableAmortization;
    private Double mailCharges;//邮电费
    private Double trainingExpense;//培训费
    private Double meetingAffairExpense;//会务费
    private Double consultingExpense;//咨询费
    private Double advertisingExpense;
    private Double recruitingExpense;
    private Double intangibleAmortization;
    private Double otherTaxExpense;
    private Double testExpense;
    private Double otherExpense;
    private Double financialExpense; //财务费
    private Double operatingProftProject;
    private Double unitInternalSettlementIncome;
    private Double unitInternalSettlementPlusCharge;
    private Double preTaxProfits;//税前利润
    private Double inPeopleMonth;//投入人月
    private Double perCapitaCost;//人均费用
    private Double profitRate;//利润率
    private Double projectApportionmentCostDeadline;//截止某月项目公摊成本
    private Double projectApportionmentCost1;
    private Double projectApportionmentCost2;
    private Double projectApportionmentCost3;
    private Double projectApportionmentCost4;
    private Double projectApportionmentCost5;
    private Double projectApportionmentCost6;//项目公摊成本6
    private Double projectApportionmentCost7;//项目公摊成本
    private Double projectApportionmentCost8;//项目公摊成本
    private Double projectApportionmentCost9;//项目公摊成本
    private Double projectApportionmentCost10;//项目公摊成本
    private Double projectApportionmentCost11;//项目公摊成本
    private Double projectApportionmentCost12;//项目公摊成本
    private Double projectApportionmentCost13;//项目公摊成本
    private Double projectApportionmentCost14;//项目公摊成本
    private Double projectApportionmentCost15;//项目公摊成本
    private Double projectApportionmentCost16;//项目公摊成本
    private Double projectApportionmentCost17;//项目公摊成本
    private Double projectApportionmentCostTimeSlot;//某段时间项目公摊后成本
    private Double addUpApportionmentMoney;//累计公摊金额
    private Double projectActualBouns15;//项目15年实发奖金
    private Double projectActualBouns16; //项目16年实发奖金
    private Double projectProvisionBouns15;//项目15年计提奖金
    private Double projectProvisionBouns16; //项目16年计提奖金
    private Double projectActualBouns17; //项目17年实发奖金
    private Double projectProvisionBouns17;//项目17年计提奖金
    private Double projectProvisionBouns18;//项目18年计提奖金
    private Double projectActualBouns18; //项目17年实发奖金
    private String date;//日期
    private Date createTime;
}
