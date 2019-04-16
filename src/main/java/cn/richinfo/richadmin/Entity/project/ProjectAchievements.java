package cn.richinfo.richadmin.Entity.project;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Data
public class ProjectAchievements implements Serializable{

    private String projectNum;
    private String projectName;
    private String projectType;
    private Integer projectState;
    private String projectManager;
    private String dept;
    private int speed1;
    private Double speed2;
    private Double manhour1;
    private Double manhour2;
    private int payment1;
    private Double payment2;
    private Double cost1;
    private Double cost2;
    private Double grossProfit1;
    private Double grossProfit2;
    private Double settlementr1;
    private Double settlementr2;
    private String selectType;
    private String room;   //室
    private String isDept;  //部门
    private Double score;  //项目得分
    private Double rollingMaoriScore;//滚动毛利得分
    private Double acceptanceProgressScore;//验收进度得分
    private Double returnProgressScore; //回款进度得分
    private Double settlementRateScore;//结算率得分

}
