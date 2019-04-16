package cn.richinfo.richadmin.Entity.project;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Data
public class ProjectCost implements Serializable {

    private Integer cId;

    private Integer sort;

    private String projectNum;

    private String planCost;

    private String planYear;

    private String laborCost;

    private String expenseReimbursement;

    private String cost1;

    private String cost2;

    private Date createTime;

    private String dept;

    private String planMonth;

    private String historyCost;

    private String countCost;

    private String unitApportionmentCost;

    private String companyApportionmentCost;

    private Double cost;
}
