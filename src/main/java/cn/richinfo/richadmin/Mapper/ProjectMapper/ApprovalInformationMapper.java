package cn.richinfo.richadmin.Mapper.ProjectMapper;

import cn.richinfo.richadmin.Entity.BiddingAndContract.ExcessGrossProfit;
import cn.richinfo.richadmin.Entity.project.ApprovalInformation;
import cn.richinfo.richadmin.Entity.projectValue.ProjectLibrary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Mapper
public interface ApprovalInformationMapper {

    ApprovalInformation getApprovalByNum(String projectNum);

    List<ApprovalInformation> selectApp();

    List<ApprovalInformation> selectAllProjectNum();

    List<ApprovalInformation> selectApproval();

    void updateCostHours(ApprovalInformation approvalInformation);

    int updateByPrimaryKeySelective(ApprovalInformation record);

    ApprovalInformation selectImplementProject(@Param("projectNum")String projectNum);

    /**
     * 根据项目名称获取项目信息
     * @param projectName 项目名称
     * @return ApprovalInformation实体
     */
    ApprovalInformation selectByProjectName(String projectName);

    List<ExcessGrossProfit> selectExcessGrossProfit1(ExcessGrossProfit excessGrossProfit);

    String selectProjectNumByProjectName(String projectName);

    ExcessGrossProfit selectExcessGrossProfitOne(String projectName);

    void insertExcessGrossProfit(ExcessGrossProfit excess);

}