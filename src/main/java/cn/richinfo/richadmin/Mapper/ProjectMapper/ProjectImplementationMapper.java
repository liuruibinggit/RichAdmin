package cn.richinfo.richadmin.Mapper.ProjectMapper;

import cn.richinfo.richadmin.Entity.project.ProjectImplementation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Mapper
public interface ProjectImplementationMapper {

    void deleteByPrimaryKey(String projectNum);

    void insertSelective(ProjectImplementation projectImplementation);

    ProjectImplementation selectByPrimaryKey(String projectNum);

    void updateByPrimaryKeySelective(ProjectImplementation projectImplementation);

    int countNewApp(@Param("startTime") String startTime, @Param("endTime")String endTime,
                    @Param("dept")String dept, @Param("projectNums")String[] projectNums);
}
