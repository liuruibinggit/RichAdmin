package cn.richinfo.richadmin.common.vo.finance;

import lombok.Data;

/**
 * Created by LiuRuibing on 2019/4/9 0009.
 */
@Data
public class BusinessTotalVo {
    private String orderType;

    private Double actualAmount;

    private Double kpiAmount;

    private Double marginAmount;
}
