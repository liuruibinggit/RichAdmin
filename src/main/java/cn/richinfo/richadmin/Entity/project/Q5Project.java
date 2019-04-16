package cn.richinfo.richadmin.Entity.project;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Data
public class Q5Project implements Serializable {

    private Integer qId;

    private String projectNum;

    private String managerName;

    private String managerNum;

    private String workingHoursCount;

    private String workTime;

    private Integer workMonth;

    private Date createTime;

    private String planMonth;

    private String planCountMonth;

    private int peopleCount;
}
