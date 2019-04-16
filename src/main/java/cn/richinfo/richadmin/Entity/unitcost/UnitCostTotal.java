package cn.richinfo.richadmin.Entity.unitcost;

import lombok.Data;

import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Data
public class UnitCostTotal {
    private Integer id;
    private String businessLineId;
    private String businessLine;
    private Integer employeeNum;
    private Double manpowerCostTotal;
    private Double directCostTotal;
    private Date editTime;
}
