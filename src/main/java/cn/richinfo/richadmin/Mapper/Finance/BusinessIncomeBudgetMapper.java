package cn.richinfo.richadmin.Mapper.Finance;

import cn.richinfo.richadmin.Entity.finance.BusinessIncome;
import cn.richinfo.richadmin.Entity.finance.BusinessIncomeBudget;
import cn.richinfo.richadmin.Entity.finance.BusinessIncomeValue;
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
public interface BusinessIncomeBudgetMapper {

    void insertSelective(BusinessIncomeBudget businessIncomeBudget);

    BusinessIncome selectByPrimaryKey(Integer businessIncomeId);

    List<BusinessIncomeBudget> selectBusinessIncomeBudget(
            BusinessIncomeModel businessIncomeModel, RowBounds rowBounds);

    List<BusinessIncomeBudget> selectBusinessIncomeBudget(
            BusinessIncomeModel businessIncomeModel);

    List<BusinessIncomeBudget> selectBusinessIncomeBudget();

    void deleteBusinessIncomeBudget(BusinessIncomeModel businessModel);

    //根据年份获取月份集合
    List<String> getMonthList(@Param("year") String  year);

    //获取年份列表
    List<String> getYearList();

    //获取当前最新的日期
    String maxDate();

    List<String> getProjectNameList(BusinessIncomeValue incomeValue); //获取所有的项目名称

    BusinessIncomeMarginDto getCurMonth(BusinessIncomeMarginDto incomeMarginDto);

    BusinessIncomeBudget selectByProjectName(@Param("projectName")String projectName,@Param("date")String date);

    void updateBudgetName(BusinessIncomeBudget businessIncomeBudget);

    BusinessIncomeMarginDto sum(BusinessIncomeMarginDto incomeMarginDto);

    List<String> getMonthByProject(BusinessIncomeMarginDto incomeMarginDto);

    Double getSum(BusinessIncomeModel businessIncomeModel); //获取合计值

}
