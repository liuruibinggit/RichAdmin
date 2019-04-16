package cn.richinfo.richadmin.Entity.check;

import lombok.Data;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Data
public class ProjectCheck {
    private Integer projectCheckId;

    private String businessLine;

    private String projectName;

    private String projectNum;

    private String projectManager;

    private String projectType;

    private Integer projectStatus;

    private Double initialCostHours;

    private Double contactAmount;

    private Double singleScore;

    private Double overTargetScore;

    private Double planScore;

    private Double customerScore;

    private Double timeRatio;

    private Double projectHours;

    private Double allProjectHours;

    private Double difficultyRatio;

    private Double projectTypeScore;

    private Double totalHumanMonth;

    private Double contactAmountScore;

    private Double crossMonth;

    private Double productScore;

    private Double projectCheckScore;

    private String year;

    private Double costScore;

    private Double cooperateScore;

    private Double unitCheckScore;

    private Double manageScore;

}
