package cn.richinfo.richadmin.Entity.finance;

import lombok.Data;

/**
 * Created by LiuRuibing on 2019/4/9 0009.
 */
@Data
public class BusinessIncomeValue {

    private Integer businessIncomeId;

    private String signingBusinessUnit;

    private String date;

    private String month;

    private Double monthValue;

    private String year;

    private  String startYear;

    private  Integer startMonth;

    private  String endYear;

    private  Integer endMonth;
}
