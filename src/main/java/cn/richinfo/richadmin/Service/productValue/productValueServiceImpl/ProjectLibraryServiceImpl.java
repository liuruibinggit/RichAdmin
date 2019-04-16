package cn.richinfo.richadmin.Service.productValue.productValueServiceImpl;

import cn.richinfo.richadmin.Entity.projectValue.ProjectLibrary;
import cn.richinfo.richadmin.Mapper.ProjectValue.ProjectLibraryMapper;
import cn.richinfo.richadmin.Service.productValue.ProjectLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/10 0010.
 */
@Service
public class ProjectLibraryServiceImpl implements ProjectLibraryService{

    @Autowired
    private ProjectLibraryMapper projectLibraryMapper;

    /**
     * 插入项目信息库信息到数据库
     */
    @Override
    public void addProjectLibraryInfo() {
        List<ProjectLibrary> projectLibraryList=new ArrayList<>();
        String projectNum="";
        ProjectLibrary projectLibraryTemp=new ProjectLibrary();
        //获取预立项、实施项目的集合
        List<ProjectLibrary> approvalProjectLibraryList=projectLibraryMapper.getApprovalLibraryInfo();
        projectLibraryList.addAll(approvalProjectLibraryList);
        //获取企业业务单元中2人月以下项目的信息集合
        List<ProjectLibrary> enterpriseDeptSmallerTwoMonthLibraryInfoList = projectLibraryMapper.getEnterpriseDeptSmallerTwoMonthLibraryInfo();
        projectLibraryList.addAll(enterpriseDeptSmallerTwoMonthLibraryInfoList);
        //投标管理中取得“已中标”的且为“过路单”的项目
        List<ProjectLibrary> bidProjectLibraryInfo = projectLibraryMapper.getBidProjectLibraryInfo();
        projectLibraryList.addAll(bidProjectLibraryInfo);
        //将新计算得到的集合进行遍历对比
        for (ProjectLibrary projectLibrary:projectLibraryList){
            projectNum=projectLibrary.getProjectNum();
            projectLibraryTemp = projectLibraryMapper.selectProjectLibraryByProjectNum(projectNum);
            if(projectLibraryTemp==null){//没有该项目的项目基础信息库
                projectLibraryMapper.insertProjectLibrary(projectLibrary);
            }else{//原先就有该项目的基础信息库
                projectLibraryMapper.updateByProjectNum(projectLibrary);
            }
        }
    }
}
