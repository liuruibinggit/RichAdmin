package cn.richinfo.richadmin.Mapper.ProjectMapper;

import cn.richinfo.richadmin.Entity.project.PlanCost;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Mapper
public interface PlanCostMapper {

    int deleteByPrimaryKey(String projectNum);

    int insert(PlanCost record);

    int insertSelective(PlanCost record);

    List<PlanCost> selectByPrimaryKey(String projectNum);

    int updateByPrimaryKeySelective(PlanCost record);

    int updateByPrimaryKey(PlanCost record);

    List<PlanCost> getProjectByName(String dept);

    List<PlanCost> selectByPrimaryKeyAndPlanTime(HashMap<String, String> mapPlanCost);

    PlanCost selectByProjectNumAndSort(PlanCost planCost);
}
