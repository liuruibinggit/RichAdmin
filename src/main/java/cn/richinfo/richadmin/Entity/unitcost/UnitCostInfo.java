package cn.richinfo.richadmin.Entity.unitcost;

import lombok.Data;

import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Data
public class UnitCostInfo {

    private Integer id;
    private String userId; // 员工编号
    private String userName; // 员工姓名
    private String position; // 职位
    private String positionType; // 职位类别
    private String deptId;
    private String dept;
    private String addr;
    private String businessLine; // 业务线名称
    private String businessLineId; // 业务线id
    private Double directCosts; // 直接成本
    private Double manpowerCost;// 人力成本
    private Integer employeeSum; // 售前和销售总人数
    private Double manpowerDirectCost; // 人力成本+直接成本
    private Integer orderNo;
    private Date editTime;
    private String month;

    //新添加字段   入职时间  离职时间
    private String hireDate;
    private String resignDate;

    private String room;  //室
}
