package cn.richinfo.richadmin.Mapper.ProjectValue;

import cn.richinfo.richadmin.Entity.projectValue.ProjectLibrary;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/11 0011.
 */
@Mapper
public interface ProjectLibraryMapper {

    /**
     * 插入project_library数据到数据库
     * @param projectLibrary 要插入的数据集合
     */
    void insertProjectLibrary(ProjectLibrary projectLibrary);

    /**
     * 获取实施项目/预立项项目的项目信息库信息
     */
    List<ProjectLibrary> getApprovalLibraryInfo();

    /**
     * 获取企业业务单元中2人月以下项目信息库信息
     */
    List<ProjectLibrary> getEnterpriseDeptSmallerTwoMonthLibraryInfo();
    /**
     * 投标管理中取得“已中标”的且为“过路单”的项目
     */
    List<ProjectLibrary> getBidProjectLibraryInfo();

    /**
     * 根据项目编号进行更新操作
     */
    int updateByProjectNum(ProjectLibrary record);

    /**
     * 根据项目编号获取项目信息库信息
     */
    ProjectLibrary selectProjectLibraryByProjectNum(String projectNum);

    ProjectLibrary selectProjectById(int pid);
}
