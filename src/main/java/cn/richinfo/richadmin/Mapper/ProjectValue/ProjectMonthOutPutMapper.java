package cn.richinfo.richadmin.Mapper.ProjectValue;

import cn.richinfo.richadmin.Entity.project.ProjectMonthOutPut;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/16 0016.
 */
@Mapper
public interface ProjectMonthOutPutMapper {
    int deleteByPrimaryKey(Integer pid);

    int insert(ProjectMonthOutPut record);

    int insertSelective(ProjectMonthOutPut record);

    List<ProjectMonthOutPut> selectByPrimaryKey(Integer pid);

    Double selectMonthOutPut(ProjectMonthOutPut record);

    int updateByPrimaryKeySelective(ProjectMonthOutPut record);

    int updateByPrimaryKey(ProjectMonthOutPut record);

    List<ProjectMonthOutPut> selectMonnt(String month);

}
