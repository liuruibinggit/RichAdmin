package cn.richinfo.richadmin.SchedualConfigs;

import cn.richinfo.richadmin.Entity.BiddingAndContract.BiddingInformation;
import cn.richinfo.richadmin.Entity.BiddingAndContract.ContractInformation;
import cn.richinfo.richadmin.Entity.BiddingAndContract.NoWinBidding;
import cn.richinfo.richadmin.Entity.project.ApprovalInformation;
import cn.richinfo.richadmin.Entity.project.ProjectCost;
import cn.richinfo.richadmin.Entity.project.Q5Project;
import cn.richinfo.richadmin.Mapper.BiddingAndContract.BiddingInformationMapper;
import cn.richinfo.richadmin.Mapper.BiddingAndContract.ContractInformationMapper;
import cn.richinfo.richadmin.Mapper.BiddingAndContract.ContractPlanMapper;
import cn.richinfo.richadmin.Mapper.BiddingAndContract.NoWinBiddingMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.ApprovalInformationMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.ProjectCostMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.Q5ProjectMapper;
import cn.richinfo.richadmin.common.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/16 0016.
 */
@Component
public class NoContractWinBiddingTask {

    private static final Logger logger = LoggerFactory.getLogger(SigningTask.class);

    @Autowired
    BiddingInformationMapper biddingInformationMapper;
    @Autowired
    ContractInformationMapper contractInformationMapper;
    @Autowired
    ApprovalInformationMapper approvalInformationMapper;
    @Autowired
    ContractPlanMapper contractPlanMapper;
    @Autowired
    ProjectCostMapper projectCostMapper;
    @Autowired
    Q5ProjectMapper q5ProjectMapper;
    @Autowired
    CommonService commonService;
    @Autowired
    NoWinBiddingMapper noWinBiddingMapper;

    @Scheduled(cron ="${winBiddingDate}")
    public void run() throws ParseException{
        logger.info("定时清理中标但未签约项目信息任务开始....................");

        BiddingInformation biddingInformation = new BiddingInformation();
        List<BiddingInformation> biddingList = biddingInformationMapper.getWinBidding(biddingInformation);
        List<BiddingInformation> noContractBidding = new ArrayList();
        if(biddingList != null && biddingList.size()>0){
            for(int i=0;i<biddingList.size();i++){
                List<ContractInformation> conList=contractInformationMapper.getContarctByProjectName(biddingList.get(i).getProjectName());
                if(conList == null || conList.size()==0){
                    noContractBidding.add(biddingList.get(i));
                }
            }
        }
        if(noContractBidding != null && noContractBidding.size()>0){
            noWinBiddingMapper.deleteAll();
            for(int i=0;i<noContractBidding.size();i++){
                NoWinBidding noWinBidding = new NoWinBidding();
                noWinBidding.setProjectName(noContractBidding.get(i).getProjectName());
                noWinBidding.setSigingAmount(noContractBidding.get(i).getSigningAmont());
                noWinBidding.setBidTime(noContractBidding.get(i).getBidTime());

                ApprovalInformation approval = approvalInformationMapper.selectByProjectName(noContractBidding.get(i).getProjectName());
                if(approval != null){
                    noWinBidding.setProjectManager(approval.getProjectManager());
                    noWinBidding.setCostHours(approval.getInitialCostHours());
                    List<ProjectCost> projectCost = projectCostMapper.selectByPrimaryKey(approval.getProjectNum());
                    if(approval.getIsProject() == 0){
                        noWinBidding.setProjectEstimatedCost(approval.getEstimatedCost());
                    }else{
                        noWinBidding.setProjectEstimatedCost(approval.getProjectEstimatedCost());
                        noWinBidding.setCostHours(approval.getInitialCostHours());
                    }
                    if(projectCost != null && projectCost.size()>0){
                        double cost =0.00;
                        for(int j=0;j<projectCost.size();j++){
                            cost+=Double.parseDouble(projectCost.get(j).getCost1())+Double.parseDouble(projectCost.get(j).getCost2())+Double.parseDouble(projectCost.get(j).getLaborCost())+Double.parseDouble(projectCost.get(j).getExpenseReimbursement());
                        }
                        noWinBidding.setProjectCost(cost);
                        noWinBidding.setBudgetShare(cost/noWinBidding.getProjectEstimatedCost());
                    }
                    List<Q5Project> listQ5 = q5ProjectMapper.selectByPrimaryKey(approval.getProjectNum());
                    if(listQ5 != null && listQ5.size()>0){
                        double q5Count = 0.0;
                        for(int k=0;k<listQ5.size();k++){
                            q5Count += Double.parseDouble(listQ5.get(k).getWorkingHoursCount());
                        }
                        noWinBidding.setProjectHoursCount(q5Count);
                    }
                    noWinBidding.setGrossProfit(commonService.getBiddingGrossProfit(noContractBidding.get(i).getProjectName()));
                }else{
                    noWinBidding.setProjectEstimatedCost(noContractBidding.get(i).getBudgetedCost());
                    noWinBidding.setProjectCost(0.00);
                    noWinBidding.setBudgetShare(0.00);
                    noWinBidding.setCostHours(0.00);
                    noWinBidding.setProjectHoursCount(0.00);
                    noWinBidding.setGrossProfit(0.0);
                }
                noWinBiddingMapper.insertSelective(noWinBidding);
            }
        }
    }
}
