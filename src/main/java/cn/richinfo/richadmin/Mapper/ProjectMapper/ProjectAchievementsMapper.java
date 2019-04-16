package cn.richinfo.richadmin.Mapper.ProjectMapper;

import cn.richinfo.richadmin.Entity.project.ProjectAchievements;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Mapper
public interface ProjectAchievementsMapper {

    int deleteByPrimaryKey(String projectNum);
    int insert(ProjectAchievements record);
    int insertSelective(ProjectAchievements record);
    ProjectAchievements selectByPrimaryKey(String projectNum);
    int updateByPrimaryKeySelective(ProjectAchievements record);
    int updateByPrimaryKey(ProjectAchievements record);
    void delete();
    List<ProjectAchievements> selectProject(ProjectAchievements projectAchievements, RowBounds row);
    int selectCountProject(ProjectAchievements projectAchievements);
    List<ProjectAchievements> selectProject(ProjectAchievements projectAchievements);
}
