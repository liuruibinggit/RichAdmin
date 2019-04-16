package cn.richinfo.richadmin.SchedualConfigs;

import cn.richinfo.richadmin.Entity.BiddingAndContract.ContractInformation;
import cn.richinfo.richadmin.Entity.BiddingAndContract.ExcessGrossProfit;
import cn.richinfo.richadmin.Entity.project.ApprovalInformation;
import cn.richinfo.richadmin.Entity.project.PlanCost;
import cn.richinfo.richadmin.Entity.project.ProjectCost;
import cn.richinfo.richadmin.Mapper.BiddingAndContract.ContractInformationMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.ApprovalInformationMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.PlanCostMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.ProjectCostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 * 定时计算超额毛利计算表  朝九晚五工作时间内每半小时
 */
@Component
public class ExcessGrossProfitTask {

    private static final Logger logger = LoggerFactory.getLogger(ExcessGrossProfitTask.class);

    @Autowired
    private ApprovalInformationMapper approvalInformationMapper;
    @Autowired
    private PlanCostMapper planCostMapper;
    @Autowired
    private ProjectCostMapper projectCostMapper;
    @Autowired
    private ContractInformationMapper contractInformationMapper;

    public void run() throws ParseException{
        logger.info("定时计算超额毛利计算表任务开始....................");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        List<ExcessGrossProfit> list=new ArrayList<>();
        ExcessGrossProfit excess = new ExcessGrossProfit();
        ContractInformation contractInformation=new ContractInformation();
        ApprovalInformation approval=new ApprovalInformation();
        list = approvalInformationMapper.selectExcessGrossProfit1(excess);
        for(ExcessGrossProfit s :list){
            contractInformation=contractInformationMapper.selectExcessGrossProfit2(s.getProjectName());
            if(contractInformation!=null){
                String contractStartTime=sdf.format(contractInformation.getContractStartTime());
                String contractEndTime=sdf.format(contractInformation.getContractEndTime());
                s.setContractAllTime(contractStartTime+"-"+contractEndTime);
            }else{
                s.setContractAllTime("合同未约定");
            }
            approval = approvalInformationMapper.getApprovalByNum(approvalInformationMapper.selectProjectNumByProjectName(s.getProjectName()));
            double overCost =0.0;//实际成本
            double overIncome =0.0;//结算金额
            double rage = Double.parseDouble(approval.getTaxRate()); //税率

            List<ProjectCost> listProjectCost = projectCostMapper.selectByPrimaryKey(approvalInformationMapper.selectProjectNumByProjectName(s.getProjectName()));
            List<PlanCost> listPlanCost = planCostMapper.selectByPrimaryKey(approvalInformationMapper.selectProjectNumByProjectName(s.getProjectName()));
            for(int i=0;i<listProjectCost.size();i++){
                if(i!=listProjectCost.size()-1){
                    overCost+=(Double.parseDouble(listProjectCost.get(i).getCost1())+Double.parseDouble(listProjectCost.get(i).getCost2())+Double.parseDouble(listProjectCost.get(i).getLaborCost())+Double.parseDouble(listProjectCost.get(i).getExpenseReimbursement()+Double.parseDouble(listProjectCost.get(i).getHistoryCost())));
                }else{
                    overCost+=(Double.parseDouble(listProjectCost.get(i).getCost1())+Double.parseDouble(listProjectCost.get(i).getCost2())+Double.parseDouble(listProjectCost.get(i).getLaborCost())+Double.parseDouble(listProjectCost.get(i).getExpenseReimbursement()));
                }
            }

            if(listPlanCost != null && listPlanCost.size()>0){
                for(int i=0;i<listPlanCost.size();i++){
                    if(listPlanCost.get(i).getActualCost() != null && !listPlanCost.get(i).getActualCost().equals("")){
                        overIncome += Double.parseDouble(listPlanCost.get(i).getActualCost());
                    }
                }
            }
            double overTarget=overIncome/(1+rage)-overCost;   //结项毛利
            double overTargeMargin=overTarget/(overIncome/(1+rage));//结项毛利率
            s.setOverTarget(overTarget);
            s.setOverTargetMargin(overTargeMargin);
            s.setActualContractAmount(overCost/(1+rage));
            double excessGrossProfit=overTarget-s.getTargetProfit();
            s.setExcessGrossProfit(excessGrossProfit);
            if(s.getExcessGrossProfit()>0){
                excess=approvalInformationMapper.selectExcessGrossProfitOne(s.getProjectName());
                if(excess==null){
                    approvalInformationMapper.insertExcessGrossProfit(s);
                }
            }
        }
        logger.info("定时计算超额毛利计算表任务结束..................................");
    }
}
