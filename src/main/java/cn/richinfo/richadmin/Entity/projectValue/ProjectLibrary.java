package cn.richinfo.richadmin.Entity.projectValue;

import lombok.Data;

import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/10 0010.
 */
@Data
public class ProjectLibrary {

    private Integer pId;

    private String projectNum;

    private String projectName;

    private String dept;

    private Double contractCost;

    private String outCost;

    private String outputValue;

    private String projectType;

    private Date startTime;

    private Date stopTime;

    private Date createTime;

    private String cycle;

    private String monthCycle;

    private Integer isFrame;

    private Integer isNext;

    private String turnover;

    private String workMonth;

    private Integer workCount;

    //搜索参数
    private Date minStartTime;
    private Date maxStartTime;

    private Date minStopTime;
    private Date maxStopTime;

    private String minContractAmount;
    private String maxContractAmount;

    private String minOutCost;
    private String maxOutCost;

    private String minOutputValue;
    private String maxOutputValue;
}
