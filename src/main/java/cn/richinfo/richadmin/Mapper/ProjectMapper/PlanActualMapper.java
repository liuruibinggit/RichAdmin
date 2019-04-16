package cn.richinfo.richadmin.Mapper.ProjectMapper;

import cn.richinfo.richadmin.Entity.project.PlanActual;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Mapper
public interface PlanActualMapper {

    int deleteByPrimaryKey(String projectNum);

    int insert(PlanActual record);

    int insertSelective(PlanActual record);

    List<PlanActual> selectByPrimaryKey(String projectNum);

    int updateByPrimaryKeySelective(PlanActual record);

    int updateByPrimaryKey(PlanActual record);

    List<PlanActual> selectByPrimaryKeyAndPlanTime(HashMap<String, String> mapPlanActual);

    PlanActual selectByProjectNumAndSort(PlanActual planActual);

    //获取最大排序 sort值
    int getMaxSort (String projectNum);
}
