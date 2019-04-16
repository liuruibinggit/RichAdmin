package cn.richinfo.richadmin.Mapper.ProjectMapper;

import cn.richinfo.richadmin.Entity.project.ProjectCost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Mapper
public interface ProjectCostMapper {

    int deleteByPrimaryKey(String projectNum);

    int insert(ProjectCost record);

    int insertSelective(ProjectCost record);

    List<ProjectCost> selectByPrimaryKey(String projectNum);

    int updateByPrimaryKeySelective(ProjectCost record);

    int updateByPrimaryKey(ProjectCost record);

    int updateByCost(ProjectCost record);

    ProjectCost selectByNumYear(@Param("projectNum")String projectNum,@Param("planYear")String planYear);

    List<ProjectCost> selectForGrossprofit(String projectNum);

    List<ProjectCost> selectByNumYear2(String projectNum);

    void updateIncreateHours(ProjectCost record);

    double getPlanMonthHours(String projectNum);

    void deleteProjectCostByPlanYearAndProjectNum(ProjectCost projectCost);

    Integer getMaxSort(@Param("projectNum") String projectNum);

    Double getCountCost(@Param("projectNum")String projectNum);

    List<ProjectCost> getOctBeforeDateList();

    void updateById(ProjectCost cost);
}
