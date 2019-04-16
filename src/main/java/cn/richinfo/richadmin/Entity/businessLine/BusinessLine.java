package cn.richinfo.richadmin.Entity.businessLine;

import lombok.Data;

import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/12 0012.
 */
@Data
public class BusinessLine {
    private String businessLineId;
    private String businessLine;
    private String showState;
    private Date createTime;
    private Integer orderNo;
}
