package cn.richinfo.richadmin.Entity.check;

import lombok.Data;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Data
public class ManagerCheck {
    private Integer managerCheckId;

    private String projectManager;

    private String businessLine;

    private String dept;

    private String managerNum;

    private String position;

    private Double managerCheckScore;

    private Double rank;

    private String rankLevel;

    private Double basicBonus;

    private Double bonusTimes;

    private Double salary;

    private Double managerHours;

    private Double actualManagerHours;

    private Double decemberBonus;

    private String year;

}
