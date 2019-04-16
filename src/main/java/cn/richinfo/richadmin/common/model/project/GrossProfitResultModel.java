package cn.richinfo.richadmin.common.model.project;

import lombok.Data;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Data
public class GrossProfitResultModel {

    //立项目标毛利率
    private double targetProfitMargin;
    //立项毛利
    private double maori;
    //滚动毛利
    private double scrollingMaori;

    List<GrossProfitModel> list;
}
