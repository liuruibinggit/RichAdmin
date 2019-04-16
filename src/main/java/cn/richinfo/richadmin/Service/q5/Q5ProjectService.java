package cn.richinfo.richadmin.Service.q5;

import cn.richinfo.richadmin.Entity.project.ApprovalInformation;
import cn.richinfo.richadmin.Entity.project.Q5Project;

import java.io.IOException;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/8 0008.
 */
public interface Q5ProjectService {

    void addQ5ProjectInit(String year,String month);

    List<ApprovalInformation> selectAllProjectNum();

    void insertInfo(String year, String month, ApprovalInformation appro, Q5Project pro);

    /**
     * 获取所有的项目列表
     */
    public List<Q5Project> list();

}
