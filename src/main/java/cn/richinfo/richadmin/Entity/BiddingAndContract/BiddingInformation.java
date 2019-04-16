package cn.richinfo.richadmin.Entity.BiddingAndContract;

import lombok.Data;

import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Data
public class BiddingInformation {

    private Integer bidId;

    private String dept;

    private String customerNum;

    private String projectName;

    private String customerName;

    private Double finalOffer;

    private String taxRate;

    private String averagePrive;

    private String functionUnitPrice;

    private Double budgetedCost;

    private Double budgetedTotalHours;

    private Double unitPoolCoefficient;

    private String exBalance;

    private String bfBalance;

    private Date tenderTime;

    private String isBid;

    private String salesmanDept;

    private String salesman;

    private String businessInterfacePeople;

    private Double outsourcingCost;

    private Double purchaseCost;

    private Double otherCost;

    private String remark;

    private String addOrExtend;

    private String isOutsourcing;

    private Date creatTime;

    private Date modifyTime;

    private Date bidTime;

    private Integer projectTerm;

    private Date endTime;

    private Date startTime;

    private Double signingAmont;

    private Double netBusinessIncome;

    private String projectNum;

    private String businessNum;

    private String isSearchBid;

    private String purchaseType;

    private String isFail;
}
