package cn.richinfo.richadmin.Mapper.ProjectMapper;
import cn.richinfo.richadmin.Entity.project.ProjectStatusSecore;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Mapper
public interface ProjectStatusSecoreMapper {

    //根据年份和月份删除项目诊断ProjectStatusSecore数据
    void deleteProjectStatusSecoreByYearAndMonth(String yearAndMonth);

    //插入ProjectStatusSecore
    void insertSelective(ProjectStatusSecore projectStatusSecore);

    ProjectStatusSecore getAvgSecore(ProjectStatusSecore projectStatusSecore);
}
