package cn.richinfo.richadmin.Entity.project;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Data
public class ShareCoefficient implements Serializable {

    private Integer id;
    private String businessLineId;
    private String businessLine;
    private String date; // 每个部门的单元公摊系数的时间
    private Double value; // 单元公摊系数的值
    private Date createTime;
    private String createBy;
    private Integer dateId;

    private String startDate; // 开始的查询时间
    private String endDate; // 结束的查询时间
}
