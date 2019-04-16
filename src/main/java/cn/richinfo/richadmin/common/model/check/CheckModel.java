package cn.richinfo.richadmin.common.model.check;

import lombok.Data;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Data
public class CheckModel {
    private int pageSize;
    private int pageNo;
    private String businessLine;
    private String projectNum;
    private String customerScore;
}
