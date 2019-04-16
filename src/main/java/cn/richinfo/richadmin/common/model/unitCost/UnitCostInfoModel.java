package cn.richinfo.richadmin.common.model.unitCost;

import lombok.Data;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Data
public class UnitCostInfoModel {

    private String userId; // 员工编号
    private String positionType; // 职位类别
    private String deptId;
    private String dept;
    private String businessLine; // 业务线名称
    private String businessLineId; // 业务线id
    private Double directCosts; // 直接成本
    private Double manpowerCost;// 人力成本
    private String month;

    private List<UnitCostInfoModel> modelList;

    private Integer pageSize; // 每页大小
    private Integer pageNo; // 页数

    private String startMonth;

    private String endMonth;
}
