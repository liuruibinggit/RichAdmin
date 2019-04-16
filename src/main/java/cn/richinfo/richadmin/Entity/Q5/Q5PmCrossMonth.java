package cn.richinfo.richadmin.Entity.Q5;

import lombok.Data;

import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Data
public class Q5PmCrossMonth {

    private int id;
    private String projectNum;
    private String pmNum;
    private String projectManHour;
    private String crossMonth;
    private String totalManHour;
    private String pmName;
    private Date createTime;
    private String workTime;
}
