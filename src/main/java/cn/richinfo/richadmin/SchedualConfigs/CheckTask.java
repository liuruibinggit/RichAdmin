package cn.richinfo.richadmin.SchedualConfigs;

import cn.richinfo.richadmin.Entity.BiddingAndContract.Check;
import cn.richinfo.richadmin.Entity.project.*;
import cn.richinfo.richadmin.Mapper.Check.CheckMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/16 0016.
 */
@Component
public class CheckTask {

    private static final Logger logger = LoggerFactory.getLogger(CheckTask.class);

    @Autowired
    ApprovalInformationMapper approvalInformationMapper;
    @Autowired
    PlanCostMapper planCostMapper;
    @Autowired
    PlanActualMapper planActualMapper;
    @Autowired
    ProjectCostMapper projectCostMapper;
    @Autowired
    ProjectAchievementsMapper projectAchievementsMapper;
    @Autowired
    CheckMapper checkMapper;
    @Autowired
    Q5ProjectMapper q5ProjectMapper;
    @Autowired
    Q5WorkDetailsMapper q5WorkDetailsMapper;

    public void run() throws ParseException {
        logger.info("定时计算考核信息任务开始....................");
        List<Check> checkList=new ArrayList<Check>();
        List<ApprovalInformation> approvalList=new ArrayList<ApprovalInformation>();
        approvalList=approvalInformationMapper.selectApproval();
        for(ApprovalInformation approval:approvalList){
            Check check=new Check();
            check.setBusinessLine(approval.getFinancialIncome());
            check.setProjectName(approval.getProjectName());
            check.setProjectNum(approval.getProjectNum());
            check.setProjectManager(approval.getProjectManager());
            check.setProjectType(approval.getProjectType());
            check.setContractAmount(approval.getContractAmount());
            check.setTargetProfitMargin(Double.valueOf(approval.getTargetProfitMargin()));
            check.setTargetProfit(approval.getTargetProfit());
            checkList.add(check);
        }
        for(Check check:checkList){
            String projectNum=check.getProjectNum();
            //根据项目编号计算结项毛利开始
            ApprovalInformation approval = approvalInformationMapper.getApprovalByNum(projectNum);
            List<ProjectCost> listProjectCost = projectCostMapper.selectByPrimaryKey(projectNum);
            List<PlanCost> listPlanCost = planCostMapper.selectByPrimaryKey(projectNum);        //计划回款里程碑
            double overCost =0.0;//实际成本
            double overIncome =0.0;//结算金额
            double rage = Double.parseDouble(approval.getTaxRate().replace("%", ""))/100; //税率

            for(int i=0;i<listProjectCost.size();i++){
                if(i==listProjectCost.size()-1){
                    System.out.println("projectNum:.............  "+projectNum);
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
            double overTarget=overIncome/(1+rage)-overCost;
            //根据项目编号计算结项毛利结束
            if(approval.getIsOver()==0){     //未结项
                overTarget=0;
            }
            check.setOverTarget(overTarget);                   //实际毛利
            ProjectAchievements projectAchievements =new ProjectAchievements();
            projectAchievements=projectAchievementsMapper.selectByPrimaryKey(projectNum);
            if(null!=projectAchievements){
                check.setProjectStatus(projectAchievements.getProjectState()+"");   //项目状态
            }
            double x=check.getOverTarget()/check.getTargetProfit();
            if(x>1){
                x=1;
            }else if(x<0.6){
                x=0;
            }
            double overTargetScore=70*x;

            if(approval.getIsOver()==0){     //未结项
                overTargetScore=0;
            }
            check.setOverTargetScore(overTargetScore);    //实际毛利得分
            ///////////
            List<PlanActual> listPlanActual = planActualMapper.selectByPrimaryKey(projectNum);  //计划验收里程碑
            double N=listPlanActual.size();
            double planActualScore=0;         //
            for(PlanActual planActual:listPlanActual){
                double n=0;         //实际时间与计划时间的差值
                double score=0;      //每个项目每项里程碑的单项分数

                if(planActual.getActualTime()!=null){
                    n=(planActual.getActualTime().getTime()-planActual.getPlanTime().getTime())/(24*60*60*1000);
                    if(n<=0){
                        n=0;
                        score=10/N;
                    }else if(n>=60){
                        n=60;
                        score=0;
                    }else{
                        score=(60-n)/60*(10/N);
                    }
                }else{
                    n=60;
                    score=0;
                }
                planActualScore+=score;  //每个项目所有里程碑的总分数

            }
            check.setPlanActualScore(planActualScore);     //验收进度得分
            //////////
            double N1=listPlanCost.size();
            double planCostScore=0;         //
            for(PlanCost planCost:listPlanCost){
                double n=0;         //实际时间与计划时间的差值
                double score=0;      //每个项目每项里程碑的单项分数
                if(null!=planCost.getActualTime()&&null!=planCost.getPlanTime()){
                    n=(planCost.getActualTime().getTime()-planCost.getPlanTime().getTime())/(24*60*60*1000);
                    if(n<=0){
                        n=0;
                        score=10/N1;
                    }else if(n>=60){
                        n=60;
                        score=0;
                    }else{
                        score=(60-n)/60*(10/N1);
                    }
                }else{
                    n=60;
                    score=0;
                }
                planCostScore+=score;  //每个项目所有里程碑的总分数

            }
            check.setPlanCostScore(planCostScore);     //回款得分
            double topScore=0;
            if(approval.getIsOver()==0){
                topScore=30;

            }else{
                topScore=100;
            }
            check.setTopScore(topScore);       //考核最高分

            double actualScore;
            double customerScore;
            List<Check> check1 =checkMapper.selectByProjectNum(projectNum);
            if(check1.size()==0){
                customerScore=0;
            }else{
                customerScore=checkMapper.selectCustomerScore(projectNum);
            }
            actualScore=overTargetScore+planActualScore+planCostScore+customerScore;
            check.setActualScore(actualScore);          //当前实际得分
            check.setCustomerScore(customerScore);
            double percentScore=0;
            if(approval.getIsOver()==0){     //未结项
                percentScore=(actualScore/30)*100;
            }else{
                percentScore=actualScore;
            }
            DecimalFormat df = new DecimalFormat("0.00%");
            String percentScore1 = df.format(percentScore/100);
            check.setPercentScore(percentScore1);   //百分制分数

            double workingHoursAll=0;
            List<Q5Project> list=q5ProjectMapper.selectByPrimaryKey(projectNum);
            for(Q5Project q5Project:list){
                double t=Double.valueOf(q5Project.getWorkingHoursCount());
                workingHoursAll+=t;
            }
            check.setWorkingHoursAll(workingHoursAll); //总工时
            Double workingHoursManager;
            Q5WorkDetails q5WorkDetails=new Q5WorkDetails();
            q5WorkDetails.setEmployeeNumber(list.get(0).getManagerNum());
            q5WorkDetails.setProjectNum(projectNum);
            workingHoursManager=q5WorkDetailsMapper.selectAllHours1(q5WorkDetails);
            if(null!=workingHoursManager){
                check.setWorkingHoursManager(workingHoursManager);  //项目经理工时
            }
            //加到数据库中
            if(check1.size()!=0){
                checkMapper.deleteByProjectNum(projectNum);
            }
            checkMapper.insertSelective(check);
        }

        logger.info("定时计算考核信息任务结束..................................");
    }

}
