package cn.richinfo.richadmin.Service.Project.ProjectImp;

import cn.richinfo.richadmin.Entity.project.*;
import cn.richinfo.richadmin.Mapper.ProjectMapper.*;
import cn.richinfo.richadmin.Service.Project.ProjectAchievementsService;
import cn.richinfo.richadmin.common.model.project.GrossProfitModel;
import cn.richinfo.richadmin.common.model.project.GrossProfitResultModel;
import cn.richinfo.richadmin.common.utils.DateUtil;
import cn.richinfo.richadmin.common.vo.project.DiagnosticReportVo;
import cn.richinfo.richadmin.common.vo.project.DiagnosticScoreVo;
import cn.richinfo.richadmin.common.vo.project.DiagnosticTextVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Service
public class ProjectAchievementsServiceImpl implements ProjectAchievementsService {


    @Autowired
    private ProjectStatusSecoreMapper projectStatusSecoreMapper;

    @Autowired
    private ProjectAchievementsMapper projectAchievementsMapper;

    @Autowired
    private ApprovalInformationMapper approvalInformationMapper;

    @Autowired
    private ProjectImplementationMapper projectImplementationMapper;

    @Autowired
    private PlanActualMapper planActualMapper;

    @Autowired
    private Q5ProjectMapper q5ProjectMapper;

    @Autowired
    private ProjectCostMapper projectCostMapper;

    @Autowired
    private PlanCostMapper planCostMapper;

    @Autowired
    private ShareCoefficientMapper shareCoefficientMapper;

    @Autowired
    private TotalGainsLossMapper totalGainsLossMapper;

    @Override
    public void inserProject() {
        // 累计产生的工时
        double manhour1 = 0.0;
        // 累计计划工时
        double manhour2 = 0.0;
        // 每月累计实际产生成本含公摊
        double cost1 = 0.0;
        // 每月累计计划成本
        double planCost = 0.0;
        // 人均单价
        double people = 0.0;
        // 预计还需人力成本
        double planCost1 = 0.0;
        // 当前结算金额
        double actualCost = 0.0;
        // 当前结算率总和
        double actual = 0.0;
        // 上月计划工时
        double planMonth = 0.0;
        // 当月实际工时
        double workMonth = 0.0;
        DecimalFormat df = new DecimalFormat("#.00");
        projectAchievementsMapper.delete();
        List<ApprovalInformation> listApproval = approvalInformationMapper.selectApp();
        Calendar calEnd = Calendar.getInstance();
        int endYea = calEnd.get(Calendar.YEAR);
        int endMon = calEnd.get(Calendar.MONTH);
        if (endMon == 0) {
            endMon = 12;
            endYea = endYea - 1;
        }
        String planYea = endYea + "" + endMon;
        for (int i = 0; i < listApproval.size(); i++) {
            ProjectAchievements projectAchievements = new ProjectAchievements();
            ProjectImplementation projectImplementation = projectImplementationMapper
                    .selectByPrimaryKey(listApproval.get(i).getProjectNum());
            projectAchievements.setProjectNum(listApproval.get(i).getProjectNum());
            projectAchievements.setProjectName(listApproval.get(i).getProjectName());
            projectAchievements.setProjectManager(listApproval.get(i).getProjectManager());
            projectAchievements.setIsDept(listApproval.get(i).getIsDept()); // 部门
            projectAchievements.setRoom(listApproval.get(i).getRoom()); // 室
            if (projectImplementation != null) {
                projectAchievements.setProjectState(projectImplementation.getProjectState());
            }
            projectAchievements.setDept(listApproval.get(i).getFinancialIncome());
            projectAchievements.setProjectType(listApproval.get(i).getProjectType());
            // 获取进度
            List<PlanActual> listPlanActual = planActualMapper.selectByPrimaryKey(listApproval.get(i).getProjectNum());
            for (int j = 0; j < listPlanActual.size(); j++) {
                if (listPlanActual.get(j).getActualTime() != null) {
                    int speed = DateUtil.getDiffDateDays(listPlanActual.get(j).getPlanTime(),
                            listPlanActual.get(j).getActualTime());
                    projectAchievements.setSpeed1(speed);
                }
            }
            List<Q5Project> listQ5Project = q5ProjectMapper.selectByPrimaryKey(listApproval.get(i).getProjectNum());
            int k = 0;
            if (listQ5Project != null && listQ5Project.size() > 0) {
                for (int j = 0; j < listQ5Project.size(); j++) {
                    manhour1 += Double.parseDouble(listQ5Project.get(j).getWorkingHoursCount());
                    if (listQ5Project.get(j).getWorkTime().equals(planYea)) {
                        workMonth = Double.parseDouble(listQ5Project.get(j).getWorkingHoursCount());
                    }
                    ProjectCost projectCost = projectCostMapper.selectByNumYear(listQ5Project.get(j).getProjectNum(),
                            listQ5Project.get(j).getWorkTime());
                    if (projectCost != null && projectCost.getPlanCost() != null
                            && !projectCost.getPlanCost().equals("")) {
                        if (projectCost.getPlanYear().equals(planYea)) {
                            planMonth = Double.parseDouble(projectCost.getPlanMonth());
                        }
                        manhour2 += Double.parseDouble(projectCost.getPlanMonth());
                        if (j == listQ5Project.size() - 1) {
                            if (listQ5Project.get(j).getPeopleCount() != 0) {
                                double cost3 = 0.0;
                                cost3 = Double.parseDouble(projectCost.getCost1())
                                        + Double.parseDouble(projectCost.getCost2())
                                        + Double.parseDouble(projectCost.getLaborCost())
                                        + Double.parseDouble(projectCost.getExpenseReimbursement())
                                        + Double.parseDouble(projectCost.getUnitApportionmentCost());
                                people = Double.parseDouble(df.format(cost3 / listQ5Project.get(j).getPeopleCount()));
                                planCost1 = people * Double.parseDouble(listQ5Project.get(j).getPlanCountMonth());
                                cost1 = cost1 + cost3 + Double.parseDouble(projectCost.getHistoryCost());
                            }
                        } else {
                            double cost2 = 0.0;
                            cost2 = Double.parseDouble(projectCost.getCost1())
                                    + Double.parseDouble(projectCost.getCost2())
                                    + Double.parseDouble(projectCost.getLaborCost())
                                    + Double.parseDouble(projectCost.getExpenseReimbursement())
                                    + Double.parseDouble(projectCost.getUnitApportionmentCost());
                            cost1 += cost2;
                        }
                        planCost += Double.parseDouble(projectCost.getPlanCost());
                    }
                }
            }
            // 获取计划总工时
            if (manhour2 != 0.0) {
                projectAchievements.setManhour2(Double.parseDouble(df.format(manhour1 / manhour2)));
            } else {
                projectAchievements.setManhour2(1.0);
            }
            projectAchievements.setManhour1(workMonth - planMonth);
            // 获取计划成本信息
            Double estimatedCost = listApproval.get(i).getEstimatedCost();
            if (estimatedCost != 0.0) {
                projectAchievements.setCost2(Double.parseDouble(df.format(cost1 / estimatedCost)));
            } else {
                projectAchievements.setCost2(1.0);
            }
            projectAchievements.setCost1(cost1 - planCost);
            // 获取回款时间判断
            List<PlanCost> listPlanCost = planCostMapper.selectByPrimaryKey(listApproval.get(i).getProjectNum());
            for (int j = 0; j < listPlanCost.size(); j++) {
                if (listPlanCost.get(j).getActualTime() != null) {
                    int payMent = DateUtil.getDiffDateDays(listPlanCost.get(j).getPlanTime(),
                            listPlanCost.get(j).getActualTime());
                    projectAchievements.setPayment1(payMent);
                }
                if (listPlanCost.get(j).getActualCost() != null && !listPlanCost.get(j).getActualCost().equals("")
                        && !listPlanCost.get(j).getActualCost().equals("0")) {
                    if (!listPlanCost.get(j).getPlanCost().equals("0")
                            && !listPlanCost.get(j).getPlanCost().trim().equals("")) {
                        actualCost += (Double.parseDouble(listPlanCost.get(j).getActualCost())
                                - Double.parseDouble(listPlanCost.get(j).getPlanCost()));
                        double actual2 = Double
                                .parseDouble(df.format((Double.parseDouble(listPlanCost.get(j).getActualCost())
                                        / Double.parseDouble(listPlanCost.get(j).getPlanCost()))));
                        actual += actual2;
                    }
                    k++;
                }
            }
            // 获取税率
            String taxRate = listApproval.get(i).getTaxRate().replace("%", "");
            if (taxRate.equals("")) {
                taxRate = "0";
            }
            // 获取税后合同金额
            double contract = 0.0;
            double contract1 = 0.0;
            contract1 = (listApproval.get(i).getContractAmount() / (1 + Double.parseDouble(taxRate)));
            if(listApproval.get(i).getLatestEstimatedIncome() != null){
                contract = (listApproval.get(i).getLatestEstimatedIncome() / (1 + Double.parseDouble(taxRate)));
            }else{
                contract = (listApproval.get(i).getContractAmount() / (1 + Double.parseDouble(taxRate)));
            }
            // 获取单元最新公摊系数
            List<ShareCoefficient> listShare = shareCoefficientMapper
                    .selectShareByDeptName(listApproval.get(i).getFinancialIncome());
            double shareCoefficient = 0;
            if (listShare != null && listShare.size() > 0 && listShare.get(0).getValue() != null) {
                shareCoefficient = listShare.get(0).getValue();
            }
            // 计算滚动毛利率
            if (contract > 0) {
                double grossProfit = Double
                        .parseDouble(df.format((contract - (cost1 + planCost1 * (1 + shareCoefficient))) / contract1));
                double targetProfitMargin = Double.parseDouble(listApproval.get(i).getTargetProfitMargin());
                projectAchievements.setGrossProfit1(grossProfit - targetProfitMargin);
            }
            // 获取当前平均结算率
            if (k != 0) {
                double actual1 = Double.parseDouble(df.format(actual / k));
                projectAchievements.setSettlementr2(actual1);
            } else {
                projectAchievements.setSettlementr2(0.0);
            }
            projectAchievements.setSettlementr1(actualCost);
            // 项目得分
            DiagnosticScoreVo diagnosticScoreVo = diagnosticScore(projectAchievements.getProjectNum());
            Double score = diagnosticScoreVo.getScore();
            Double rollingMaoriScore = diagnosticScoreVo.getRollingMaoriScore();
            Double acceptanceProgressScore = diagnosticScoreVo.getAcceptanceProgressScore();
            Double returnProgressScore = diagnosticScoreVo.getReturnProgressScore();
            Double settlementRateScore = diagnosticScoreVo.getSettlementRateScore();
            projectAchievements.setRollingMaoriScore(rollingMaoriScore);
            projectAchievements.setAcceptanceProgressScore(acceptanceProgressScore);
            projectAchievements.setReturnProgressScore(returnProgressScore);
            projectAchievements.setSettlementRateScore(settlementRateScore);
            projectAchievements.setScore(score);
            projectAchievementsMapper.insertSelective(projectAchievements);
        }
    }

    @Override
    public Object getFrossprofit(String projectNum) {
        GrossProfitResultModel grossProfitResultModel = new GrossProfitResultModel();
        List<GrossProfitModel> list = new ArrayList<GrossProfitModel>();
        ApprovalInformation app = approvalInformationMapper.getApprovalByNum(projectNum);
        double purchasingcost=app.getThirdPartyCost();	  //根据项目编号获取第三方采购成本  用来滚动后毛利率的重新计算)
        ProjectImplementation projectImplementation = projectImplementationMapper.selectByPrimaryKey(projectNum);
        String taxRate = app.getTaxRate().replace("%", "");    //
        if (taxRate.equals("")) {
            taxRate = "0";
        }
        // 立项目标毛利率
        double targetProfit = Double.parseDouble(app.getTargetProfitMargin());
        // 立项目标毛利
        double maori = app.getTargetProfit();
        // 税后合同金额
        double contract = 0.0;
        double contract1c= 0.0;
        contract1c = (app.getContractAmount() / (1 + Double.parseDouble(taxRate)));
        if(app.getLatestEstimatedIncome() != null && app.getLatestEstimatedIncome() != 0.0){
            contract = (app.getLatestEstimatedIncome() / (1 + Double.parseDouble(taxRate)));
        }else{
            contract = (app.getContractAmount() / (1 + Double.parseDouble(taxRate)));
        }
        grossProfitResultModel.setTargetProfitMargin(targetProfit);
        List<ProjectCost> listProjectCost = projectCostMapper.selectForGrossprofit(projectNum);
        // 获取已报总工时
        double countHours = q5ProjectMapper.getProjectCountHours(projectNum);
        List<Q5Project> q5List = q5ProjectMapper.selectByPrimaryKey(projectNum);
        // 尚需完工工时
        double overCostHours = app.getCostHours() - countHours;
        // 当前总成本(含公摊,预先设定加上奖金)
        double countCost = 0.0;
        if(projectImplementation != null){
            countCost = projectImplementation.getBonusBalance();
        }
        if (listProjectCost != null && listProjectCost.size() > 0) {
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
            GrossProfitModel grossProfitModel = new GrossProfitModel();
            if(q5List!=null && q5List.size()<3){ //报工小于三个月不计算滚动毛利率
                grossProfitModel.setGrossProfit(0);  //滚动毛利率
                grossProfitModel.setMonth(maxDate);
                grossProfitModel.setDifference(0); //差额
                list.add(grossProfitModel);
                grossProfitResultModel.setList(list);
                grossProfitResultModel.setMaori(maori);
                grossProfitResultModel.setScrollingMaori(0); //滚动毛利
            }else{
                double grossCostCount = countCost + (overCostHours * perCapitaCost * (1 + shareCoefficient)) + purchasingcost;//滚动后成本重新计算（加上 第三方采购的实际的成本话费）
                double grossProfit =(contract1c==0.0) ? 0.0 : ((contract - grossCostCount) / contract1c) * 100;  //滚动后毛利率
                grossProfitModel.setGrossProfit(grossProfit);  //滚动毛利率
                grossProfitModel.setMonth(maxDate);
                grossProfitModel.setDifference(grossProfit - (targetProfit * 100)); //差额
                list.add(grossProfitModel);

                grossProfitResultModel.setList(list);
                grossProfitResultModel.setMaori(maori);
                grossProfitResultModel.setScrollingMaori((list.get(list.size() - 1).getGrossProfit()) / 100 * contract); //滚动毛利
            }
            //CX4GDLTS17020 这个项目有另一套计算规则 只针对于这一个项目，写死
            if("CX4GDLTS17020".equals(projectNum)){
                grossProfitModel.setGrossProfit(7.63);
                grossProfitResultModel.setScrollingMaori(2855414.19); //滚动毛利
                grossProfitModel.setDifference(2.65); //差额
            }
        }
        return grossProfitResultModel;
    }


    public DiagnosticScoreVo diagnosticScore(String projectNum) {
        DiagnosticScoreVo diagnosticScoreVo = new DiagnosticScoreVo();
        // 开始计算项目得分
        // 1.滚动毛利得分
        Double rollingMaoriScore = 0.0;
        GrossProfitResultModel grossProfitResultModel = (GrossProfitResultModel) getFrossprofit(projectNum);
        Double maori = grossProfitResultModel.getMaori();// 立项毛利
        Double scrollingMaori = grossProfitResultModel.getScrollingMaori();// 滚动毛利
        Double M = scrollingMaori / maori;
        if (M > 1) {
            rollingMaoriScore = 70.0;
        } else if (M >= 0.6 && M <= 1) {
            rollingMaoriScore = 70 * M;
        } else {
            rollingMaoriScore = 0.0;
        }
        diagnosticScoreVo.setRollingMaoriScore(rollingMaoriScore);
        // 2.验收进度得分B
        Double acceptanceProgressScore = 0.0;
        List<PlanActual> listPlanActual = planActualMapper.selectByPrimaryKey(projectNum);

        final CopyOnWriteArrayList<PlanActual> cowList = new CopyOnWriteArrayList<PlanActual>(listPlanActual);
        for (PlanActual planActual : cowList) {
            if (null == planActual.getPlanTime() || planActual.getPlanTime().after(new Date())) {
                cowList.remove(planActual);
            }
        }
        int N = cowList.size();
        List<Integer> listDays = new ArrayList<Integer>();
        for (PlanActual planActual : cowList) {
            if (null == planActual.getActualTime()) {
                planActual.setActualTime(new Date());
            }
            int n = DateUtil.getDiffDateDays(planActual.getPlanTime(), planActual.getActualTime());
            if (n <= 0) {
                n = 0;
            } else if (n >= 60) {
                n = 60;
            }
            listDays.add(n);
        }
        for (int i = 0; i < N; i++) {
            acceptanceProgressScore += (60.0 - listDays.get(i)) / 60.0 * (10.0 / N);
        }
        diagnosticScoreVo.setAcceptanceProgressScore(acceptanceProgressScore);
        // 回款进度得分C
        // 结算率得分D
        Double returnProgressScore = 0.0;
        Double settlementRateScore = 0.0;
        List<PlanCost> listPlanCost = planCostMapper.selectByPrimaryKey(projectNum);

        final CopyOnWriteArrayList<PlanCost> costList = new CopyOnWriteArrayList<PlanCost>(listPlanCost);
        for (PlanCost planCost : costList) {
            if (null == planCost.getPlanTime() || planCost.getPlanTime().after(new Date())) {
                costList.remove(planCost);
            }
        }
        int N1 = costList.size();
        List<Integer> listDays1 = new ArrayList<>();
        List<Double> settlementRateList = new ArrayList<Double>();
        for (PlanCost planCost : costList) {
            if (null == planCost.getActualTime()) {
                planCost.setActualTime(new Date());
            }
            int n = DateUtil.getDiffDateDays(planCost.getPlanTime(), planCost.getActualTime());
            if (n <= 0) {
                n = 0;
            } else if (n >= 60) {
                n = 60;
            }
            listDays1.add(n);
            Double settlementRate = 0.0;
            if (StringUtils.isNotBlank(planCost.getSettlementRate())) {
                settlementRate = Double.valueOf(planCost.getSettlementRate());
                if (settlementRate < 0.96) {
                    settlementRate = 0.0;
                } else if (settlementRate > 1) {
                    settlementRate = 1.0;
                }
            } else {
                settlementRate = 0.0;
            }
            settlementRateList.add(settlementRate);
        }
        for (int i = 0; i < N1; i++) {
            returnProgressScore += (60.0 - listDays1.get(i)) / 60.0 * (10.0 / N1);
            settlementRateScore += settlementRateList.get(i) * (10.0 / N1);
        }
        diagnosticScoreVo.setReturnProgressScore(returnProgressScore);
        diagnosticScoreVo.setSettlementRateScore(settlementRateScore);
        // 该项目总得分E
        Double score = 0.0;
        if (rollingMaoriScore == 0.0) {
            score = 0.0;
        } else {
            score = rollingMaoriScore + acceptanceProgressScore + returnProgressScore + settlementRateScore;
        }
        diagnosticScoreVo.setScore(score);
        String projectStatus = "";// 项目状态
        if (score >= 90 && score <= 100) {
            projectStatus = "优秀";
        } else if (score >= 70 && score < 90) {
            projectStatus = "良好";
        } else if (score >= 0 && score < 70) {
            projectStatus = "不达标";
        }
        diagnosticScoreVo.setProjectStatus(projectStatus);
        return diagnosticScoreVo;
    }

    @Override
    public DiagnosticReportVo diagnosticReport(String projectNum) {
        List<Q5Project> q5ProjectList = q5ProjectMapper.selectByPrimaryKey(projectNum);
        DecimalFormat df = new DecimalFormat("######0.00");
        DiagnosticReportVo diagnosticReportVo = new DiagnosticReportVo();
        diagnosticReportVo.setProjectNum(projectNum);
        ApprovalInformation approval = approvalInformationMapper.getApprovalByNum(projectNum);
        String projectName = approval.getProjectName(); // 项目名称
        diagnosticReportVo.setProjectName(projectName);
        int isHandTrans = approval.getIsHandTrans(); //是否手动转立项项目
        DiagnosticScoreVo diagnosticScoreVo = new DiagnosticScoreVo();
        // 开始计算项目得分
        // 1.滚动毛利得分A
        Double rollingMaoriScore = 0.0;
        GrossProfitResultModel grossProfitResultModel = (GrossProfitResultModel) getFrossprofit(projectNum);
        Double maori = grossProfitResultModel.getMaori();// 立项毛利
        Double scrollingMaori = grossProfitResultModel.getScrollingMaori();// 滚动毛利
        Double M = scrollingMaori / maori; //M=（滚动毛利/立项毛利）*100%
        if (M > 1) {  //1、M>100%，A=100分
            rollingMaoriScore = 100.00*0.3;
        } else if (M >= 0.6 && M <= 1) {  //2、60%<=M<=100%，A=100*M分
            rollingMaoriScore = 100 * M*0.3;
        } else { //M<60%，A=0分
            rollingMaoriScore = 0.0;
        }
        if(maori<0){ //当立项毛利为负的情况下，只要滚动毛利低于立项毛利，这个项目的毛利得分就是0分。
            if(maori-scrollingMaori>0){
                rollingMaoriScore=0.0;
            }
        }
        diagnosticScoreVo.setRollingMaoriScore(Double.valueOf(df.format(rollingMaoriScore)));
        // 2.验收进度得分B  验收进度得分B计算规则：=(60-n1)/60*(100/N)+(60-n2)/60*(100/N)+(60-n3)/60*(100/N)
        //N:该项目截止目前有多少个验收里程碑时间节点（不代表所有里程碑）
		/*n1:该项目第一个验收里程碑差异天数
			第一种情况：n1<=0,则n1=0；
			第二种情况：n1>0且n1<60,则n1=n1；
			第三种情况：n1>=60,则n1=60；
			第四种情况：没有实际录入时间，但已超过计划时间，则按照当前日期计算延迟天数；
		 */
        Double acceptanceProgressScore = 0.0;
        List<PlanActual> listPlanActual = planActualMapper.selectByPrimaryKey(projectNum); //获取所有实际验收列表
        final CopyOnWriteArrayList<PlanActual> cowList = new CopyOnWriteArrayList<PlanActual>(listPlanActual);
        for (PlanActual planActual : cowList) {
            if (null == planActual.getPlanTime() || planActual.getPlanTime().after(new Date())) {  //如果计划验收时间大于当前计算时间，则忽略
                cowList.remove(planActual);
            }
        }
        int N = cowList.size();

        List<Integer> listDays = new ArrayList<Integer>();
        if(N==0){  //没有计划时间  或者是 当前还未到里程碑的项目  验收里程碑的满分
            acceptanceProgressScore=100*0.2;
        }else{  //其他情况
            for (PlanActual planActual : cowList) {
                if (null == planActual.getActualTime()) {  //没有实际录入时间，但是已超计划时间，则按照当前日期计算延迟天数
                    planActual.setActualTime(new Date());
                }
                int n = DateUtil.getDiffDateDays(planActual.getPlanTime(), planActual.getActualTime());
                if (n <= 0) { //n1<=0,则n1=0；
                    n = 0;
                } else if (n >= 60) {//n1>0且n1<60,则n1=n1；
                    n = 60;
                }
                listDays.add(n);
            }

            for (int i = 0; i < N; i++) {  //=(60-n1)/60*(100/N)+(60-n2)/60*(100/N)+(60-n3)/60*(100/N)
                acceptanceProgressScore=acceptanceProgressScore+(60.0-listDays.get(i))/60.0*(100.0/N)*0.2;
            }
        }
        diagnosticScoreVo.setAcceptanceProgressScore(Double.valueOf(df.format(acceptanceProgressScore)));
        // 回款进度得分C
		/*
		 * 回款进度得分B计算规则：=(60-n1)/60*(100/N)+(60-n2)/60*(100/N)+(60-n3)/60*(100/N)
			N:该项目截止目前有多少个验收里程碑时间节点（不代表所有里程碑）
			n1:该项目第一个验收里程碑差异天数
			第一种情况：n1<=0,则n1=0；
			第二种情况：n1>0且n1<60,则n1=n1；
			第三种情况：n1>=60,则n1=60；
			第四种情况：没有实际录入时间，但已超过计划时间，则按照当前日期计算延迟天数；
		 */
        Double returnProgressScore = 0.0;
        // 结算率得分D  和徐双确认 不要结算率得分，新增结算额得分 2018.12.19 by wanghuizhen
        List<PlanCost> listPlanCost = planCostMapper.selectByPrimaryKey(projectNum);
        final CopyOnWriteArrayList<PlanCost> costList = new CopyOnWriteArrayList<PlanCost>(listPlanCost);
        for (PlanCost planCost : costList) {
            if (null == planCost.getPlanTime() || planCost.getPlanTime().after(new Date())) {
                costList.remove(planCost);
            }
        }
        int N1 = costList.size();
        List<Integer> listDays1 = new ArrayList<Integer>();
        if(N1==0){
            returnProgressScore=100*0.2;
        }else{
            for (PlanCost planCost : costList) {
                if (null == planCost.getActualTime()) {
                    planCost.setActualTime(new Date());
                }
                int n = DateUtil.getDiffDateDays(planCost.getPlanTime(), planCost.getActualTime());
                if (n <= 0) {
                    n = 0;
                } else if (n >= 60) {
                    n = 60;
                }
                listDays1.add(n);
            }
            for (int i = 0; i < N1; i++) {
                returnProgressScore += (60.0 - listDays1.get(i)) / 60.0 * (100.0 / N1)*0.2;
            }
        }
        diagnosticScoreVo.setReturnProgressScore(Double.valueOf(df.format(returnProgressScore)));
        //结算额得分=D
        /**  公式：
         * D=n1*100
         * n1为该项目结算额占比=最新暂估收入/合同额*100%
         第一种情况：n1<70%,则n1=0；
         第二种情况：70%<=n1<=96%,则n1=n1；
         第三种情况：96%<n1,则n1=100%
         结算额得分只需要计算“结算额预计不达标项目”维度里的项目，“最新暂估收入”、“合同额”从项目详情取数。
         */
        Double settlementScore = 0.0;
        if(approval.getLatestEstimatedIncome() !=null && approval.getLatestEstimatedIncome() >0){ //结算额不达标项目
            Double n1=0.00;
            if(approval.getContractAmount() != null){
                n1 =approval.getLatestEstimatedIncome() /approval.getContractAmount();
            }
            if(n1 <0.7){
                n1=0.00;
            }else if(n1>0.96){
                n1=1.00;
            }
            settlementScore = n1 *100*0.3;
        }else{  //没有这个维度的得分
            settlementScore = 100*0.3;
        }
        diagnosticScoreVo.setSettlementRateScore(settlementScore);
        // 该项目总得分E E=A*30%+B*30%+C*20%+D*20%  毛利率*30%+验收*30%+回款*30%+结算额*20%
        Double score =  rollingMaoriScore  + acceptanceProgressScore + returnProgressScore + settlementScore ;
        diagnosticScoreVo.setScore(Double.valueOf(df.format(score)));
        String projectStatus = "";// 项目状态 设置项目状态等级X：70分以下为不达标，70-90分良好，90-100分
        if (score >= 90 && score <= 100) {
            projectStatus = "优秀";
        } else if (score >= 70 && score < 90) {
            projectStatus = "良好";
        } else if (score >= 0 && score < 70) {
            projectStatus = "不达标";
        }
        diagnosticScoreVo.setProjectStatus(projectStatus);
        List<DiagnosticScoreVo> list = new ArrayList<DiagnosticScoreVo>();
        list.add(diagnosticScoreVo);
        diagnosticReportVo.setList(list);
        ProjectAchievements achievements = projectAchievementsMapper.selectByPrimaryKey(projectNum);
        Double planMonthCount = 0.0;// 当前预算总工时
        Double planCostCount = 0.0;// 当前预算总成本
        double cost1 = 0.0;// 当前累计产生成本
        ProjectImplementation projectImplementation = projectImplementationMapper.selectByPrimaryKey(projectNum);
        if(projectImplementation!=null && projectImplementation.getBonusBalance() != null){
            cost1 += projectImplementation.getBonusBalance();
        }
        double initialPlanCost = approval.getEstimatedCost(); // 初始预算总成本
        double initialPlanMonth = approval.getRollCostHours(); // 初始预算总工时
        List<ProjectCost> listProjectCost = projectCostMapper.selectByNumYear2(projectNum);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        int date1 = Integer.valueOf(format.format(date)); // 当前日期
        for (ProjectCost projectCost : listProjectCost) {
            cost1  += Double.parseDouble(projectCost.getCost1()) + Double.parseDouble(projectCost.getCost2())
                    + Double.parseDouble(projectCost.getLaborCost())
                    + Double.parseDouble(projectCost.getExpenseReimbursement())
                    + Double.parseDouble(projectCost.getUnitApportionmentCost());
            String planYear = projectCost.getPlanYear();
            int planYear1 = 0;
            if (planYear.length() == 5) {
                planYear1 = Integer.valueOf(planYear.substring(0, 4) + "0" + planYear.substring(4, 5));// 把20174格式的月份变成201704
            }else{
                planYear1 = Integer.valueOf(planYear);
            }
            if (planYear1 < date1) {
                if (StringUtils.isNotBlank(projectCost.getPlanCost())) {
                    planCostCount += Double.valueOf(projectCost.getPlanCost());
                }
                if (StringUtils.isNotBlank(projectCost.getPlanMonth())) {
                    planMonthCount += Double.valueOf(projectCost.getPlanMonth());
                }
            }
        }
        Calendar calEnd = Calendar.getInstance();
        int endYea = calEnd.get(Calendar.YEAR);
        int endMon = calEnd.get(Calendar.MONTH);
        if (endMon == 0) {
            endMon = 12;
            endYea = endYea - 1;
        }
        String planYea = endYea + "" + endMon;
        List<Q5Project> listQ5Project = q5ProjectMapper.selectByPrimaryKey(projectNum);
        // 累计产生的工时
        double manhour1 = 0.0;
        // 累计计划工时
        double manhour2 = 0.0;
        // 每月累计实际产生成本含公摊
        double cost11 = 0.0;
        // 每月累计计划成本
        double planCost11 = 0.0;
        // 人均单价
        double people = 0.0;
        // 预计还需人力成本
        double planCost1 = 0.0;
        // 当前结算率总和
        double actual = 0.0;
        // 上月计划工时
        double planMonth = 0.0;
        // 当月实际工时
        double workMonth = 0.0;
        int k = 0;
        if (listQ5Project != null && listQ5Project.size() > 0) {
            for (int j = 0; j < listQ5Project.size(); j++) {
                manhour1 += Double.parseDouble(listQ5Project.get(j).getWorkingHoursCount());
                if (listQ5Project.get(j).getWorkTime().equals(planYea)) {
                    workMonth = Double.parseDouble(listQ5Project.get(j).getWorkingHoursCount());
                }
                ProjectCost projectCost = projectCostMapper.selectByNumYear(listQ5Project.get(j).getProjectNum(),
                        listQ5Project.get(j).getWorkTime());
                if (projectCost != null && projectCost.getPlanCost() != null
                        && !projectCost.getPlanCost().equals("")) {
                    if (projectCost.getPlanYear().equals(planYea)) {
                        planMonth = Double.parseDouble(projectCost.getPlanMonth());
                    }
                    manhour2 += Double.parseDouble(projectCost.getPlanMonth());
                    if (j == listQ5Project.size() - 1) {
                        if (listQ5Project.get(j).getPeopleCount() != 0) {
                            double cost3 = 0.0;
                            cost3 = Double.parseDouble(projectCost.getCost1())
                                    + Double.parseDouble(projectCost.getCost2())
                                    + Double.parseDouble(projectCost.getLaborCost())
                                    + Double.parseDouble(projectCost.getExpenseReimbursement())
                                    + Double.parseDouble(projectCost.getUnitApportionmentCost());
                            people = Double.parseDouble(df.format(cost3 / listQ5Project.get(j).getPeopleCount()));
                            planCost1 = people * Double.parseDouble(listQ5Project.get(j).getPlanCountMonth());
                            cost11 = cost11 + cost3 + Double.parseDouble(projectCost.getHistoryCost());
                        }
                    } else {
                        double cost2 = 0.0;
                        cost2 = Double.parseDouble(projectCost.getCost1())
                                + Double.parseDouble(projectCost.getCost2())
                                + Double.parseDouble(projectCost.getLaborCost())
                                + Double.parseDouble(projectCost.getExpenseReimbursement())
                                + Double.parseDouble(projectCost.getUnitApportionmentCost());
                        cost11 += cost2;
                    }
                    planCost11 += Double.parseDouble(projectCost.getPlanCost());
                }
            }
        }

        // 累计产生的工时
        double manhour = q5ProjectMapper.getProjectCountHours(projectNum);
        // 滚动后预算总工时
        double rollingWorkingHours = approval.getCostHours();
        // 项目工时诊断
        if(isHandTrans!=1){
            if(manhour==0){
                diagnosticReportVo.setManhour(2);
                diagnosticReportVo.setManhourlevel(0);
                diagnosticReportVo.setManhourValue(0.0);
                DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                diagnosticTextVo.setDescribe("该项目没有报工");
                diagnosticTextVo.setConclusion("");
                diagnosticTextVo.setSuggest("");
                diagnosticReportVo.getManhourList().clear();
                diagnosticReportVo.getManhourList().add(diagnosticTextVo);
            }else if((workMonth - planMonth) <= 0 && (manhour / planMonthCount) <= 1) {
                diagnosticReportVo.setManhour(2);
                diagnosticReportVo.setManhourlevel(0);
                diagnosticReportVo.setManhourValue(0.0);
                DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                diagnosticTextVo.setDescribe("月实际投入人月("+df.format(workMonth)+")- 月计划投入人月("+df.format(planMonth)+")=" + df.format(workMonth - planMonth) + "（人月）；"
                        + "当前累计报工总工时("+df.format(manhour)+")/当前预算总工时("+df.format(planMonthCount)+")="+df.format(manhour / planMonthCount * 100)+ "%");
                diagnosticTextVo.setConclusion("月实际投入工时低于月计划投入工时，月工时投入控制较好。当前累计报工总工时低于当前预算总工时，该项目当前工时投入在可控范围内。注意实时监控。");
                diagnosticTextVo.setSuggest("");
                diagnosticReportVo.getManhourList().clear();
                diagnosticReportVo.getManhourList().add(diagnosticTextVo);
            } else {
                diagnosticReportVo.setManhour(1);
                if ((workMonth - planMonth) > 0) {
                    diagnosticReportVo.setManhourlevel(1);
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo.setDescribe("月实际投入人月("+df.format(workMonth)+")- 月计划投入人月("+df.format(planMonth)+")=" + df.format(workMonth - planMonth) + "（人月）；");
                    diagnosticTextVo
                            .setConclusion("月实际投入工时超出月计划投入工时" + df.format(workMonth - planMonth) + "人月，需引起注意。");
                    diagnosticTextVo.setSuggest("项目组要及时调整后续人月投入，保证整体工时投入不超标。");
                    diagnosticReportVo.getManhourList().clear();
                    diagnosticReportVo.getManhourList().add(diagnosticTextVo);
                    diagnosticReportVo.setManhourValue(Double.valueOf(df.format(workMonth - planMonth)));
                }
                if ((manhour / planMonthCount) > 1) {
                    diagnosticReportVo.setManhourlevel(2);
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo.setDescribe("当前累计报工总工时("+df.format(manhour)+")/当前预算总工时("+df.format(planMonthCount)+")=" + df.format(manhour / planMonthCount * 100) + "%");
                    diagnosticTextVo.setConclusion("截至本月累计报工总工时超出截至当月的预算总工时，需引起注意。");
                    diagnosticTextVo.setSuggest("项目组要及时调整后续人月投入，保证整体工时投入不超标。");
                    diagnosticReportVo.getManhourList().clear();
                    diagnosticReportVo.getManhourList().add(diagnosticTextVo);
                    if(planMonthCount > 0){
                        diagnosticReportVo.setManhourValue(Double.valueOf(df.format(manhour / planMonthCount)));
                    }else{
                        diagnosticReportVo.setManhourValue(0.0);
                    }
                }
                if (((rollingWorkingHours - initialPlanMonth) / initialPlanMonth) > 0.15) {
                    diagnosticReportVo.setManhourlevel(3);
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo.setDescribe("（滚动后预算总工时("+df.format(rollingWorkingHours)+")-初始预算总工时("+df.format(initialPlanMonth)+")/初始预算总工时("+df.format(initialPlanMonth)+")="
                            + df.format(((rollingWorkingHours - initialPlanMonth) / initialPlanMonth) * 100) + "%");
                    diagnosticTextVo.setConclusion("滚动后预算总工时超出初始预算总工时的"
                            + df.format(((rollingWorkingHours - initialPlanMonth) / initialPlanMonth) * 100)
                            + "%，该项目后续存在工时投入超支的风险。");
                    diagnosticTextVo.setSuggest("注意及时减少后续人员投入，防范人工成本超支风险。");
                    diagnosticReportVo.getManhourList().clear();
                    diagnosticReportVo.getManhourList().add(diagnosticTextVo);
                    diagnosticReportVo.setManhourValue(Double.valueOf(df.format((rollingWorkingHours - initialPlanMonth) / initialPlanMonth)));
                }
                if ((manhour / initialPlanMonth) <= 1.3 && (manhour / initialPlanMonth) > 1) {
                    diagnosticReportVo.setManhourlevel(4);
                    diagnosticReportVo.getManhourList().clear();
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo
                            .setDescribe("当前累计报工总工时("+df.format(manhour)+" )/ 初始预算总工时("+df.format(initialPlanMonth)+")=" + df.format((manhour / initialPlanMonth) * 100) + "%");
                    diagnosticTextVo.setConclusion("当前累计报工总工时超出初始预算总工时的10%，该项目工时投入已经超支。");
                    diagnosticTextVo.setSuggest("控制人员投入，同时控制业务报销等其他费用。");
                    diagnosticReportVo.getManhourList().add(diagnosticTextVo);
                    diagnosticReportVo.setManhourValue(Double.valueOf(df.format(manhour / initialPlanMonth)));
                }
                if ((manhour / initialPlanMonth) <= 1.5 && (manhour / initialPlanMonth) > 1.3) {
                    diagnosticReportVo.setManhourlevel(5);
                    diagnosticReportVo.getManhourList().clear();
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo
                            .setDescribe("当前累计报工总工时("+df.format(manhour)+" )/ 初始预算总工时("+df.format(initialPlanMonth)+")=" + df.format((manhour / initialPlanMonth) * 100) + "%");
                    diagnosticTextVo.setConclusion("当前累计报工总工时超出初始预算总工时的30%，该项目工时投入已经严重超支，项目面临亏损风险。");
                    diagnosticTextVo.setSuggest("控制人员投入，同时建议项目组召开专题会议，商讨解决方案，并定期向单元负责人报告项目工时成本控制情况，抄送PMO。");
                    diagnosticReportVo.getManhourList().add(diagnosticTextVo);
                    diagnosticReportVo.setManhourValue(Double.valueOf(df.format(manhour / initialPlanMonth)));
                }
                if ((manhour / initialPlanMonth) > 1.5) {
                    diagnosticReportVo.setManhourlevel(6);
                    diagnosticReportVo.getManhourList().clear();
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo
                            .setDescribe("当前累计报工总工时("+df.format(manhour)+" )/ 初始预算总工时("+df.format(initialPlanMonth)+")=" + df.format((manhour / initialPlanMonth) * 100) + "%");
                    diagnosticTextVo.setConclusion("当前累计报工总工时超出初始预算总工时的50%，该项目工时投入已经严重超支，项目面临亏损风险。");
                    diagnosticTextVo.setSuggest("控制人员投入，同时建议项目组召开专题会议，商讨解决方案，并定期向单元负责人报告项目工时成本控制情况，抄送PMO。");
                    diagnosticReportVo.getManhourList().add(diagnosticTextVo);
                    if(StringUtils.isNumeric((manhour / initialPlanMonth)+"")){
                        diagnosticReportVo.setManhourValue(Double.valueOf(df.format(manhour / initialPlanMonth)));
                    }else{
                        diagnosticReportVo.setManhourValue(0.0);
                    }
                }
            }
        }else{
            diagnosticReportVo.setManhour(2);
            diagnosticReportVo.setManhourlevel(0);
            diagnosticReportVo.setManhourValue(0.0);
            DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
            diagnosticTextVo.setDescribe("该项目工时诊断正常");
            diagnosticTextVo.setConclusion("该项目工时诊断正常");
            diagnosticTextVo.setSuggest("该项目工时诊断正常");
            diagnosticReportVo.getManhourList().clear();
            diagnosticReportVo.getManhourList().add(diagnosticTextVo);
        }
        Calendar Date = Calendar.getInstance();
        int year = Date.get(Calendar.YEAR);
        int month = Date.get(Calendar.MONTH);
        String monthNow=DateUtil.getStandardYearAndMonth(month,year);
        Double PlanCost=0.0;
        Double ActualCost=0.0;
        ProjectCost projectCost=projectCostMapper.selectByNumYear(projectNum, monthNow);
        if(null!=projectCost){
            PlanCost=Double.valueOf("".equals(projectCost.getPlanCost())? "0" : projectCost.getPlanCost()); //月计划成本
            ActualCost = Double.parseDouble(projectCost.getCost1())  //月实际成本
                    + Double.parseDouble(projectCost.getCost2())
                    + Double.parseDouble(projectCost.getLaborCost())
                    + Double.parseDouble(projectCost.getExpenseReimbursement())
                    + Double.parseDouble(projectCost.getUnitApportionmentCost());
        }
        // 项目成本诊断
        if(listProjectCost.size()==0){
            diagnosticReportVo.setCost(2);
            diagnosticReportVo.setCostlevel(0);
            DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
            diagnosticTextVo.setDescribe("该项目没有成本信息");
            diagnosticTextVo.setConclusion("");
            diagnosticTextVo.setSuggest("");
            diagnosticReportVo.getCostList().clear();
            diagnosticReportVo.getCostList().add(diagnosticTextVo);
            diagnosticReportVo.setCostValue(0.0);
        }else if(planCostCount==0){
            diagnosticReportVo.setCost(2);
            diagnosticReportVo.setCostlevel(0);
            DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
            diagnosticTextVo.setDescribe("当前预算总成本为0");
            diagnosticTextVo.setConclusion("系统无法判断预警级别");
            diagnosticTextVo.setSuggest("联系管理员查看是否此项目是否没有录入当前预算总成本");
            diagnosticReportVo.getCostList().clear();
            diagnosticReportVo.getCostList().add(diagnosticTextVo);
            diagnosticReportVo.setCostValue(0.0);
        }else{
            if (achievements.getCost1() <= 0 && cost1 / planCostCount <= 1) {
                diagnosticReportVo.setCost(2);
                diagnosticReportVo.setCostlevel(0);
                DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                diagnosticTextVo.setDescribe("月实际成本("+df.format(ActualCost)+") - 月计划投入成本 ("+df.format(PlanCost)+")=" + df.format(ActualCost - PlanCost) + "元;"
                        + "当前累计成本("+df.format(cost1)+")/当前预算成本("+df.format(planCostCount)+")=" + df.format(cost1 / planCostCount * 100) + "%");
                diagnosticTextVo.setConclusion("月实际成本低于月计划投入成本，月成本控制较好。当前累计成本低于当前预算成本，该项目当前成本在可控范围内。注意实时监控。");
                diagnosticTextVo.setSuggest("");
                diagnosticReportVo.getCostList().clear();
                diagnosticReportVo.getCostList().add(diagnosticTextVo);
                diagnosticReportVo.setCostValue(0.0);
            }
            else if(ActualCost-PlanCost<=0){
                diagnosticReportVo.setCost(2);
                diagnosticReportVo.setCostlevel(0);
                DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                diagnosticTextVo.setDescribe("月实际成本("+df.format(ActualCost)+") - 月计划投入成本 ("+df.format(PlanCost)+")=" + df.format(ActualCost - PlanCost) + "元;");
                diagnosticTextVo.setSuggest("");
                diagnosticReportVo.getCostList().clear();
                diagnosticReportVo.getCostList().add(diagnosticTextVo);
                diagnosticReportVo.setCostValue(0.0);
            }
            else {
                diagnosticReportVo.setCost(1);
                if (achievements.getCost1() > 0) {
                    diagnosticReportVo.setCostlevel(1);
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo.setDescribe("月实际成本("+df.format(ActualCost)+") - 月计划投入成本 ("+df.format(PlanCost)+")=" + df.format(ActualCost - PlanCost) + "元;");
                    diagnosticTextVo.setConclusion("月实际成本超出月计划投入成本" + df.format(ActualCost - PlanCost) + "元，需引起注意。");
                    diagnosticTextVo.setSuggest("项目组要及时调整后续成本计划，保证整体成本不超标。");
                    diagnosticReportVo.setCostValue(Double.valueOf(df.format(ActualCost - PlanCost)));
                    diagnosticReportVo.getCostList().clear();
                    diagnosticReportVo.getCostList().add(diagnosticTextVo);
                }

                if (cost1 / planCostCount > 1) {
                    diagnosticReportVo.setCostlevel(2);
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo.setDescribe("当前累计成本("+df.format(cost1)+")/当前预算成本("+df.format(planCostCount)+")=" + df.format(cost1 / planCostCount * 100) + "%");
                    diagnosticTextVo.setConclusion("截至本月累计成本超出截至当月的预算成本，该项目后续存在成本超支的风险。");
                    diagnosticTextVo.setSuggest("项目组要及时调整后续成本计划，控制人员投入和业务报销等其他费用，防范成本超支风险。");
                    diagnosticReportVo.getCostList().clear();
                    diagnosticReportVo.getCostList().add(diagnosticTextVo);
                    diagnosticReportVo.setCostValue(Double.valueOf(df.format(cost1 / planCostCount)));
                }
                if ((cost1 / initialPlanCost) > 1 && (cost1 / initialPlanCost) <= 1.3) {
                    diagnosticReportVo.setCostlevel(3);
                    diagnosticReportVo.getCostList().clear();
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo.setDescribe("当前累计成本("+df.format(cost1)+")/项目预算总成本("+df.format(initialPlanCost)+")=" + df.format(cost1 / initialPlanCost * 100) + "%");
                    diagnosticTextVo.setConclusion("当前累计成本超出项目预算总成本的10%，该项目成本已经超支。");
                    diagnosticTextVo.setSuggest("控制人员投入和业务报销等其他费用，防范项目成本严重超支。");
                    diagnosticReportVo.getCostList().add(diagnosticTextVo);
                    diagnosticReportVo.setCostValue(Double.valueOf(df.format(cost1 / initialPlanCost)));
                }
                if ((cost1 / initialPlanCost) > 1.3 && (cost1 / initialPlanCost) <= 1.5) {
                    diagnosticReportVo.setCostlevel(4);
                    diagnosticReportVo.getCostList().clear();
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo.setDescribe("当前累计成本("+df.format(cost1)+")/项目预算总成本("+df.format(initialPlanCost)+")=" + df.format(cost1 / initialPlanCost * 100) + "%");
                    diagnosticTextVo.setConclusion("当前累计成本超出项目预算总成本的30%，该项目成本已经严重超支，项目面临亏损风险。");
                    diagnosticTextVo.setSuggest("控制人员投入和业务报销等其他费用，同时建议项目组召开专题会议，商讨解决方案，并定期向单元负责人报告项目成本控制情况，抄送PMO。");
                    diagnosticReportVo.getCostList().add(diagnosticTextVo);
                    diagnosticReportVo.setCostValue(Double.valueOf(df.format(cost1 / initialPlanCost)));
                }
                if ((cost1 / initialPlanCost) > 1.5) {
                    diagnosticReportVo.setCostlevel(5);
                    diagnosticReportVo.getCostList().clear();
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo.setDescribe("当前累计成本("+df.format(cost1)+")/项目预算总成本("+df.format(initialPlanCost)+")=" + df.format(cost1 / initialPlanCost * 100) + "%");
                    diagnosticTextVo.setConclusion("当前累计成本超出项目预算总成本的50%，该项目成本已经严重超支，项目面临亏损风险。");
                    diagnosticTextVo.setSuggest("控制人员投入和业务报销等其他费用，同时建议项目组召开专题会议，商讨解决方案，并定期向单元负责人、公司领导报告项目成本控制情况，抄送PMO。");
                    diagnosticReportVo.getCostList().add(diagnosticTextVo);
                    diagnosticReportVo.setCostValue(Double.valueOf(df.format(cost1 / initialPlanCost)));
                }
            }
        }

        // 计算滚动后毛利率
        Double grossProfitMargin=0.0;
        if(grossProfitResultModel!=null && grossProfitResultModel.getList()!=null && grossProfitResultModel.getList().size()>0){
            grossProfitMargin =grossProfitResultModel.getList().get(0).getGrossProfit()/100;
        }

        //立项目标毛利率
        Double targetProfitMargin=0.0;
        if(grossProfitResultModel!=null ){
            targetProfitMargin=grossProfitResultModel.getTargetProfitMargin();
        }

        //滚动后毛利率与立项毛利率差值
        Double difference=0.0;
        if(grossProfitResultModel!=null && grossProfitResultModel.getList()!=null && grossProfitResultModel.getList().size()>0){
            difference=grossProfitResultModel.getList().get(0).getDifference()/100;
        }

        // 滚动后毛利率诊断
        if(isHandTrans!=1 && q5ProjectList!=null && q5ProjectList.size()>=3){   //非手动转立项    并且  报工要大于3个月
            if (difference >= 0) {
                diagnosticReportVo.setGrossprofit(2);
                diagnosticReportVo.setGrossprofitlevel(0);
                DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                diagnosticTextVo.setDescribe("月滚动后毛利率("+df.format(grossProfitMargin*100)+"%) - 立项毛利率("+df.format(targetProfitMargin*100)+"%)=" + df.format(difference*100)+"%");
                diagnosticTextVo.setConclusion("月滚动后毛利率大于或等于立项毛利率，该项目当前毛利率达标。");
                diagnosticTextVo.setSuggest("");
                diagnosticReportVo.getGrossprofitList().add(diagnosticTextVo);
                diagnosticReportVo.setGrossprofitValue(Double.valueOf(df.format(0.0)));
            } else {
                diagnosticReportVo.setGrossprofit(1);
                if (grossProfitMargin < 0.25) {
                    diagnosticReportVo.setGrossprofitlevel(1);
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo.setDescribe("滚动后毛利率=" + df.format(grossProfitMargin*100)+"%");
                    diagnosticTextVo.setConclusion("项目滚动后毛利率低于25%（一单元低于28%），低于2018年业务单元标准要求，需引起注意。");
                    diagnosticTextVo.setSuggest("项目组从成本和进度计划方面优化项目执行方案，通过控制后期投入和进度管理等方法提高毛利。");
                    diagnosticReportVo.getGrossprofitList().clear();
                    diagnosticReportVo.getGrossprofitList().add(diagnosticTextVo);
                    diagnosticReportVo.setGrossprofitValue(Double.valueOf(df.format(grossProfitMargin)));
                }
                if (difference < 0) {
                    diagnosticReportVo.setGrossprofitlevel(2);
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo.setDescribe("滚动后毛利率("+df.format(grossProfitMargin*100)+"%) - 立项毛利率("+df.format(targetProfitMargin*100)+"%)=" + df.format(difference*100)+"%");
                    diagnosticTextVo.setConclusion("滚动后毛利率低于立项毛利率，该项目后续存在毛利不达标的风险。");
                    diagnosticTextVo.setSuggest("项目组从成本和进度计划方面优化项目执行方案，通过控制后期投入和进度管理等方法提高毛利。");
                    diagnosticReportVo.getGrossprofitList().clear();
                    diagnosticReportVo.getGrossprofitList().add(diagnosticTextVo);
                    diagnosticReportVo.setGrossprofitValue(Double.valueOf(df.format(difference)));
                }
                if (((maori - scrollingMaori) / maori) >= 0.15 && ((maori - scrollingMaori) / maori) < 0.3) {
                    diagnosticReportVo.setGrossprofitlevel(3);
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo.setDescribe("（立项毛利("+df.format(maori)+")-滚动后毛利("+df.format(scrollingMaori)+")）/立项毛利("+df.format(maori)+")=" + df.format((maori - scrollingMaori) / maori*100)+"%");
                    diagnosticTextVo.setConclusion("滚动后毛利低于立项毛利的85%，项目毛利不达标的风险较大。");
                    diagnosticTextVo
                            .setSuggest("项目组需引起重视，从成本和进度计划方面优化项目执行方案，通过控制后期投入和进度管理等方法提高毛利，并定期向单元负责人报告项目成本控制情况，抄送PMO。");
                    diagnosticReportVo.getGrossprofitList().clear();
                    diagnosticReportVo.getGrossprofitList().add(diagnosticTextVo);
                    diagnosticReportVo.setGrossprofitValue(Double.valueOf(df.format((maori - scrollingMaori) / maori)));
                }
                if (((maori - scrollingMaori) / maori) >= 0.3 && ((maori - scrollingMaori) / maori) < 0.5) {
                    diagnosticReportVo.setGrossprofitlevel(4);
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo.setDescribe("（立项毛利("+df.format(maori)+")-滚动后毛利("+df.format(scrollingMaori)+")）/立项毛利("+df.format(maori)+")=" + df.format((maori - scrollingMaori) / maori*100)+"%");
                    diagnosticTextVo.setConclusion("滚动后毛利低于立项毛利的70%，预计项目毛利不达标，项目面临亏损风险。");
                    diagnosticTextVo
                            .setSuggest("项目组需引起重视，从成本和进度计划方面优化项目执行方案，通过控制后期投入和进度管理等方法提高毛利，并定期向单元负责人报告项目成本控制情况，抄送PMO。");
                    diagnosticReportVo.getGrossprofitList().clear();
                    diagnosticReportVo.getGrossprofitList().add(diagnosticTextVo);
                    diagnosticReportVo.setGrossprofitValue(Double.valueOf(df.format((maori - scrollingMaori) / maori)));
                }
                if (((maori - scrollingMaori) / maori) >= 0.5 && ((maori - scrollingMaori) / maori) < 1) {
                    diagnosticReportVo.setGrossprofitlevel(5);
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo.setDescribe("（立项毛利("+df.format(maori)+")-滚动后毛利("+df.format(scrollingMaori)+")）/立项毛利("+df.format(maori)+")=" + df.format((maori - scrollingMaori) / maori*100)+"%");
                    diagnosticTextVo.setConclusion("滚动后毛利低于立项毛利的50%，预计项目毛利严重不达标，项目面临亏损风险。");
                    diagnosticTextVo.setSuggest("项目组召开专题会议，提出后续建议，并定期向单元负责人、公司领导报告进展情况，抄送PMO。");
                    diagnosticReportVo.getGrossprofitList().clear();
                    diagnosticReportVo.getGrossprofitList().add(diagnosticTextVo);
                    diagnosticReportVo.setGrossprofitValue(Double.valueOf(df.format((maori - scrollingMaori) / maori)));
                }
                if (((maori - scrollingMaori) / maori) >= 1) {
                    diagnosticReportVo.setGrossprofitlevel(5);
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo.setDescribe("（立项毛利("+df.format(maori)+")-滚动后毛利("+df.format(scrollingMaori)+")）/立项毛利("+df.format(maori)+")=" + df.format((maori - scrollingMaori) / maori*100)+"%");
                    diagnosticTextVo.setConclusion("滚动后毛利小于或等于0，预计项目毛利严重不达标，项目将严重亏损。");
                    diagnosticTextVo.setSuggest("项目组召开专题会议，提出后续建议，并定期向单元负责人、公司领导报告进展情况，抄送PMO。");
                    diagnosticReportVo.getGrossprofitList().clear();
                    diagnosticReportVo.getGrossprofitList().add(diagnosticTextVo);
                    diagnosticReportVo.setGrossprofitValue(Double.valueOf(df.format((maori - scrollingMaori) / maori)));
                }
            }

        }else{ // 手动转立项的项目     项目报工小于3个月的项目  滚动毛利率不计算，设置为正常 亮绿灯
            diagnosticReportVo.setGrossprofit(2);
            diagnosticReportVo.setGrossprofitlevel(0);
            DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
            diagnosticTextVo.setDescribe("项目报工小于3个月,月滚动后毛利率正常");
            diagnosticTextVo.setConclusion("项目报工小于3个月");
            diagnosticTextVo.setSuggest("月滚动后毛利率正常");
            diagnosticReportVo.getGrossprofitList().add(diagnosticTextVo);
            diagnosticReportVo.setGrossprofitValue(Double.valueOf(df.format(0.0)));
        }
        // 进度预警 和 回款预警
        try {
            DiagnosticReportVo diagnosticReportVoTemp = (DiagnosticReportVo)diagnosticSpeedReport(projectNum,diagnosticReportVo);
            BeanUtils.copyProperties(diagnosticReportVo, diagnosticReportVoTemp);
            DiagnosticReportVo diagnosticReportVoPayMent = (DiagnosticReportVo)diagnosticPayMentReport(projectNum,diagnosticReportVo);
            BeanUtils.copyProperties(diagnosticReportVo, diagnosticReportVoPayMent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 结算率预警
        // 实际结算率
        double radio = 0.0;
        for (int j = 0; j < listPlanCost.size(); j++) {
            if (listPlanCost.get(j).getActualCost() != null && !listPlanCost.get(j).getActualCost().equals("")
                    && !listPlanCost.get(j).getActualCost().equals("0")) {
                if (!listPlanCost.get(j).getPlanCost().equals("0") && !listPlanCost.get(j).getPlanCost().trim().equals("")) {
                    double actualCost = 0.0;
                    actualCost += Double.parseDouble(listPlanCost.get(j).getActualCost());
                    double planCost = 0.0;
                    planCost += Double.parseDouble(listPlanCost.get(j).getPlanCost());
                    radio = actualCost / planCost;
                }

            }
        }

        DecimalFormat dfs = new DecimalFormat("#0.0000");  //保留4位小数
        double a=Math.abs((radio-1)*100);  //解决 -0.00 问题
        a=Math.round(a); //四舍五入
        String a1 = dfs.format(a);
        String format2 = dfs.format(radio);
        if(radio==0){
            diagnosticReportVo.setSettlementr(2);
            diagnosticReportVo.setSettlementrlevel(0);
            DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
            diagnosticTextVo.setDescribe("该项目没有结算");
            diagnosticTextVo.setConclusion("该项目实际结算率为0");
            diagnosticTextVo.setSuggest("");
            diagnosticReportVo.getSettlementrList().clear();
            diagnosticReportVo.getSettlementrList().add(diagnosticTextVo);
            diagnosticReportVo.setSettlementrValue(Double.valueOf(df.format(0.0)));
        }else if ((Double.parseDouble(format2) - 1) >= 0) {
            diagnosticReportVo.setSettlementr(2);
            diagnosticReportVo.setSettlementrlevel(0);
            DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
            if("0.0000".equals(a1)){
                diagnosticTextVo.setDescribe("里程碑实际结算率("+df.format(radio*100)+"%) - 里程碑预计结算率 (100%)=" + "0.00%");
            }else{
                diagnosticTextVo.setDescribe("里程碑实际结算率("+df.format(radio*100)+"%) - 里程碑预计结算率 (100%)=" + df.format((radio - 1) * 100) + "%");
            }
            diagnosticTextVo.setConclusion("该项目里程碑结算率达标。");
            diagnosticTextVo.setSuggest("");
            diagnosticReportVo.getSettlementrList().clear();
            diagnosticReportVo.getSettlementrList().add(diagnosticTextVo);
            diagnosticReportVo.setSettlementrValue(Double.valueOf(df.format(0.0)));
        } else {
            diagnosticReportVo.setSettlementr(1);
            if ((Double.parseDouble(format2)  - 1) < 0) {
                diagnosticReportVo.setSettlementrlevel(1);
                DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                diagnosticTextVo.setDescribe("里程碑实际结算率("+df.format(radio*100)+"%) - 里程碑预计结算率 (100%)=" + df.format((radio - 1) * 100) + "%");
                diagnosticTextVo.setConclusion("里程碑实际结算率低于里程碑预计结算率，该项目结算不理想。");
                diagnosticTextVo.setSuggest("项目组加强考核管理和客户满意度提升，保证项目整体结算率。");
                diagnosticReportVo.getSettlementrList().clear();
                diagnosticReportVo.getSettlementrList().add(diagnosticTextVo);
                diagnosticReportVo.setSettlementrValue(Double.valueOf(df.format(radio - 1)));
            }
            if (radio >= 0.9 && radio < 0.96) {
                diagnosticReportVo.setSettlementrlevel(2);
                DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                diagnosticTextVo.setDescribe("里程碑实际结算率("+df.format(radio*100)+"%) /里程碑预计结算率 (100%)=" + df.format(radio * 100) + "%");
                diagnosticTextVo.setConclusion("里程碑实际结算率低于96%，该项目结算低于2018年业务单元标准要求。");
                diagnosticTextVo.setSuggest("项目组加强考核管理和客户满意度提升，保证项目整体结算率。");
                diagnosticReportVo.getSettlementrList().clear();
                diagnosticReportVo.getSettlementrList().add(diagnosticTextVo);
                diagnosticReportVo.setSettlementrValue(Double.valueOf(df.format(radio)));
            }
            if (radio >= 0.6 && radio < 0.9) {
                diagnosticReportVo.setSettlementrlevel(3);
                DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                diagnosticTextVo.setDescribe("里程碑实际结算率("+df.format(radio*100)+"%) /里程碑预计结算率 (100%)=" + df.format(radio * 100) + "%");
                diagnosticTextVo.setConclusion("里程碑实际结算率低于里程碑预计结算率的90%，该项目结算不理想。");
                diagnosticTextVo.setSuggest("项目组需引起重视，从考核管理和客户满意度等方面优化项目执行方案，保证项目整体结算率，并定期向单元负责人报告项目结算情况，抄送PMO、财务。");
                diagnosticReportVo.getSettlementrList().clear();
                diagnosticReportVo.getSettlementrList().add(diagnosticTextVo);
                diagnosticReportVo.setSettlementrValue(Double.valueOf(df.format(radio)));
            }
            if (radio >= 0.3 && radio < 0.6) {
                diagnosticReportVo.setSettlementrlevel(4);
                DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                diagnosticTextVo.setDescribe("里程碑实际结算率("+df.format(radio*100)+"%)/里程碑预计结算率 (100%)=" + df.format(radio * 100) + "%");
                diagnosticTextVo.setConclusion("里程碑实际结算率低于里程碑预计结算率的60%，该项目结算情况较差。");
                diagnosticTextVo.setSuggest("项目组召开专题会议，提出后续建议，并定期向单元负责人、公司领导报告进展情况，抄送PMO、财务。");
                diagnosticReportVo.getSettlementrList().clear();
                diagnosticReportVo.getSettlementrList().add(diagnosticTextVo);
                diagnosticReportVo.setSettlementrValue(Double.valueOf(df.format(radio)));
            }
            if (radio < 0.3) {
                diagnosticReportVo.setSettlementrlevel(5);
                DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                diagnosticTextVo.setDescribe("里程碑实际结算率("+df.format(radio*100)+"%) /里程碑预计结算率 (100%)=" + df.format(radio * 100) + "%");
                diagnosticTextVo.setConclusion("里程碑实际结算率低于里程碑预计结算率的30%，该项目结算情况极差。");
                diagnosticTextVo.setSuggest("项目组召开专题会议，提出后续建议，并定期向单元负责人、公司领导报告进展情况，抄送PMO、财务。");
                diagnosticReportVo.getSettlementrList().clear();
                diagnosticReportVo.getSettlementrList().add(diagnosticTextVo);
                diagnosticReportVo.setSettlementrValue(Double.valueOf(df.format(radio)));
            }
        }
        return diagnosticReportVo;
    }

    @Override
    public Object diagnosticSpeedReport(String projectNum,DiagnosticReportVo diagnosticReportVo) {
        ApprovalInformation approvalInformation = approvalInformationMapper.getApprovalByNum(projectNum);
        int isHandTrans = approvalInformation.getIsHandTrans();
        SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM-dd");
        int speed = 0;                                                  //定义一个时间差，接收里程碑计划时间与当前时间的差值
        int speed1=0;
        String flag="speed";
        Date planTime = null;
        Date actualTime = null;
        String isEndRecord=null;
        Date planTimeAfterUpdate=null;
        Boolean hasPlanActual=true;
        List<PlanActual> listPlanActual1 = planActualMapper.selectByPrimaryKey(projectNum);
        Date nowDate=new Date();                                        //当前时间

        for(int i = 0; i < listPlanActual1.size(); i++) {
            planTime=listPlanActual1.get(i).getPlanTime();
            actualTime=listPlanActual1.get(i).getActualTime();
            planTimeAfterUpdate=listPlanActual1.get(i).getPlantimeAfterUptd();
            if(planTime != null && actualTime == null && planTimeAfterUpdate==null){   // 无里程碑实际时间有里程碑计划时间
                speed = DateUtil.getDiffDateDays(planTime, nowDate);              // nowDate - planTime
                speed1= Math.abs(speed);                                        // 天数差的绝对值
                break;
            }else if(planTimeAfterUpdate!=null && actualTime == null){
                speed = DateUtil.getDiffDateDays(planTimeAfterUpdate, nowDate);    // nowDate - planTime
                speed1= Math.abs(speed);                                      // 天数差的绝对值
                break;
            }else if((planTime != null || planTimeAfterUpdate!=null) && actualTime!=null && i==listPlanActual1.size()-1){ //最后一个里程碑既有计划时间又有实际时间
                speed = DateUtil.getDiffDateDays(planTime==null?planTimeAfterUpdate:planTime, actualTime);    // nowDate - planTime
                speed1= Math.abs(speed);                                     // 天数差的绝对值
                if(actualTime.getTime()<=new Date().getTime()){              //实际时间，并且实际时间已经过去
                    speed=-1;
                    speed1=-1;
                }
                isEndRecord="1";
                break;
            }
            else if(planTime==null && planTimeAfterUpdate==null){
                speed=0;   //诊断设置为正常 绿灯
                speed1=31; //不计算预警
            }
        }

        if(listPlanActual1==null || listPlanActual1.size()==0){
            hasPlanActual=false;
        }
        if(isHandTrans!=1){
            if (speed <= 0) { 												 // 当前时间  - 计划时间  <= 0
                diagnosticReportVo.setSpeed(2);
                diagnosticReportVo.setSpeedlevel("0");
                DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                diagnosticTextVo = getHandleDiagnosticReportVo(diagnosticTextVo, planTime, planTimeAfterUpdate, actualTime, isEndRecord, speed,flag,hasPlanActual);
                if(hasPlanActual==true){
                    diagnosticTextVo.setConclusion("该项目还未填写里程碑，当前考核进度正常。");
                }else{
                    diagnosticTextVo.setConclusion("里程碑的实际考核时间("+tf.format(nowDate)+")，计算时间在计划考核时间之前，该项目当前考核进度正常。");
                }
                diagnosticTextVo.setSuggest("");

                if (speed1 <= 30 && actualTime==null && hasPlanActual==true) { //30天提前提醒
                    diagnosticReportVo.setSpeed(3);
                    diagnosticReportVo.setSpeedlevel("-1");
                    diagnosticTextVo.setDescribe("里程碑的计划考核时间，提前30天开始亮黄灯");
                    diagnosticTextVo.setConclusion("");
                    diagnosticTextVo.setSuggest("距离里程碑的计划考核时间还有30天，请项目组注意把控进度，务必按照计划时间完成考核。");
                }
                if(speed1==-1 && actualTime!=null && hasPlanActual==true){
                    diagnosticTextVo.setDescribe("当前进度正常");
                    diagnosticTextVo.setConclusion("已验收");
                    diagnosticTextVo.setSuggest("已验收");
                }
                diagnosticReportVo.getSpeedList().add(diagnosticTextVo);
                diagnosticReportVo.setSpeedValue(0.0);
            }
            else {                                                        // 当前时间  - 计划时间  > 0  当前时间已经超过计划时间，实际时间还是没有填   异常的情况
                diagnosticReportVo.setSpeed(1);
                if (speed1 > 0 && speed1 <= 30) {
                    diagnosticReportVo.setSpeedlevel("1");
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo = getHandleDiagnosticReportVo(diagnosticTextVo, planTime, planTimeAfterUpdate, actualTime, isEndRecord, speed,flag,hasPlanActual);
                    diagnosticTextVo.setConclusion("里程碑实际考核时间比里程碑计划考核时间延迟（1个月内）。");
                    diagnosticTextVo.setSuggest("项目组加强进度管理，防范项目成本风险。");
                    diagnosticReportVo.getSpeedList().clear();
                    diagnosticReportVo.getSpeedList().add(diagnosticTextVo);
                    diagnosticReportVo.setSpeedValue(Double.valueOf(speed));
                }
                if (speed1 > 30 && speed1 <= 60) {
                    diagnosticReportVo.setSpeedlevel("2");
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo = getHandleDiagnosticReportVo(diagnosticTextVo, planTime, planTimeAfterUpdate, actualTime, isEndRecord, speed,flag,hasPlanActual);
                    diagnosticTextVo.setConclusion("里程碑实际考核时间比里程碑计划考核时间延迟1个月以上，项目面临成本风险。");
                    diagnosticTextVo.setSuggest("项目组加强进度管理，防范项目成本风险。");
                    diagnosticReportVo.getSpeedList().clear();
                    diagnosticReportVo.getSpeedList().add(diagnosticTextVo);
                    diagnosticReportVo.setSpeedValue(Double.valueOf(speed));
                }
                if (speed1 > 60 && speed1 <= 180) {
                    diagnosticReportVo.setSpeedlevel("3");
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo = getHandleDiagnosticReportVo(diagnosticTextVo, planTime, planTimeAfterUpdate, actualTime, isEndRecord, speed,flag,hasPlanActual);
                    diagnosticTextVo.setConclusion("里程碑实际考核时间比里程碑计划考核时间延迟2个月以上，项目面临成本风险。");
                    diagnosticTextVo.setSuggest("项目组加强进度管理，防范项目成本风险，并定期向单元负责人报告项目进度控制情况，抄送PMO。");
                    diagnosticReportVo.getSpeedList().clear();
                    diagnosticReportVo.getSpeedList().add(diagnosticTextVo);
                    diagnosticReportVo.setSpeedValue(Double.valueOf(speed));
                }
                if (speed1 > 180 && speed1 <= 360) {
                    diagnosticReportVo.setSpeedlevel("4");
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo = getHandleDiagnosticReportVo(diagnosticTextVo, planTime, planTimeAfterUpdate, actualTime, isEndRecord, speed,flag,hasPlanActual);
                    diagnosticTextVo.setConclusion("里程碑实际考核时间比里程碑计划考核时间延迟6个月以上，该项目考核进度已经严重延期，同时面临较大的成本风险。");
                    diagnosticTextVo.setSuggest("项目组召开专题会议，提出后续建议，并定期向单元负责人、公司领导报告进展情况，抄送PMO。");
                    diagnosticReportVo.getSpeedList().clear();
                    diagnosticReportVo.getSpeedList().add(diagnosticTextVo);
                    diagnosticReportVo.setSpeedValue(Double.valueOf(speed));
                }
                if (speed1 > 360) {
                    diagnosticReportVo.setSpeedlevel("5");
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo = getHandleDiagnosticReportVo(diagnosticTextVo, planTime, planTimeAfterUpdate, actualTime, isEndRecord, speed,flag,hasPlanActual);
                    diagnosticTextVo.setConclusion("里程碑实际考核时间比里程碑计划考核时间延迟1年以上，该项目考核进度已经严重延期，处于失控状态，项目面临亏损风险。");
                    diagnosticTextVo.setSuggest("项目组召开专题会议，提出后续建议，并定期向单元负责人、公司领导报告进展情况，抄送PMO。");
                    diagnosticReportVo.getSpeedList().clear();
                    diagnosticReportVo.getSpeedList().add(diagnosticTextVo);
                    diagnosticReportVo.setSpeedValue(Double.valueOf(speed));
                }
            }
        }else{
            diagnosticReportVo.setSpeed(2);
            diagnosticReportVo.setSpeedlevel("0");
            DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
            diagnosticTextVo.setDescribe("该项目进度诊断正常");
            diagnosticTextVo.setConclusion("该项目进度诊断正常");
            diagnosticTextVo.setSuggest("该项目进度诊断正常");
            diagnosticReportVo.getSpeedList().add(diagnosticTextVo);
            diagnosticReportVo.setSpeedValue(0.0);
        }
        return diagnosticReportVo;
    }

    @Override
    public Object diagnosticPayMentReport(String projectNum,DiagnosticReportVo diagnosticReportVo) {
        ApprovalInformation approvalInformation = approvalInformationMapper.getApprovalByNum(projectNum);
        int isHandTrans = approvalInformation.getIsHandTrans();
        Date nowDate=new Date();
        int payMent = 0;
        int payMent1 = 0;
        Date planTime = null;//里程碑计划时间
        Date actualTime = null;//里程碑实际时间
        Date planTimeAfterupd=null;
        String isLastRecord=null;
        Boolean hasPlanCost=true;
        String flag="payment";
        List<PlanCost> listPlanCost1 = planCostMapper.selectByPrimaryKey(projectNum);
        for(int i = 0; i < listPlanCost1.size(); i++) {
            planTime=listPlanCost1.get(i).getPlanTime(); //计划回款时间
            planTimeAfterupd=listPlanCost1.get(i).getPlantimeAfterupd();  //修改后计划回款时间
            actualTime=listPlanCost1.get(i).getActualTime();    //实际回款时间
            if(planTime != null && planTimeAfterupd==null && actualTime == null){   // 无里程碑实际时间有里程碑计划时间
                payMent = DateUtil.getDiffDateDays(planTime, nowDate);              // nowDate - planTime
                payMent1= Math.abs(payMent);                                        // 天数差的绝对值
                break;
            }else if(planTimeAfterupd!=null && actualTime == null){
                payMent = DateUtil.getDiffDateDays(planTimeAfterupd, nowDate);    // nowDate - planTime
                payMent1= Math.abs(payMent);                                      // 天数差的绝对值
                break;
            }else if((planTime != null || planTimeAfterupd!=null) && actualTime!=null && i==listPlanCost1.size()-1){ //最后一个里程碑既有计划时间又有实际时间
                payMent = DateUtil.getDiffDateDays(planTime==null?planTimeAfterupd:planTime, actualTime);    // nowDate - planTime
                payMent1= Math.abs(payMent);   // 天数差的绝对值
                isLastRecord="1";
                break;
            }else if(planTime==null && planTimeAfterupd==null){
                //	planTimeAndUpdatePlanTimeIsNull=true; //标记是否循环结束之后计划时间和修改后计划时间都是空的
                payMent=0;   //诊断设置为正常 绿灯
                payMent1=31; //不计算预警
            }
        }

        if(listPlanCost1==null || listPlanCost1.size()==0){
            hasPlanCost=false;
        }

        if(isHandTrans!=1){
            if (payMent <= 0) {
                diagnosticReportVo.setPayment(2);
                diagnosticReportVo.setPaymentlevel("0");
                DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                //  计划时间     修改后计划时间    isLastRecord
                diagnosticTextVo=getHandleDiagnosticReportVo(diagnosticTextVo, planTime, planTimeAfterupd, actualTime, isLastRecord, payMent,flag,hasPlanCost);
                if(!"".equals(actualTime) && actualTime!=null){
                    diagnosticTextVo.setConclusion("里程碑的实际回款时间提前，该项目当前回款进度正常。");
                }else{
                    diagnosticTextVo.setConclusion("当前还未到计划回款时间，该项目当前回款进度正常。");
                }
                diagnosticTextVo.setSuggest("");
                if (0<payMent1 && payMent1<= 30 && hasPlanCost==true) {
                    diagnosticReportVo.setPayment(3);
                    diagnosticReportVo.setPaymentlevel("-1");
                    diagnosticTextVo.setDescribe("里程碑的计划回款时间，提前30天开始亮黄灯");
                    diagnosticTextVo.setConclusion("");
                    diagnosticTextVo.setSuggest("距离里程碑的计划回款时间还有30天，请项目组注意把控进度，务必按照计划时间完成回款。");
                }
                diagnosticReportVo.getPaymentList().add(diagnosticTextVo);
                diagnosticReportVo.setPaymentValue(0.0);
            } else {
                diagnosticReportVo.setPayment(1);
                if (payMent1 > 0 && payMent1 <= 30) {
                    diagnosticReportVo.setPaymentlevel("1");
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo=getHandleDiagnosticReportVo(diagnosticTextVo, planTime, planTimeAfterupd, actualTime, isLastRecord, payMent,flag,hasPlanCost);
                    diagnosticTextVo.setConclusion("里程碑实际回款时间比里程碑计划回款时间延迟（1个月内）。");
                    diagnosticTextVo.setSuggest("项目组（责任销售、PM）注意跟催付款进度。");
                    diagnosticReportVo.getPaymentList().clear();
                    diagnosticReportVo.getPaymentList().add(diagnosticTextVo);
                    diagnosticReportVo.setPaymentValue(Double.valueOf(payMent));
                }
                if (payMent1 > 30 && payMent1 <= 60) {
                    diagnosticReportVo.setPaymentlevel("2");
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo=getHandleDiagnosticReportVo(diagnosticTextVo, planTime, planTimeAfterupd, actualTime, isLastRecord, payMent,flag,hasPlanCost);
                    diagnosticTextVo.setConclusion("里程碑实际回款时间比里程碑计划回款时间延迟1个月以上。");
                    diagnosticTextVo.setSuggest("项目组（责任销售、PM）注意跟催付款进度。");
                    diagnosticReportVo.getPaymentList().clear();
                    diagnosticReportVo.getPaymentList().add(diagnosticTextVo);
                    diagnosticReportVo.setPaymentValue(Double.valueOf(payMent));
                }
                if (payMent1 > 60 && payMent1 <= 180) {
                    diagnosticReportVo.setPaymentlevel("3");
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo=getHandleDiagnosticReportVo(diagnosticTextVo, planTime, planTimeAfterupd, actualTime, isLastRecord, payMent,flag,hasPlanCost);
                    diagnosticTextVo.setConclusion("里程碑实际回款时间比里程碑计划回款时间延迟2个月以上。");
                    diagnosticTextVo.setSuggest("项目组（责任销售、PM）注意跟催付款进度，并定期向单元负责人报告收款情况，抄送PMO、财务。");
                    diagnosticReportVo.getPaymentList().clear();
                    diagnosticReportVo.getPaymentList().add(diagnosticTextVo);
                    diagnosticReportVo.setPaymentValue(Double.valueOf(payMent));
                }
                if (payMent1 > 180 && payMent1 <= 360) {
                    diagnosticReportVo.setPaymentlevel("4");
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo=getHandleDiagnosticReportVo(diagnosticTextVo, planTime, planTimeAfterupd, actualTime, isLastRecord, payMent,flag,hasPlanCost);
                    diagnosticTextVo.setConclusion("里程碑实际回款时间比里程碑计划回款时间延迟6个月以上，该项目回款进度已经严重延期。");
                    diagnosticTextVo.setSuggest("项目组（责任销售、PM）加大跟催力度，同时项目组全力配合，并定期向单元负责人、公司领导报告进展情况，抄送PMO、财务。");
                    diagnosticReportVo.getPaymentList().clear();
                    diagnosticReportVo.getPaymentList().add(diagnosticTextVo);
                    diagnosticReportVo.setPaymentValue(Double.valueOf(payMent));
                }
                if (payMent1 > 360) {
                    diagnosticReportVo.setPaymentlevel("5");
                    DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
                    diagnosticTextVo=getHandleDiagnosticReportVo(diagnosticTextVo, planTime, planTimeAfterupd, actualTime, isLastRecord, payMent,flag,hasPlanCost);
                    diagnosticTextVo.setConclusion("里程碑实际回款时间比里程碑计划回款时间延迟1年以上，该项目回款进度已经严重延期，处于失控状态。");
                    diagnosticTextVo.setSuggest("项目组（责任销售、PM）加大跟催力度，同时项目组全力配合，并定期向单元负责人、公司领导报告进展情况，抄送PMO、财务。");
                    diagnosticReportVo.getPaymentList().clear();
                    diagnosticReportVo.getPaymentList().add(diagnosticTextVo);
                    diagnosticReportVo.setPaymentValue(Double.valueOf(payMent));
                }
            }
        }else{
            diagnosticReportVo.setPayment(2);
            diagnosticReportVo.setPaymentlevel("0");
            DiagnosticTextVo diagnosticTextVo = new DiagnosticTextVo();
            diagnosticTextVo.setDescribe("该项目回款诊断正常");
            diagnosticTextVo.setConclusion("该项目回款诊断正常");
            diagnosticTextVo.setSuggest("该项目回款诊断正常");
            diagnosticReportVo.getPaymentList().add(diagnosticTextVo);
            diagnosticReportVo.setPaymentValue(0.0);
        }
        return diagnosticReportVo;
    }

    //统一处理进度与回款的结论描述
    public DiagnosticTextVo getHandleDiagnosticReportVo(DiagnosticTextVo diagnosticTextVo,Date planTime,Date planTimeAfterupd,Date actualTime,String isLastRecord,int days,String flag,Boolean hasPlanActual){
        SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate=new Date();
        String classify=flag.equals("speed") ?  "考核" : "回款";
        if(planTime!=null && planTimeAfterupd!=null && "1".equals(isLastRecord)){
            diagnosticTextVo.setDescribe("里程碑的实际"+classify+"时间("+tf.format(actualTime)+") - 里程碑的计划"+classify+"时间("+tf.format(planTimeAfterupd)+") =" + days + "天");
        }else if(planTime!=null && planTimeAfterupd==null && "1".equals(isLastRecord)){
            diagnosticTextVo.setDescribe("里程碑的实际"+classify+"时间("+tf.format(actualTime)+") - 里程碑的计划"+classify+"时间("+tf.format(planTime)+") =" + days + "天");
        }else if(planTime!=null && planTimeAfterupd!=null && !"1".equals(isLastRecord)){
            diagnosticTextVo.setDescribe("里程碑的实际"+classify+"时间("+tf.format(nowDate)+") - 里程碑的计划"+classify+"时间("+tf.format(planTimeAfterupd)+") =" + days + "天");
        }else if(planTime!=null && planTimeAfterupd==null && !"1".equals(isLastRecord)){
            diagnosticTextVo.setDescribe("里程碑的实际"+classify+"时间("+tf.format(nowDate)+") - 里程碑的计划"+classify+"时间("+tf.format(planTime)+") =" + days + "天");
        }else if(!hasPlanActual){
            diagnosticTextVo.setDescribe("里程碑的实际"+classify+"时间("+tf.format(nowDate)+") - 里程碑的计划"+classify+"时间(null) =null天(该项目还未填写里程碑)");
        }
        return diagnosticTextVo;
    }

}
