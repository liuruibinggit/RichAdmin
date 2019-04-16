package cn.richinfo.richadmin.Mapper.UnitCost;

import cn.richinfo.richadmin.Entity.unitcost.UnitCostInfo;
import cn.richinfo.richadmin.Entity.unitcost.UnitCostTotal;
import cn.richinfo.richadmin.common.model.unitCost.UnitCostInfoModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Mapper
public interface UnitCostInfoMapper {
    public void addCostInfo(UnitCostInfo unitCostInfo);

    public Integer getMaxOrderNo();

    public UnitCostInfo queryByUserId(UnitCostInfo unitCostInfo);

    public List<UnitCostInfo> getCostInfoList(@Param("businessLineId") String businessLineId, @Param("month")String month, @Param("businessLine")String businessLine);

    public List<UnitCostInfo> getCostInfoList(RowBounds row, String businessLineId);

    public void delCostInfo(UnitCostInfo unitCostInfo);

    public void editCostInfo(UnitCostInfo unitCostInfo);

    void deleteAll(String stage);

    List<UnitCostInfo> selectByUserName(String projectName);

    public void insertSelective(UnitCostInfo unitCostInfo);

    public UnitCostTotal sum(UnitCostInfoModel costModel);

    public Integer getPopleNum(UnitCostInfoModel costModel);

    public UnitCostInfo queryByUserIdAndMonth(@Param("userId") String userId,@Param("month") String month);

    public void updateCostInfo(UnitCostInfo unitCostInfo);
}
