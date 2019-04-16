package cn.richinfo.richadmin.Entity.project;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Data
public class PlanCost implements Serializable {

    private Integer pId;

    private String projectNum;

    private Integer sort;

    private String stateName;

    private Date planTime;

    private String planCost;

    private Date invoiceTime;

    private Date actualTime;

    private String actualCost;

    private String settlementRate;

    private Date createTime;

    private String dept;

    private Date plantimeAfterupd;
}
