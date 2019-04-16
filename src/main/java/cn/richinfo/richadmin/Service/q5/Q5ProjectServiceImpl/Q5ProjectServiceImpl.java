package cn.richinfo.richadmin.Service.q5.Q5ProjectServiceImpl;

import cn.richinfo.richadmin.Entity.project.ApprovalInformation;
import cn.richinfo.richadmin.Entity.project.Employee;
import cn.richinfo.richadmin.Entity.project.Q5Project;
import cn.richinfo.richadmin.Entity.project.Q5WorkDetails;
import cn.richinfo.richadmin.Mapper.ProjectMapper.ApprovalInformationMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.Q5ProjectMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.Q5WorkDetailsMapper;
import cn.richinfo.richadmin.Service.q5.Q5ProjectService;
import cn.richinfo.richadmin.common.utils.ConnectUtil;
import cn.richinfo.richadmin.common.utils.DateUtil;
import cn.richinfo.richadmin.common.utils.StringHelp;
import cn.richinfo.richadmin.common.vo.project.Q5GetDailyReport;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/8 0008.
 */
@Service
public class Q5ProjectServiceImpl implements Q5ProjectService{

    private static final Logger logger = LoggerFactory.getLogger(Q5ProjectServiceImpl.class);

    @Autowired
    private Q5ProjectMapper q5ProjectMapper;

    @Autowired
    private Q5WorkDetailsMapper q5WorkDetailsMapper;

    @Autowired
    private ApprovalInformationMapper approvalInformationMapper;

    @Autowired
    private Environment environment;

    @Override
    public void addQ5ProjectInit(String year,String month) {
        List<ApprovalInformation> approList = approvalInformationMapper.selectAllProjectNum(); // 获取所有的项目编号
        try {
            for (ApprovalInformation appro : approList) {
                if ((appro.getStartTime() == null || appro.getStartTime().equals(""))  || ((appro.getStopTime() == null ) || appro.getStopTime().equals("") )){
                    logger.error("tid={} | 时间不存在");
                } else {
                    if(environment.getProperty("isInitQ5Project").equals("1")){
                        Q5Project pro = new Q5Project();
                        pro.setProjectNum(appro.getProjectNum());
                        pro.setWorkTime(year + month);
                        Calendar stopCal= Calendar.getInstance();
                        stopCal.setTime(appro.getStopTime());
                        if(DateUtil.getYearAndMonth(stopCal).compareTo(year+month) >= 0){
                            insertInfo(year, month, appro, pro);
                        }
                    }else{ //重新初始化Q5项目的所有数据
                        Calendar calStart =Calendar.getInstance();
                        calStart.setTime(appro.getStartTime()); //获取项目开始时间
                        Calendar calEnd = Calendar.getInstance();
                        int mon = Integer.valueOf(month);
                        int yea = Integer.valueOf(year);
                        int startYea = 2015;
                        int startMon= 1;
                        int endYea = calEnd.get(Calendar.YEAR);
                        int endMon = calEnd.get(Calendar.MONTH)+1;
                        int timeNum = calDateNum(mon, yea, startMon,startYea ,endMon,endYea);
                        for (int i = 0; i <=timeNum; i++) {
                            String tempMonth = "";
                            String tempYear = "";
                            if (startMon + i<= 12) {
                                tempMonth = String.valueOf(startMon + i);
                                tempYear = String.valueOf(startYea);
                            } else if((startMon + i )%12 == 0){
                                tempMonth = String.valueOf(startMon + i -((startMon + i )/12-1)*12);
                                tempYear = String.valueOf(startYea + (startMon + i)/12-1);
                            }else{
                                tempMonth = String.valueOf(startMon + i -((startMon + i )/12)*12);
                                tempYear = String.valueOf(startYea + (startMon + i)/12);
                            }
                            Q5Project pro = new Q5Project();
                            pro.setProjectNum(appro.getProjectNum());
                            if(tempMonth.length()==1){
                                tempMonth="0"+ tempMonth;
                            }
                            pro.setWorkTime(tempYear+ tempMonth);
                            insertInfo(tempYear, tempMonth, appro, pro);
                        }
                    }
                }
            }
           addQ5ProjectDetails(year,month);
        } catch (Exception e) {
            logger.error("tid={} | msg=从Q5 系统中按月获取项目报工异常", e);
        }
    }


    /**
     * 从Q5信息中获取项目报工，并写入数据库
     *
     * @param year
     * @param month
     * @param appro
     */
    @Override
    public void insertInfo(String year, String month, ApprovalInformation appro, Q5Project pro){
        try {
            String date = year + month;
            if (queryByprojectNum(pro) != null) { // 如果数据库中已经存在该项目月份的数据，则将数据库中的数据删除
                deleteByProjectNum(pro);
            }
            Q5GetDailyReport q5GetDailyReport = JSON.parseObject(getProjectInfo(appro.getProjectNum(), year, month),
                    Q5GetDailyReport.class);// 从Q5系统中获取项目信息
            Double workingHoursCount = 0.0;
            pro.setCreateTime(new Date());
            pro.setManagerName(q5GetDailyReport.getName());
            pro.setManagerNum(q5GetDailyReport.getNumber());
            pro.setWorkMonth(Integer.parseInt(date));
            pro.setPeopleCount(q5GetDailyReport.getCount());
            if(q5GetDailyReport.getCount() != 0){
                workingHoursCount = addWorkDetails(year + month, appro, q5GetDailyReport);
            }
            pro.setWorkingHoursCount(workingHoursCount.toString());
            String startDate = DateUtil.formatDate(appro.getStartTime(), "yyyy-MM-dd");
            String monthAndYaer = startDate.substring(0, 4)+startDate.substring(5, 7); // 将2017-07-07 转为 201707

            if ((date).equals(monthAndYaer)) { // 如果当前月份与项目的开始时间一致,则尚需投入工时=初始预算总工时-当月总工时
                if (appro.getCostHours() == 0.00 || "".equals(appro.getCostHours())) {
                    pro.setPlanCountMonth("-" + workingHoursCount);
                } else {
                    Double budgetHours = Double.valueOf(appro.getCostHours());
                    Double planCountMonth = budgetHours - workingHoursCount;
                    pro.setPlanCountMonth(planCountMonth.toString());
                }
            } else { // 否则，获取数据库中的最新报工时间，则尚需投入工时=最新报工时间的尚需投入的工时-当月工时
                if(getNewDate() != null){
                    Double before = StringHelp.String2Double(getNewDate().getPlanCountMonth()); // 获取数据库中存在新报工时间的尚需投入工时
                    Double now = before - workingHoursCount;
                    pro.setPlanCountMonth(now.toString());
                }
            }
            if(q5GetDailyReport.getCount() != 0){
                addProjectInfo(pro); // 添加项目信息
            }
        } catch (Exception e) {
            logger.error("tid={} | msg=从Q5 系统中按月获取项目报工异常", e);
        }
    }

    /**
     * 添加Q5报工详情
     *
     * @param date
     * @param appro
     * @param q5GetDailyReport
     * @return
     */
    public Double addWorkDetails(String date, ApprovalInformation appro, Q5GetDailyReport q5GetDailyReport) throws Exception {

        Q5WorkDetails workDetails = new Q5WorkDetails();
        workDetails.setCreateTime(new Date());
        workDetails.setProjectNum(appro.getProjectNum());
        workDetails.setWorkTime(date);
        List<Q5WorkDetails> list = queryDetails(workDetails); // 根据项目编号和报工时间查询数据库中是否存在数据
        if (list.size() != 0) { // 如果存在，则删除
            deleteDetails(list);
        }
        List<Employee> employees = q5GetDailyReport.getList(); // 获取Q5系统的员工列表
        Double workingHoursCount = 0.00;
        for (Employee employee : employees) {
            workDetails.setWorkName(employee.getName());
            workDetails.setWorkDept(employee.getBelongTo());
            workDetails.setWorkMonth(Integer.valueOf(date));
            if(employee.getNumber() != null && !employee.getNumber().equals("")){
                workDetails.setEmployeeNumber(employee.getNumber());
            }
            Double workHours = employee.getMonths();
            workDetails.setWorkHours(workHours.toString());
            workingHoursCount += workHours;
            addDetails(workDetails); // 添加项目详细信息
        }
        return workingHoursCount;
    }

    /**
     * 计算项目起始时间到（当前月份-1）或结束时间的时间差
     *
     * @param month
     * @param year
     * @param startYear
     * @param startMonth
     * @return
     */
    public int calDateNum(int month, int year, int startMonth, int startYear,int endMonth,int endYear) {
        Integer timeNum = 0;
        Integer tempYear = 0;
        Integer tempMonth = 0;
        if((endYear > year)|| (endYear == year && endMonth > month) ) //如果结束年份大于当前年份，计算开始时间到当前时间的差
        {
            tempYear = year;
            tempMonth = month;
        }else if((endYear < year) || (endYear == year && endMonth < month) ){ //如果结束年份小于当前年份，计算开始时间到结束时间的差
            tempYear = endYear;
            tempMonth = endMonth;
        }
        if (tempYear == startYear) { // 如果在同一年
            timeNum = tempMonth - startMonth;
        } else if (tempMonth > startMonth) {
            int yeaNum = tempYear - startYear;
            timeNum = yeaNum * 12 + (tempMonth - startMonth);
        } else if (tempMonth < startYear || tempMonth == startYear ) {
            int yeaNum = tempYear - startYear - 1;
            timeNum = yeaNum * 12 + (12 - (startMonth - tempMonth));
        }
        return timeNum;
    }

    /**
     * 获取所有项目编号
     */
    public List<ApprovalInformation> selectAllProjectNum() {
        return approvalInformationMapper.selectAllProjectNum();
    }

    /**
     * 添加项目信息
     */
    public int addProjectInfo(Q5Project q5Project) {

        return q5ProjectMapper.insertSelective(q5Project);
    }

    /**
     * 添加项目详细信息
     */
    public void addDetails(Q5WorkDetails q5WorkDetails) {
        q5WorkDetailsMapper.insertSelective(q5WorkDetails);
    }

    public void updateByPrimaryKey(Q5Project record) {
        q5ProjectMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 通过项目编号 从Q5系统中获取项目信息
     */
    public String getProjectInfo(String pronum, String year, String month) throws IOException{
        String path = environment.getProperty("Q5_url") + "?action=" + environment.getProperty("proAction") + "&pronum="
                + pronum + "&year=" + year + "&month=" + month;
        return ConnectUtil.sendPost(path);
    }

    public String getQ5ProjectDetailInfo(String year, String month) throws IOException{
        String path = environment.getProperty("Q5_url") + "?action=" + environment.getProperty("deteilAction") +"&year=" + year + "&month="+ month;
        return ConnectUtil.sendPost(path);
    }

    /**
     * 通过项目编号和工时填报年月份查询数据中是否存在该数据
     */
    public List<Q5Project> queryByprojectNum(Q5Project q5Project) {
        return q5ProjectMapper.queryByProjectNum(q5Project);
    }

    public List<Q5WorkDetails> queryDetails(Q5WorkDetails q5WorkDetails) {
        return q5WorkDetailsMapper.selectByPrimaryKey(q5WorkDetails);
    }

    public void deleteByProjectNum(Q5Project q5Project) {
        q5ProjectMapper.deleteByProjectNum(q5Project);
    }

    public void deleteDetails(List<Q5WorkDetails> q5WorkDetails) {
        for (Q5WorkDetails q5WorkDetail : q5WorkDetails) {
            q5WorkDetailsMapper.deleteDetails(q5WorkDetail);
        }
    }

    /**
     * 获取所有的项目列表
     */
    public List<Q5Project> list() {
        return q5ProjectMapper.selectList();
    }

    public Q5Project getNewDate() {
        return q5ProjectMapper.getNewDate();
    }

    public void addQ5ProjectDetails(String year,String month){
        //调用接口 获取Q5详细信息
        String q5ProjectDetailInfo=null;
        try {
            q5ProjectDetailInfo =getQ5ProjectDetailInfo(year,month);
        } catch (IOException e) {
            logger.error("tid={} | 获取q5项目详细信息失败!");
        }
        JSONObject parseObject = JSONObject.parseObject(q5ProjectDetailInfo);
        JSONArray jsonArray = parseObject.getJSONArray("List");
        JSONObject jSONObject=null;
        Q5WorkDetails q5WorkDetails=new Q5WorkDetails();
        for(int i=0;i<jsonArray.size();i++){
            jSONObject = (JSONObject)jsonArray.get(i);
            String projectNum=(String)jSONObject.get("ProNumber");
            q5WorkDetails.setProjectNum(projectNum);
            String temp=year+month+"";
            q5WorkDetails.setWorkTime(temp);
            q5WorkDetails.setWorkMonth(Integer.parseInt(temp));
            String adrr = (String)jSONObject.get("Addr");
            q5WorkDetails.setAddress(adrr);
            String EmpName=(String)jSONObject.get("EmpName");
            q5WorkDetails.setWorkName(EmpName);
            String empNum=(String)jSONObject.get("EmpNumber");
            q5WorkDetails.setEmployeeNumber(empNum);
            String empState=(String)jSONObject.get("EmpState");
            q5WorkDetails.setEmp_state(empState);
            String hireDate=(String)jSONObject.get("HireDate");
            q5WorkDetails.setHired_date(hireDate);
            String months=((BigDecimal)jSONObject.get("Months")).toString();
            q5WorkDetails.setWorkHours(months);
            String needAttend=((BigDecimal)jSONObject.get("NeedAttend")).toString();
            q5WorkDetails.setNeed_attend(needAttend);
            String uintName=(String)jSONObject.get("Org1Name");
            q5WorkDetails.setWorkDept(uintName);
            String dept=(String)jSONObject.get("Org2Name");
            q5WorkDetails.setDept(dept);
            String group=(String)jSONObject.get("Org4Name");
            q5WorkDetails.setGroup_in(group);
            String accountUint=(String)jSONObject.get("Org5Name");
            q5WorkDetails.setAccount_incomeuint(accountUint);
            String position=(String)jSONObject.get("PosName");
            q5WorkDetails.setPosition(position);
            String projectName=(String)jSONObject.get("ProName");
            q5WorkDetails.setProject_name(projectName);
            String redList=((Boolean)jSONObject.get("RedList")).toString();
            q5WorkDetails.setRed_list(redList);
            q5WorkDetails.setCreateTime(new Date());
            addDetails(q5WorkDetails); // 添加项目详细信息
        }
    }

}
