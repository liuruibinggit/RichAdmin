package cn.richinfo.richadmin.Entity.project;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Data
public class ProjectImplementation implements Serializable{

    private Integer pId;

    private String projectNum;

    private Integer projectState;

    private Integer isNext;

    private String nextProjectName;

    private Integer implementationStat;

    private String projectProgress;

    private Double preContractMoney;

    private String remark;

    private String signingProgress;

    private Double wyWork;

    private Double wyAssessment;

    private Double wyMoney;

    private Double wySettlementAmount;

    private Date createTime;

    private Date servicestopTime;

    private String isDept;

    private String room;

    private Double bonusBalance;

    private String dept;

    private Date modifyTime;
}
