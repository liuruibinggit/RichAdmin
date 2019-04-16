package cn.richinfo.richadmin.Entity.BiddingAndContract;

import lombok.Data;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Data
public class Check {
    private int checkId;
    private String businessLine;
    private String projectName;
    private String projectNum;
    private String projectManager;
    private String projectType;
    private String projectStatus;
    private double contractAmount;
    private double targetProfitMargin;
    private double targetProfit;
    private double overTarget;
    private double overTargetScore;
    private double planActualScore;
    private double planCostScore;
    private double customerScore;
    private double topScore;
    private double actualScore;
    private String percentScore;
    private double workingHoursAll;
    private double workingHoursManager;

}
