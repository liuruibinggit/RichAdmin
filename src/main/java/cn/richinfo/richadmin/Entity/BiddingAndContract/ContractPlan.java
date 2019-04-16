package cn.richinfo.richadmin.Entity.BiddingAndContract;

import lombok.Data;

import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Data
public class ContractPlan {
    private Integer cpId;

    private Integer contractId;

    private String planIncome;

    private Date startTime;

    private Date stopTime;

    private Integer cycle;

    private String actual;

    private Date createTime;

    private Integer sort;
}
