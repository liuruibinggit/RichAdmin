package cn.richinfo.richadmin.Mapper.Finance;

import cn.richinfo.richadmin.Entity.finance.BusinessCostBudget;
import cn.richinfo.richadmin.common.dto.BusinessIncomeMarginDto;
import cn.richinfo.richadmin.common.model.finance.BusinessIncomeModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/9 0009.
 */
@Mapper
public interface BusinessCostBudgetMapper {

    Integer insertSelective(BusinessCostBudget businessCostBudget);

    List<BusinessCostBudget> selectBusinessCostBudget(
            BusinessIncomeModel businessIncomeModel, RowBounds rowBounds);

    List<BusinessCostBudget> selectBusinessCostBudget(
            BusinessIncomeModel businessIncomeModel);

    List<BusinessCostBudget> selectBusinessCostBudget();

    void deleteBusinessCostBudget(BusinessIncomeModel businessIncomeModel);

    //根据年份获取月份集合
    List<String> getMonthList(@Param("year") String  year);

    //获取年份列表
    List<String> getYearList();

    //获取当前最新的日期
    String maxDate();

    Double getCurMonth(BusinessIncomeMarginDto incomeMarginDto);

    List<BusinessCostBudget> selectByProjectName(@Param("projectName")String projectName,@Param("date")String date);

    void updateBudgetName(BusinessCostBudget businessIncomeBudget);

    Double sum(BusinessIncomeMarginDto incomeMarginDto);

    Double getSum(BusinessIncomeModel businessIncomeModel); //获取合计值

    List<String> getMonthByProject(BusinessIncomeMarginDto incomeMarginDto); //获取成本不为0的月份列表

}
