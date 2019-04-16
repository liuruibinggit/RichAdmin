package cn.richinfo.richadmin.Mapper.Check;

import cn.richinfo.richadmin.Entity.check.ManagerCheck;
import cn.richinfo.richadmin.Entity.check.ProjectCheck;
import cn.richinfo.richadmin.common.model.check.ManagerCheckModel;
import cn.richinfo.richadmin.common.model.check.ProjectCheckModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Mapper
public interface ProjectCheckMapper {
    Double selectProductScore(String projectNum);

    ProjectCheck selectByProjectNum(String projectNum);

    void insertSelective(ProjectCheck projectCheck);

    void deleteByPrimaryKey(Integer id);

    List<ProjectCheck> getProjectCheckList(ProjectCheckModel projectCheckModel, RowBounds rowBounds);

    List<ProjectCheck> getProjectCheckList(ProjectCheckModel projectCheckModel);

    void updateByPrimaryKeySelective(ProjectCheckModel projectCheckModel);

    List<ProjectCheck> selectByProjectManagerAndYear(Map<String, String> map);

}
