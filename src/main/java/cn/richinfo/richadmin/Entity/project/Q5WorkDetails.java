package cn.richinfo.richadmin.Entity.project;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/8 0008.
 */
@Data
public class Q5WorkDetails implements Serializable{

    private Integer pId;

    private String projectNum;

    private String workTime;

    private String workName;

    private String workHours;

    private String workDept;

    private Integer workMonth;

    private Date createTime;

    private String employeeNumber;

    private String dept;
    private String room;
    private String group_in;
    private String address;
    private String emp_state;
    private String red_list;
    private String position;
    private String project_name;
    private String account_incomeuint;
    private String need_attend;

    private String hired_date;
    private String fired_Date;
}
