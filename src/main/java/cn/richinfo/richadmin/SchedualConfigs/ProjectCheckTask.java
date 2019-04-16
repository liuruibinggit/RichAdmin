package cn.richinfo.richadmin.SchedualConfigs;

import cn.richinfo.richadmin.Entity.BiddingAndContract.Check;
import cn.richinfo.richadmin.Entity.check.ProjectCheck;
import cn.richinfo.richadmin.Entity.Q5.Q5PmCrossMonth;
import cn.richinfo.richadmin.Entity.project.*;
import cn.richinfo.richadmin.Mapper.Check.CheckMapper;
import cn.richinfo.richadmin.Mapper.Check.ProjectCheckMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.*;
import cn.richinfo.richadmin.Mapper.Q5.Q5PmCrossMonthMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Component
public class ProjectCheckTask {

    private static final Logger logger = LoggerFactory.getLogger(ProjectCheckTask.class);
    @Autowired
    ApprovalInformationMapper approvalInformationMapper;
    @Autowired
    Q5ProjectMapper q5ProjectMapper;
    @Autowired
    ProjectCostMapper projectCostMapper;
    @Autowired
    CheckMapper checkMapper;
    @Autowired
    ProjectCheckMapper projectCheckMapper;
    @Autowired
    Q5PmCrossMonthMapper q5PmCrossMonthMapper;
    @Autowired
    PlanActualMapper planActualMapper;
    @Autowired
    PlanCostMapper planCostMapper;

    public void run() throws ParseException{
        logger.info("定时计算项目考核信息任务开始....................");

        DecimalFormat df = new DecimalFormat("#.00");
        List<ApprovalInformation> approvalList=new ArrayList<>();
        approvalList=approvalInformationMapper.selectApproval();
        for(ApprovalInformation approval:approvalList){
            ProjectCheck projectCheck=new ProjectCheck();
            projectCheck.setBusinessLine(approval.getFinancialIncome());         //所属业务单元
            projectCheck.setProjectName(approval.getProjectName());              //项目名称
            projectCheck.setProjectNum(approval.getProjectNum());                //项目编码
            projectCheck.setProjectManager(approval.getProjectManager());        //项目经理
            projectCheck.setProjectStatus(approval.getIsOver());                 //项目状态
            projectCheck.setProjectType(approval.getProjectType());              // 项目类型
            projectCheck.setInitialCostHours(approval.getCostHours());          //初始预算总工时
            projectCheck.setContactAmount(approval.getContractAmount());         //合同金额

            String overTime=String.valueOf(approval.getModifiyTime()); //结项时间
            String workTime=overTime.substring(overTime.length()-4, overTime.length());
            //年
            projectCheck.setYear(workTime);

            //项目类型得分
            String projectType=projectCheck.getProjectType();
            Double projectTypeScore=0.0;
            if(projectType.equals("建设类--开发类（从无到有进行开发）")){
                projectTypeScore=1.00;
            }else if(projectType.equals("建设类--产品类（产品+部分定制）")){
                projectTypeScore=0.75;
            }else if(projectType.equals("运营支撑类--有考核")){
                projectTypeScore=0.75;
            }else if(projectType.equals("运营支撑类--无考核")){
                projectTypeScore=0.5;
            }else if(projectType.equals("外协类")){
                projectTypeScore=0.25;

            }
            projectCheck.setProjectTypeScore(projectTypeScore);

            //总投入人月得分
            Double initialCostHours =projectCheck.getInitialCostHours();
            Double TotalHumanMonth=0.0;
            if(initialCostHours>=200){
                TotalHumanMonth=1.0;
            }else if(initialCostHours>0 && initialCostHours<200){
                TotalHumanMonth=Double.valueOf(df.format(initialCostHours/200));
            }
            projectCheck.setTotalHumanMonth(TotalHumanMonth);

            //合同金额得分
            Double contactAmount=projectCheck.getContactAmount();
            Double ContactAmountScore=0.0;
            if(contactAmount>0 && contactAmount<100*10000){
                ContactAmountScore=0.25;
            }else if(contactAmount>=100*10000 && contactAmount<300*10000){
                ContactAmountScore=0.5;
            }else if(contactAmount>=300*10000 &&contactAmount<500*10000){
                ContactAmountScore=0.75;
            }else if(contactAmount>=500*10000){
                ContactAmountScore=1.0;

            }
            projectCheck.setContactAmountScore(ContactAmountScore);


            //难度系数
            Double difficultyRatio=projectCheck.getProjectTypeScore()*0.4+projectCheck.getTotalHumanMonth()*0.4+projectCheck.getContactAmountScore()*0.2;
            projectCheck.setDifficultyRatio(difficultyRatio);

            String projectNum=projectCheck.getProjectNum();
            //项目交叉月份数
            Double crossMonth = 0.0;
            Q5PmCrossMonth q5PmCrossMonth=new Q5PmCrossMonth();
            q5PmCrossMonth.setWorkTime(workTime);
            q5PmCrossMonth.setProjectNum(projectNum);
            List<Q5PmCrossMonth> list=q5PmCrossMonthMapper.selectCrossMonthByCondition(q5PmCrossMonth);
            for(Q5PmCrossMonth q:list){
                if(null != q.getCrossMonth()){
                    crossMonth+=Double.valueOf(q.getCrossMonth());
                }
            }
            if(crossMonth<3){
                crossMonth=0.0;
            }
            projectCheck.setCrossMonth(crossMonth);

            //产品积累加分项得分
            Double productScore=0.0;
            if(null != projectCheckMapper.selectProductScore(projectNum)){
                productScore=projectCheckMapper.selectProductScore(projectNum);
            }
            projectCheck.setProductScore(productScore);

            Double timeRatio=0.0;//时间系数
            Double singleScore=0.0;//单个项目考核得分
            int projectStatus=projectCheck.getProjectStatus();
            if(projectStatus==2){  //已结项
                List<Check> checkList=checkMapper.selectByProjectNum(projectNum);
                if(checkList.size()>0){
                    projectCheck.setOverTargetScore(checkList.get(0).getOverTargetScore());  //实际毛利得分
                    //进度得分 =验收进度得分+回款进度得分
                    projectCheck.setPlanScore(checkList.get(0).getPlanActualScore()+checkList.get(0).getPlanCostScore());
                    projectCheck.setCustomerScore(checkList.get(0).getCustomerScore());//客户满意度得分

                    //单个项目考核得分=实际毛利得分+进度得分+客户满意度得分
                    projectCheck.setSingleScore(projectCheck.getOverTargetScore()+projectCheck.getPlanScore()+projectCheck.getCustomerScore());


                    double projectHours=0;
                    HashMap<String,String> map=new HashMap<String,String>();
                    map.put("year", workTime);
                    map.put("projectNum", projectNum);
                    List<Q5Project> q5Projectlist= q5ProjectMapper.selectByPrimaryKeyAndYear(map);
                    for(Q5Project q5Project:q5Projectlist){
                        double t=Double.valueOf(q5Project.getWorkingHoursCount());
                        projectHours+=t;
                    }
                    projectCheck.setProjectHours(projectHours); //该项目总工时

                    double projectManagerHours=0;
                    HashMap<String,String> map1=new HashMap<String,String>();
                    map1.put("year", workTime);
                    map1.put("managerName", approval.getProjectManager());
                    List<Q5Project> managerQ5Projectlist= q5ProjectMapper.selectByProjectManagerAndYear(map1);
                    for(Q5Project q5Project:managerQ5Projectlist){
                        double t=Double.valueOf(q5Project.getWorkingHoursCount());
                        projectManagerHours+=t;
                    }
                    projectCheck.setAllProjectHours(projectManagerHours);; //该项目经理总工时

                    //时间系数
                    if(projectManagerHours!=0){
                        timeRatio=Double.valueOf(df.format(projectHours/projectManagerHours));
                    }
                    projectCheck.setTimeRatio(timeRatio);
                }
            }else{  //未结项
                //成本得分
                List<ProjectCost> listProjectCost = projectCostMapper.selectByPrimaryKey(projectNum);
                double count=0.0;       //截止目前的累计成本，所有月份之和
                Double planCostCount=0.0;   //累计计划成本
                if(listProjectCost != null && listProjectCost.size()>0){
                    for(int i=0;i<listProjectCost.size();i++){
                        double countCost=0.0;   //每月实际合计成本
                        countCost+=(Double.parseDouble(listProjectCost.get(i).getCost1())+Double.parseDouble(listProjectCost.get(i).getCost2())+Double.parseDouble(listProjectCost.get(i).getLaborCost())+Double.parseDouble(listProjectCost.get(i).getExpenseReimbursement())
                                +Double.parseDouble(listProjectCost.get(i).getUnitApportionmentCost()));
                        count+=countCost;
                        if(null!=listProjectCost.get(i)){
                            if(!StringUtils.isBlank(listProjectCost.get(i).getPlanCost())){
                                Double planCost=Double.valueOf(listProjectCost.get(i).getPlanCost());
                                planCostCount+=planCost;
                            }
                        }
                    }
                }
                //成本分数
                Double costScore=0.0;
                if(planCostCount!=0){
                    Double a=Double.valueOf(df.format((count-planCostCount)/planCostCount));//成本偏离度
                    if(a>0.0 &&a<0.5){
                        costScore=(-100)*a+50;
                    }else if(a<=0.0){
                        costScore=50.0;
                    }else if(a>=0.5){
                        costScore=0.0;
                    }
                }

                projectCheck.setCostScore(costScore);

                //验收进度得分
                HashMap<String,String> mapPlanActual=new HashMap<String,String>();
                mapPlanActual.put("projectNum", projectNum);
                mapPlanActual.put("planTime", workTime);
                List<PlanActual> listPlanActual = planActualMapper.selectByPrimaryKeyAndPlanTime(mapPlanActual);  //计划验收里程碑
                double N=listPlanActual.size();
                double planActualScore=0;          //验收进度得分
                for(PlanActual planActual:listPlanActual){
                    double n=0;         //实际时间与计划时间的差值
                    double score=0;      //每个项目每项里程碑的单项分数
                    if(planActual.getActualTime()!=null){
                        n=(planActual.getActualTime().getTime()-planActual.getPlanTime().getTime())/(24*60*60*1000);
                        if(n<=0){
                            n=0;
                            score=15/N;
                        }else if(n>=60){
                            n=60;
                            score=0;
                        }else{
                            score=(60-n)/60*(15/N);
                        }
                    }else{
                        n=60;
                        score=0;
                    }
                    planActualScore+=score;  //每个项目所有里程碑的总分数

                }
                //////////
                HashMap<String,String> mapPlanCost=new HashMap<String,String>();
                mapPlanCost.put("projectNum", projectNum);
                mapPlanCost.put("planTime", workTime);
                List<PlanCost> listPlanCost = planCostMapper.selectByPrimaryKeyAndPlanTime(mapPlanCost);        //计划回款里程碑
                double N1=listPlanCost.size();
                double planCostScore=0;         //回款得分
                for(PlanCost planCost:listPlanCost){
                    double n=0;         //实际时间与计划时间的差值
                    double score=0;      //每个项目每项里程碑的单项分数
                    if(null!=planCost.getActualTime()&&null!=planCost.getPlanTime()){
                        n=(planCost.getActualTime().getTime()-planCost.getPlanTime().getTime())/(24*60*60*1000);
                        if(n<=0){
                            n=0;
                            score=15/N1;
                        }else if(n>=60){
                            n=60;
                            score=0;
                        }else{
                            score=(60-n)/60*(15/N1);
                        }
                    }else{
                        n=60;
                        score=0;
                    }
                    planCostScore+=score;  //每个项目所有里程碑的总分数

                }
                //进度得分
                Double planScore=planActualScore+planCostScore;
                projectCheck.setPlanScore(planScore);

                //对项目管理制度流程的遵守配合度得分
                Double cooperateScore=0.0;
                if(null != projectCheckMapper.selectByProjectNum(projectNum)){
                    if(null != projectCheckMapper.selectByProjectNum(projectNum).getCooperateScore())
                        cooperateScore=projectCheckMapper.selectByProjectNum(projectNum).getCooperateScore();
                }
                projectCheck.setCooperateScore(cooperateScore);

                //单元考核得分
                Double unitCheckScore=0.0;
                if(null != projectCheckMapper.selectByProjectNum(projectNum) ){
                    if(null != projectCheckMapper.selectByProjectNum(projectNum).getUnitCheckScore() )
                        unitCheckScore=projectCheckMapper.selectByProjectNum(projectNum).getUnitCheckScore();
                }
                projectCheck.setUnitCheckScore(unitCheckScore);

                //项目管理部得分
                Double  manageScore=costScore+planScore+cooperateScore;
                projectCheck.setManageScore(manageScore);

                //单个项目考核得分

                singleScore=0.5*manageScore+0.5*unitCheckScore;
                projectCheck.setSingleScore(singleScore);
                double projectHours=0;
                HashMap<String,String> map=new HashMap<String,String>();
                map.put("year", workTime);
                map.put("projectNum", projectNum);
                List<Q5Project> q5Projectlist= q5ProjectMapper.selectByPrimaryKeyAndYear(map);
                if(null!=q5Projectlist){
                    for(Q5Project q5Project:q5Projectlist){
                        if(null!=q5Project.getWorkingHoursCount()){
                            double t=Double.valueOf(q5Project.getWorkingHoursCount());
                            projectHours+=t;
                        }
                    }
                }
                projectCheck.setProjectHours(projectHours); //该项目总工时

                double projectManagerHours=0;
                HashMap<String,String> map1=new HashMap<String,String>();
                map1.put("year", workTime);
                map1.put("managerName", approval.getProjectManager());
                List<Q5Project> managerQ5Projectlist= q5ProjectMapper.selectByProjectManagerAndYear(map1);
                if(null!=managerQ5Projectlist){
                    for(Q5Project q5Project:managerQ5Projectlist){
                        if(null!=q5Project.getWorkingHoursCount()){
                            double t=Double.valueOf(q5Project.getWorkingHoursCount());
                            projectManagerHours+=t;
                        }
                    }
                }
                projectCheck.setAllProjectHours(projectManagerHours);; //该项目经理总工时

                //时间系数
                if(projectManagerHours!=0){
                    timeRatio=Double.valueOf(df.format(projectHours/projectManagerHours));
                }
                projectCheck.setTimeRatio(timeRatio);

            }
            if(productScore==null){
                productScore=0.0;
            }
            if(crossMonth<3){
                crossMonth=0.0;
            }
            Double projectCheckScore =(singleScore*timeRatio*difficultyRatio)*(1+crossMonth/12*0.2)*(1+productScore);
            projectCheck.setProjectCheckScore(projectCheckScore);

            //加入到数据库
            if(null!=projectCheckMapper.selectByProjectNum(projectNum)){
                Integer id =projectCheckMapper.selectByProjectNum(projectNum).getProjectCheckId();
                projectCheckMapper.deleteByPrimaryKey(id);
            }

            projectCheckMapper.insertSelective(projectCheck);
        }
        logger.info("定时计算项目考核信息任务结束....................");
    }
}
