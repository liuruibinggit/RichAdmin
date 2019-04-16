package cn.richinfo.richadmin.Entity.project;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 *
 */
@Data
public class ProjectStatusSecore implements Serializable{

    private Integer id;
    private String projectNum;
    private String projectName;
    private String projectType;
    private String projectStatus;
    private String projectManager;
    private String projectProducts;
    private double contractAmount;
    private String targetProfitMargin;
    private String dept;
    private double costCount;
    private double hoursCount;
    private double payCount;
    private double settlementr;
    private double grossprofit;
    private Integer costType;
    private Integer hoursType;
    private Integer speedType;
    private Integer payType;
    private Integer settlementrType;
    private Integer grossprofitType;
    private double costSecory;
    private double hoursSecory;
    private double speedSecory;
    private double paySecory;
    private double settlementrSecory;
    private double grossprofitSecory;
    private double countSecory;
    private double minCountSecory;
    private double maxCountSecory;
    private int month;
    private Date createTime;
    private Date modifiyTime;
    private String isDept;
    private String room;

    private Integer costLevel;
    private Integer speedLevel;
    private Integer payLevel;
    private Integer settlementrLevel;
    private Integer grossprofitLevel;
    private Integer hoursLevel;

    private Double costValue;
    private Double speedValue;
    private Double manhourValue;
    private Double paymentValue;
    private Double grossprofitValue;
    private Double settlementrValue;

    private String valueField;

    private double manHours;

    private String desc;
    private String sortField;

    private int pageSize;
    private int pageNo;

    private int start;
    private int end;

    private int speedInitialTest;//里程碑是否经过初验
    private int paymentInitialTest;//里程碑是否经过 （初验 终验）

    private int accountingRate; //结算率标记

    private int grossfitRateFlag; //滚动毛利率标记

    private int top5;

    private Integer secoreLevel;

    private String[] depts; //业务单元集合

    private String[] isDepts; //部门集合

    private String[] projectNums;

}
