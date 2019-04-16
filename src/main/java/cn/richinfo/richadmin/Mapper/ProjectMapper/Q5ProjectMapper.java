package cn.richinfo.richadmin.Mapper.ProjectMapper;

import cn.richinfo.richadmin.Entity.project.ApprovalInformation;
import cn.richinfo.richadmin.Entity.project.Q5Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Mapper
public interface Q5ProjectMapper {

    int deleteByPrimaryKey(Integer qId);

    int insertSelective(Q5Project record);

    List<Q5Project> selectByPrimaryKey(String projectNum);

    int updateByPrimaryKeySelective(Q5Project record);

    List<Q5Project> queryByProjectNum(Q5Project record);

    void deleteByProjectNum(Q5Project record);

    List<Q5Project> selectList();

    Q5Project getNewDate();

    Q5Project getMonthQ5(@Param(value="projectNum") String projectNum, @Param(value="workMonth")String workMonth);

    List<Q5Project> selectByPrimaryKeyAndYear(HashMap<String, String> map);

    List<Q5Project> selectByProjectManagerAndYear(HashMap<String, String> map1);

    double getProjectCountHours(String projectNum);

    List<ApprovalInformation> selectAllProjectNum();
}
