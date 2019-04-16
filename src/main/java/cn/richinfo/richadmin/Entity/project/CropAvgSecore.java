package cn.richinfo.richadmin.Entity.project;

import lombok.Data;

import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/12 0012.
 */
@Data
public class CropAvgSecore {
    private Integer id;
    private Integer month;


    private Double corpSercore;
    private Double yw1Sercore;
    private Double yw2Sercore;
    private Double qySercore;
    private Double nfSercore;
    private Double bfSercore;
    private Double szSercore;

    private Double corpSpeedSercore;
    private Double qySpeedSercore;
    private Double nfSpeedSercore;
    private Double bfSpeedSercore;
    private Double szSpeedSercore;
    private Double yw1SpeedSercore;
    private Double yw2SpeedSercore;

    private Double corpMaoriSercore;
    private Double yw1MaoriSercore;
    private Double yw2MaoriSercore;
    private Double nfMaoriSercore;
    private Double bfMaoriSercore;
    private Double qyMaoriSercore;
    private Double szMaoriSercore;

    private Double corpSettlementSecore;
    private Double yw1SettlementSecore;
    private Double yw2SettlementSecore;
    private Double nfSettlementSecore;
    private Double bfSettlementSecore;
    private Double qySettlementSecore;
    private Double szSettlementSecore;

    private Double corpReturnSecore;
    private Double yw1ReturnSecore;
    private Double yw2ReturnSecore;
    private Double nfReturnSecore;
    private Double bfReturnSecore;
    private Double qyReturnSecore;
    private Double szReturnSecore;

    private Date createTime;
    private Date modifyTime;
}
