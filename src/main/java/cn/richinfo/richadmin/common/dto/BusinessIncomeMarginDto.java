package cn.richinfo.richadmin.common.dto;

import lombok.Data;

/**
 * Created by LiuRuibing on 2019/4/9 0009.
 */
@Data
public class BusinessIncomeMarginDto {
    private String  customerName;

    private String businessType;

    private String projectNum;

    private String signingBusinessUnit;

    private String team;

    private String projectName;

    private Double monthValue;

    private String month;

    private String date;

    private String year;

    private Integer startMonth;

    private Integer endMonth;
}
