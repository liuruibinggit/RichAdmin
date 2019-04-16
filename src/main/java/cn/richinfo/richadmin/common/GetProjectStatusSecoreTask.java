package cn.richinfo.richadmin.common;

import cn.richinfo.richadmin.Entity.project.*;
import cn.richinfo.richadmin.Mapper.ProjectMapper.*;
import cn.richinfo.richadmin.Service.Project.ProjectAchievementsService;
import cn.richinfo.richadmin.common.utils.DateUtil;
import cn.richinfo.richadmin.common.utils.DoubleUtil;
import cn.richinfo.richadmin.common.vo.project.DiagnosticReportVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LiuRuibing on 2019/4/4 0004.
 */
@Component
public class GetProjectStatusSecoreTask {

    private static final Logger logger = LoggerFactory.getLogger(GetProjectStatusSecoreTask.class);

    @Autowired
    private ApprovalInformationMapper approvalInformationMapper;
    @Autowired
    private PlanCostMapper planCostMapper;
    @Autowired
    private PlanActualMapper planActualMapper;
    @Autowired
    private Q5ProjectMapper q5ProjectMapper;
    @Autowired
    private ProjectImplementationMapper projectImplementationMapper;
    @Autowired
    private ProjectAchievementsService projectAchievementsService;
    @Autowired
    private ProjectStatusSecoreMapper projectStatusSecoreMapper;
    @Autowired
    private ProjectCostMapper projectCostMapper;
    @Autowired
    private ShareCoefficientMapper shareCoefficientMapper;
    @Autowired
    private TotalGainsLossMapper totalGainsLossMapper;


    public void run() throws ParseException{
        List<ApprovalInformation> listApp = approvalInformationMapper.selectApp();
        String projectNum ="";
        int top5=0;
        int isHandTrans=0;
        if (listApp != null && listApp.size() > 0) {
            for (ApprovalInformation approvalInformation : listApp) {
                ProjectStatusSecore projectStatusSecore = new ProjectStatusSecore();
                //“结算率”维度，对于一次都没有录入过“实际回款金额”的项目，属于正常状态，只能亮绿灯，不能亮红灯，不需要进入TOP5。   标记为1
                List<PlanCost> planCostList = planCostMapper.selectByPrimaryKey(approvalInformation.getProjectNum());
                if (planCostList == null || planCostList.size() == 0) {
                    projectStatusSecore.setAccountingRate(1);
                } else {
                    if (planCostList.get(0).getActualTime() == null || "".equals(planCostList.get(0).getActualTime())) {
                        projectStatusSecore.setAccountingRate(1);
                    }
                }
                //手动转立项的项目  标记为 不在TOP5 展示
                isHandTrans = approvalInformation.getIsHandTrans();
                switch (isHandTrans) {
                    case 0:
                        top5 = 0;
                        break;
                    default:
                        top5 = 1;
                        break;
                }
                projectStatusSecore.setTop5(top5);
                projectNum = approvalInformation.getProjectNum();
                List<PlanActual> planActuals = planActualMapper.selectByPrimaryKey(projectNum);
                //查询并判断该项目的项目进度是否有初验里程碑，并且是否通过了初验里程碑
                for (PlanActual planActual : planActuals) {
                    if (ConstantClassField.CHUYAN.equals(planActual.getStateName()) && planActual.getActualTime() != null) {
                        projectStatusSecore.setSpeedInitialTest(1);
                        break;
                    } else if (ConstantClassField.CHUYAN.equals(planActual.getStateName()) && planActual.getActualTime() == null) {
                        projectStatusSecore.setSpeedInitialTest(0);
                        break;
                    } else {
                        projectStatusSecore.setSpeedInitialTest(0);
                    }
                }
                //查询并判断该项目的项目回款是否有初验终验款里程碑，并且是否通过了初验终验款里程碑
                Map<String, String> speedMap = new HashMap<>();
                for (PlanActual planActual : planActuals) {
                    if (ConstantClassField.CHUYAN.equals(planActual.getStateName()) && planActual.getActualTime() != null) {
                        speedMap.put("startSpeed", ConstantClassField.CHUYAN);
                    }
                    if (ConstantClassField.ZHONGYAN.equals(planActual.getStateName()) && planActual.getActualTime() != null) {
                        speedMap.put("endSpeed", ConstantClassField.ZHONGYAN);
                    }
                }
                List<PlanCost> planCosts = planCostMapper.selectByPrimaryKey(projectNum);
                Map<String, String> payMentMap = new HashMap<String, String>();
                for (PlanCost planCost : planCosts) {
                    if (ConstantClassField.CHUYANKUAN.equals(planCost.getStateName()) && planCost.getActualTime() != null) {
                        payMentMap.put("startPayMent", ConstantClassField.CHUYANKUAN);
                    }
                    if (ConstantClassField.ZHONGYANKUAN.equals(planCost.getStateName()) && planCost.getActualTime() != null) {
                        payMentMap.put("endPayMent", ConstantClassField.ZHONGYANKUAN);
                    }
                    if (ConstantClassField.WEIKUAN.equals(planCost.getStateName()) && planCost.getActualTime() != null) {
                        payMentMap.put("endPayMent", ConstantClassField.WEIKUAN);
                    }
                }
                if (speedMap.get("startSpeed") != null) {
                    //初验款收回情况
                    if (payMentMap.size() > 0) {
                        if (payMentMap.get("startPayMent") != null) { // 初验款已经收回
                            projectStatusSecore.setPaymentInitialTest(1);
                        } else {
                            projectStatusSecore.setPaymentInitialTest(0);
                        }
                    } else {
                        projectStatusSecore.setPaymentInitialTest(0);
                    }
                }
                if (speedMap.get("startSpeed") == null && speedMap.get("endSpeed") != null) {
                    //终验款收回情况
                    if (payMentMap.size() > 0) {
                        if (payMentMap.get("endPayMent") != null) { // 无初验款  终验收款已经收回
                            projectStatusSecore.setPaymentInitialTest(1);
                        } else if (payMentMap.get("endPayMent") == null) { //// 后续无投入   终验收款收回
                            projectStatusSecore.setPaymentInitialTest(0);
                        } else {
                            projectStatusSecore.setPaymentInitialTest(0);
                        }
                    } else {
                        projectStatusSecore.setPaymentInitialTest(0);
                    }
                }
                if (speedMap.get("startSpeed") == null && speedMap.get("endSpeed") == null) {
                    projectStatusSecore.setPaymentInitialTest(0); //无初验款  终验款
                }
                List<Q5Project> q5ProjectList = q5ProjectMapper.selectByPrimaryKey(approvalInformation.getProjectNum());
                projectStatusSecore.setProjectManager(approvalInformation.getProjectManager());
                projectStatusSecore.setProjectProducts(approvalInformation.getProjectProducts());
                projectStatusSecore.setContractAmount(approvalInformation.getContractAmount());
                projectStatusSecore.setProjectName(approvalInformation.getProjectName());
                projectStatusSecore.setProjectNum(approvalInformation.getProjectNum());
                projectStatusSecore.setProjectType(approvalInformation.getProjectCategory());
                projectStatusSecore.setDept(approvalInformation.getFinancialIncome());
                projectStatusSecore.setIsDept(approvalInformation.getIsDept());
                projectStatusSecore.setRoom(approvalInformation.getRoom());
                projectStatusSecore.setTargetProfitMargin(approvalInformation.getTargetProfitMargin());
                Calendar date = Calendar.getInstance();
                int year = date.get(Calendar.YEAR);
                int month = date.get(Calendar.MONTH);
                String monthNow=DateUtil.getStandardYearAndMonth(month,year);
                projectStatusSecore.setMonth(Integer.parseInt(monthNow));
                ProjectImplementation pit = projectImplementationMapper
                        .selectByPrimaryKey(approvalInformation.getProjectNum());
                if (pit != null) {
                    projectStatusSecore.setProjectStatus(pit.getProjectState() + "");
                }
                double manHours = 0;
                if (approvalInformation.getCostHours() != 0) {
                    manHours = DoubleUtil.doubleToTwo(getCountHours(approvalInformation.getProjectNum()) / approvalInformation.getCostHours());
                }
                projectStatusSecore.setManHours(manHours);
                if (approvalInformation != null) {
                    projectStatusSecore.setCostCount(getCountCost(approvalInformation.getProjectNum()));
                }
                projectStatusSecore.setHoursCount(getCountHours(approvalInformation.getProjectNum()));
                projectStatusSecore.setPayCount(getPayCount(approvalInformation.getProjectNum()));
                projectStatusSecore.setSettlementr(0);
                DiagnosticReportVo diagnosticReportVo = (DiagnosticReportVo) projectAchievementsService.diagnosticReport(approvalInformation.getProjectNum());
                projectStatusSecore.setCostValue(diagnosticReportVo.getCostValue());
                projectStatusSecore.setManhourValue(diagnosticReportVo.getManhourValue());
                projectStatusSecore.setPaymentValue(diagnosticReportVo.getPaymentValue());
                projectStatusSecore.setSettlementrValue(diagnosticReportVo.getSettlementrValue());
                projectStatusSecore.setSpeedValue(diagnosticReportVo.getSpeedValue());
                projectStatusSecore.setCostLevel(diagnosticReportVo.getCostlevel());
                projectStatusSecore.setSpeedLevel(Integer.parseInt(diagnosticReportVo.getSpeedlevel()));
                projectStatusSecore.setHoursLevel(diagnosticReportVo.getManhourlevel());
                projectStatusSecore.setSettlementrLevel(diagnosticReportVo.getSettlementrlevel());
                projectStatusSecore.setPayLevel(Integer.parseInt(diagnosticReportVo.getPaymentlevel()));
                projectStatusSecore.setCostType(diagnosticReportVo.getCost());
                projectStatusSecore.setHoursType(diagnosticReportVo.getManhour());
                projectStatusSecore.setPayType(diagnosticReportVo.getPayment());
                projectStatusSecore.setSpeedType(diagnosticReportVo.getSpeed());
                projectStatusSecore.setSettlementrType(diagnosticReportVo.getSettlementr());
                projectStatusSecore.setGrossprofitSecory(diagnosticReportVo.getList().get(0).getRollingMaoriScore());//滚动毛利率得分
                projectStatusSecore.setSpeedSecory(diagnosticReportVo.getList().get(0).getAcceptanceProgressScore());//验收进度得分
                projectStatusSecore.setPaySecory(diagnosticReportVo.getList().get(0).getReturnProgressScore()); //回款进度得分
                projectStatusSecore.setSettlementrSecory(diagnosticReportVo.getList().get(0).getSettlementRateScore()); //结算额得分
                projectStatusSecore.setCountSecory(diagnosticReportVo.getList().get(0).getScore()); //综合得分
                if (diagnosticReportVo.getList().get(0).getScore() >= 90) {
                    projectStatusSecore.setSecoreLevel(1);
                } else if (diagnosticReportVo.getList().get(0).getScore() < 70) {
                    projectStatusSecore.setSecoreLevel(3);
                } else {
                    projectStatusSecore.setSecoreLevel(2);
                }
                //项目报工大于3个月的
                if (q5ProjectList.size() > 3 && isHandTrans != 1) {
                    projectStatusSecore.setGrossprofitValue(diagnosticReportVo.getGrossprofitValue());
                    projectStatusSecore.setGrossprofit(getGrossProfit(approvalInformation.getProjectNum()));
                    projectStatusSecore.setGrossprofitLevel(diagnosticReportVo.getGrossprofitlevel());
                    projectStatusSecore.setGrossprofitType(diagnosticReportVo.getGrossprofit());
                    projectStatusSecore.setGrossprofitSecory(diagnosticReportVo.getList().get(0).getRollingMaoriScore());
                } else { //项目报工小于3个月的， 或者是手动转立项的项目 ，不计算滚动毛利率
                    projectStatusSecore.setGrossprofitValue(0.0);
                    projectStatusSecore.setGrossprofit(0.0);
                    projectStatusSecore.setGrossprofitLevel(0);
                    projectStatusSecore.setGrossprofitType(0);
                    projectStatusSecore.setGrossprofitSecory(0);
                    projectStatusSecore.setSecoreLevel(2);
                    projectStatusSecore.setTop5(1);
                    projectStatusSecore.setGrossfitRateFlag(1);
                }
                projectStatusSecoreMapper.insertSelective(projectStatusSecore);
            }
        }
    }
    /*
	 * 返回当前项目的总成本
	 */
    private double getCountCost(String projectNum) {
        double costCount = 0.0;
        ProjectImplementation projectImplementation = projectImplementationMapper.selectByPrimaryKey(projectNum);
        Double cost = (projectCostMapper.getCountCost(projectNum)==null)? 0.0 :  projectCostMapper.getCountCost(projectNum);
        if(projectImplementation!=null){
            costCount = cost + projectImplementation.getBonusBalance();
        }else{
            costCount = cost+0;
        }
        return DoubleUtil.doubleToTwo(costCount);
    }

    /*
     * 获取项目总工时
     */
    private double getCountHours(String projectNum) {
        double hoursCount = 0.0;
        hoursCount = q5ProjectMapper.getProjectCountHours(projectNum);
        return DoubleUtil.doubleToTwo(hoursCount);
    }

    /*
     * 获取项目总回款金额
     */
    private double getPayCount(String projectNum) {
        List<PlanCost> planCostList = planCostMapper.selectByPrimaryKey(projectNum);
        double actualCostCount = 0.0;
        if (planCostList != null && planCostList.size() > 0) {
            for (PlanCost planCost : planCostList) {
                if(planCost.getActualCost() != null && !planCost.getActualCost().equals("")){
                    actualCostCount += Double.parseDouble(planCost.getActualCost());
                }
            }
        }
        return DoubleUtil.doubleToTwo(actualCostCount);
    }

    /**
     * 获取项目滚动毛利率
     */
    private double getGrossProfit(String projectNum){
        double grossProfit = 0.0;
        ApprovalInformation app = approvalInformationMapper.getApprovalByNum(projectNum);
        ProjectImplementation projectImplementation = projectImplementationMapper.selectByPrimaryKey(projectNum);
        String taxRate = app.getTaxRate().replace("%", "");
        if (taxRate.equals("")) {
            taxRate = "0";
        }
        // 税后合同金额
        double contract = 0.0;
        double contract1 = (app.getContractAmount() / (1 + Double.parseDouble(taxRate)));
        if(app.getLatestEstimatedIncome() != null){
            contract = (app.getLatestEstimatedIncome() / (1 + Double.parseDouble(taxRate)));
        }else{
            contract = (app.getContractAmount() / (1 + Double.parseDouble(taxRate)));
        }
        List<ProjectCost> listProjectCost = projectCostMapper.selectForGrossprofit(projectNum);
        // 获取已报总工时
        double countHours = q5ProjectMapper.getProjectCountHours(projectNum);
        // 尚需完工工时
        double overCostHours = app.getCostHours() - countHours;
        // 当前总成本(含公摊,预先设定加上奖金)
        double countCost = 0.0;
        if(projectImplementation != null){
            countCost = projectImplementation.getBonusBalance();
        }
        for (int i = 0; i < listProjectCost.size(); i++) {
            countCost += (Double.parseDouble(listProjectCost.get(i).getCost1())
                    + Double.parseDouble(listProjectCost.get(i).getCost2())
                    + Double.parseDouble(listProjectCost.get(i).getLaborCost())
                    + Double.parseDouble(listProjectCost.get(i).getExpenseReimbursement())
                    + Double.parseDouble(listProjectCost.get(i).getUnitApportionmentCost()));
        }
        // 单元最新公摊系数
        List<ShareCoefficient> listShare = shareCoefficientMapper.selectShareByDeptName(app.getFinancialIncome());
        double shareCoefficient = 0;
        if (listShare.get(0).getValue() != null) {
            shareCoefficient = listShare.get(0).getValue();
        }
        // 获取最新单元人月单价
        String maxDate = totalGainsLossMapper.getMaxDate();
        double perCapitaCost = totalGainsLossMapper.selectPerCapitaCost(projectNum, maxDate);
        // 滚动后成本
        double grossCostCount = countCost + (overCostHours * perCapitaCost * (1 + shareCoefficient));
        grossProfit = ((contract - grossCostCount) / contract1) * 100;

        double doubleToTwo;
        try {
            doubleToTwo = DoubleUtil.doubleToTwo(grossProfit);
            return doubleToTwo;
        } catch (Exception e) {
            return 0;
        }
    }
}
