package cn.richinfo.richadmin.Entity.BiddingAndContract;

import lombok.Data;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Data
public class ExcessGrossProfit {
    private String projectName;
    private String contractAllTime;          //合同周期
    private double contractAmount;
    private double actualContractAmount;     //实际结算合同金额（税后）
    private double targetProfit;
    private double targetProfitMargin;     //立项毛利率目标
    private double overTarget;                  //结项毛利
    private double overTargetMargin;             //结项毛利率
    private double excessGrossProfit;        //超毛利金额
    private double reward;
    private int peopleNum;
    private double averageReward;
}
