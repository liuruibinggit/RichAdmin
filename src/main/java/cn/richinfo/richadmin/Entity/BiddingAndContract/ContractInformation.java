package cn.richinfo.richadmin.Entity.BiddingAndContract;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Data
public class ContractInformation {
    private Integer contactId;

    private String projectNum;

    private String projectName;

    private String contractNum;

    private String contractName;

    private String customerName;

    private String product;

    private String industry;

    private String isFrame;

    private Double contractAmount;

    private String noContractAmount;

    private String payType;

    private Double estimateContractAmount;

    private String maintenancePeriod;

    private String contractLegalEntity;

    private Integer isSigning;

    private Date contractSigningTime;

    private Date contractStartTime;

    private Date contractEndTime;

    private String contractEndDay;

    private String filingTime;

    private String filingState;

    private String dept;

    private String salesDept;

    private String salesman;

    private String remark;

    private Date createTime;

    private Date modifiyTime;

    private String contractSigningTimeStart; //合同签订日期时间（起始范围时间）

    private String contractSigningTimeEnd;  //合同签订日期时间（结束范围时间）

    private Double signingAmont;

    private Double netBusinessIncome;

    private List<ContractPlan> list;

    private Integer contractCycle;

    private float minContractAmount;//合同金额（起始范围金额）
    private float maxContractAmount;//合同金额（结束范围金额）

    private Date minContractStartDate;
    private Date maxContractStartDate;

    private Date minContractEndDate;
    private Date maxContractEndDate;

    //新加    商务接口人   20190315
    private String businessInterfacePerson;
}
