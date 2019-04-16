package cn.richinfo.richadmin.common;

import cn.richinfo.richadmin.Entity.BiddingAndContract.BiddingInformation;
import cn.richinfo.richadmin.Entity.project.ApprovalInformation;
import cn.richinfo.richadmin.Entity.project.ProjectCost;
import cn.richinfo.richadmin.Entity.project.Q5Project;
import cn.richinfo.richadmin.Entity.project.ShareCoefficient;
import cn.richinfo.richadmin.Mapper.BiddingAndContract.BiddingInformationMapper;
import cn.richinfo.richadmin.Mapper.BiddingAndContract.ContractInformationMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.ApprovalInformationMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.ProjectCostMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.Q5ProjectMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.ShareCoefficientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/16 0016.
 */
@Component
public class CommonService {

    @Autowired
    ApprovalInformationMapper approvalInformationMapper;
    @Autowired
    Q5ProjectMapper q5ProjectMapper;
    @Autowired
    ContractInformationMapper contractInformationMapper;
    @Autowired
    ProjectCostMapper projectCostMapper;
    @Autowired
    BiddingInformationMapper biddingInformationMapper;
    @Autowired
    ShareCoefficientMapper shareCoefficientMapper;

    /**
     * 根据项目名称获取已中标但未签合同项目的滚动毛利率
     * 计算公式
     * 滚动后毛利率=1-税率（取自投标管理）*（实际成本+预期尚需投入成本）/签单金额
     * 预期尚需投入成本=预期尚需投入人月（取自实施管理）*（截止本月累计项目实际成本(不含公摊)（取自实施管理）/截止本月累计投入工时）*（1+最新公摊系数）
     * @param projectName
     * @return
     */
    public double getBiddingGrossProfit(String projectName){
        double grossProfit = 0.00;
        ApprovalInformation approvalInformation = approvalInformationMapper.selectByProjectName(projectName);
        BiddingInformation biddingInformation = biddingInformationMapper.getBiddingByName(projectName);

        double taxRate = Double.parseDouble(biddingInformation.getTaxRate());  //投标中税率
        double finalOffer = biddingInformation.getFinalOffer(); //签单金额
        double hoursCount = 0.0; //累计投入总工时
        double needsHours =0.0;//尚需投入总工时
        double projectCost = 0.00; //累计投入人员成本(不含公摊)
        double proCost =0.00; //累计投入成本含公摊
        double shareCoefficient = 0.0;//最新公摊系数

        if(approvalInformation != null){
            List<ProjectCost> listProjectCost = projectCostMapper.selectByPrimaryKey(approvalInformation.getProjectNum());
            List<ShareCoefficient> listShareCoefficient = shareCoefficientMapper.selectShareByDeptName(approvalInformation.getFinancialIncome());
            shareCoefficient = listShareCoefficient.get(0).getValue();
            if(listProjectCost != null && listProjectCost.size()>0){
                for(ProjectCost pc:listProjectCost){
                    projectCost += (Double.parseDouble(pc.getLaborCost())+Double.parseDouble(pc.getExpenseReimbursement()));
                    proCost += (projectCost+Double.parseDouble(pc.getCost1())+Double.parseDouble(pc.getCost2()));
                }
            }
            List<Q5Project> listQ5Project = q5ProjectMapper.selectByPrimaryKey(approvalInformation.getProjectNum());
            if(listQ5Project != null && listQ5Project.size()>0){
                needsHours = Double.parseDouble(listQ5Project.get(0).getPlanCountMonth());
                for(Q5Project q5 : listQ5Project){
                    hoursCount += Double.parseDouble(q5.getWorkingHoursCount());
                }
            }
            if(hoursCount != 0.0 && finalOffer != 0.0){
                double needCost = needsHours*(projectCost/hoursCount)*(1+shareCoefficient);
                grossProfit = (1-taxRate)*(proCost+needCost)/finalOffer;
            }
        }
        return grossProfit;
    }
}
