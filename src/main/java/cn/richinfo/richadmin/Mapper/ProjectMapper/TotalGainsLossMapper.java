package cn.richinfo.richadmin.Mapper.ProjectMapper;

import cn.richinfo.richadmin.Entity.project.TotalGainsLoss;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Mapper
public interface TotalGainsLossMapper {

    void insertByTotal(TotalGainsLoss totalGainsLoss);

    void updateByTotal(TotalGainsLoss totalGainsLoss);

    void deleteByTotal(TotalGainsLoss totalGainsLoss);

    List<TotalGainsLoss> getList(TotalGainsLoss totalGainsLoss);

    List<TotalGainsLoss> getList(TotalGainsLoss totalGainsLoss,RowBounds row);

    List<String> getMonthList(String date);

    List<String> getYearList();

    String getMaxDate();

    TotalGainsLoss selectBySum(@Param("projectNum") String projectNum, @Param("date") String date);

    double selectPerCapitaCost(@Param("projectNum")String projectNum,@Param("date")String date);

    Double getProjectApportionmentCostTimeSlot(String projectNum);
}
