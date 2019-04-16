package cn.richinfo.richadmin.Controller.project;

import cn.richinfo.richadmin.Entity.Q5.Q5Didle;
import cn.richinfo.richadmin.Entity.Q5.Q5PmCrossMonth;
import cn.richinfo.richadmin.Entity.businessLine.BusinessLine;
import cn.richinfo.richadmin.Entity.project.ApprovalInformation;
import cn.richinfo.richadmin.Entity.project.Employee;
import cn.richinfo.richadmin.Entity.project.Q5Project;
import cn.richinfo.richadmin.Entity.project.ResultVo;
import cn.richinfo.richadmin.ExceptionHandle.ResultCode;
import cn.richinfo.richadmin.Mapper.ProjectMapper.ApprovalInformationMapper;
import cn.richinfo.richadmin.SchedualConfigs.ExcessGrossProfitTask;
import cn.richinfo.richadmin.SchedualConfigs.ManagerCheckTask;
import cn.richinfo.richadmin.SchedualConfigs.ProjectCheckTask;
import cn.richinfo.richadmin.SchedualConfigs.SigningTask;
import cn.richinfo.richadmin.Service.q5.Q5DidleService;
import cn.richinfo.richadmin.Service.q5.Q5PmCrossMonthService;
import cn.richinfo.richadmin.Service.q5.Q5ProjectService;
import cn.richinfo.richadmin.Service.businessLine.BusinessLineService;
import cn.richinfo.richadmin.common.ConstantClassField;
import cn.richinfo.richadmin.common.utils.ConnectUtil;
import cn.richinfo.richadmin.common.utils.DateUtil;
import cn.richinfo.richadmin.common.vo.project.Q5GetDailyReport;
import cn.richinfo.richadmin.common.vo.project.Q5ProjectStopTimeReport;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/12 0012.
 */
@Controller
@RequestMapping(value = "/project")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

   @Autowired
   private ApprovalInformationMapper approvalInformationMapper;

   @Autowired
   private Q5ProjectService q5ProjectService;

   @Autowired
   private BusinessLineService businessLineService;

   @Autowired
   private Q5DidleService q5DidleService;

   @Autowired
   private Q5PmCrossMonthService  q5PmCrossMonthService;

   @Autowired
   private Environment environment;

   @Autowired
   private SigningTask signingTask;

   @Autowired
   private ExcessGrossProfitTask excessGrossProfitTask;

   @Autowired
   private ProjectCheckTask projectCheckTask;

   @Autowired
   private ManagerCheckTask managerCheckTask;


    /**
     * #定时计算项目考核
     projectCheckDate=0 0 1 5 * ?
     */
    @RequestMapping(value = "/projectCheckDate")
    @ResponseBody
    @Scheduled(cron = ConstantClassField.PROJECTCHECKDATE)
    public ResultVo projectCheckDate() throws ParseException{
        projectCheckTask.run();
        return new ResultVo("0","定时计算项目考核完成");
    }

    /**
     * #定时计算项目考核
       managerCheckDate=0 0 1 5 * ?
     */
    @RequestMapping(value = "/managerCheckDate")
    @ResponseBody
    @Scheduled(cron = ConstantClassField.MANAGERCHECKDATE)
    public ResultVo managerCheckDate() throws ParseException{
        managerCheckTask.run();
        return new ResultVo("0","定时计算项目考核完成");
    }

    /**
     * 手动调用+自动定时计算
     *  定时计算超额毛利计算表  朝九晚五工作时间内每半小时
     *  ExcessGrossProfitTask
     */
    @RequestMapping(value = "/excessGrossProfitDate")
    @ResponseBody
    @Scheduled(cron = ConstantClassField.EXCESSGROSSPROFITDATE)
    public ResultVo excessGrossProfitDate() throws ParseException{
        excessGrossProfitTask.run();
        return new ResultVo("0","定时计算超额毛利计算表完成");
    }

    /**
     * 手动调用+自动定时计算
     * 定时计算投标与签单额与业务净收
     * @throws ParseException
     */
    @RequestMapping(value = "/signingDate")
    @ResponseBody
    @Scheduled(cron = ConstantClassField.SIGNINGDATE)
    public ResultVo signingDate() throws ParseException{
        signingTask.run();
        return new ResultVo("0","定时计算投标与签单额与业务净收完成");
    }

    /**
     * 定时获取项目结束日期
     */
    @RequestMapping(value = "/getProjectStopTimeDate")
    @ResponseBody
    @Scheduled(cron = ConstantClassField.GETPROJECTSTOPTIEDATE)
    public ResultVo getProjectStopTimeDate() throws ParseException,IOException{
        logger.info("定时获取项目结束日期任务开始....................");
        List<ApprovalInformation> list =  approvalInformationMapper.selectApproval();
        if(list != null && list.size()>0){
            for(ApprovalInformation app : list){
                Q5ProjectStopTimeReport q5 = JSON.parseObject(getProjectStopTime(app.getProjectNum()),Q5ProjectStopTimeReport.class);
                if(q5.getEndDate() != null && !q5.getEndDate().equals("")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(q5.getEndDate());
                    app.setStopTime(date);
                    approvalInformationMapper.updateByPrimaryKeySelective(app);
                }
            }
        }
        logger.info("定时获取项目结束日期任务结束....................");
        return new ResultVo("0","定时获取项目结束日期完成");
    }

    public String getProjectStopTime(String pronum) throws IOException{
        String path = environment.getProperty("Q5_url") + "?action=" + environment.getProperty("projectStopTime") + "&pronum="
                + pronum;
        return ConnectUtil.sendPost(path);
    }

    /**
     * 通过项目编号定时从Q5系统中获取信息
     * @throws Exception
     */
    @Scheduled(cron =ConstantClassField.TASKDATE)
    public ResultVo dealDateCollectTask() throws Exception {
        logger.info("dealDateCollectTask start ", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        String month = String.valueOf(date.get(Calendar.MONTH));
        if(month.equals("0")){
            year = String.valueOf(date.get(Calendar.YEAR)-1);
            month="12";
        }
          addQ5Project(year, month);//获取项目报工
          addQ5Didle(year, month); //获取空转信息
          addQ5CrossMonth(year); //获取项目经理交叉月份信息
        logger.info("dealDateCollectTask end ", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return new ResultVo("0","通过项目编号定时从Q5系统中获取信息完成");
    }

    /**
     * 通过按月获从Q5信息中获取项目报工，并写入数据库
     */
    public void addQ5Project(String year, String month) {
        List<ApprovalInformation> approList = q5ProjectService.selectAllProjectNum(); // 获取所有的项目编号
        for (ApprovalInformation appro : approList) {
            Calendar stopTime = Calendar.getInstance();
            stopTime.setTime(appro.getStopTime());
            int curYear = Integer.valueOf(year);
            int curMonth = Integer.valueOf(month);
            if (stopTime.get(Calendar.YEAR) >= curYear && stopTime.get(Calendar.MONTH) >= curMonth) { //项目结束时间大于当前时间，则添加Q5数据
                Q5Project pro = new Q5Project();
                pro.setProjectNum(appro.getProjectNum());
                pro.setWorkTime(year + month);
                q5ProjectService.insertInfo(year, month, appro, pro);
            }
        }
    }

    /**
     * 通过按月从Q5系统中获取业务单元空转信息，并写入数据库
     */
    public void addQ5Didle(String year, String month){
        try {
            List<BusinessLine> depts = businessLineService.getBusinessLineList(); // 获取业务单元列表
            for (BusinessLine dept : depts) {
                Q5GetDailyReport q5GetDailyReport = JSON.parseObject(
                        q5DidleService.getDidle(dept.getBusinessLineId(), year, month), Q5GetDailyReport.class); // 从Q5系统中获取业务单元的空转信息
                List<Employee> employees = q5GetDailyReport.getList();
                logger.info(dept + "员工信息总数为：" + employees);
                Q5Didle q5Didle = new Q5Didle();
                q5Didle.setDate(year + month);
                q5Didle.setDept(dept.getBusinessLine());
                List<Q5Didle> q5Didles = q5DidleService.queryByDate(q5Didle);
                if (q5Didles.size() != 0 && year != null && month != null) // 若该业务单元的信息已存在，则删除
                    q5DidleService.delDidleInfo(q5Didles);
                q5Didle.setCreateTime(new Date());
                for (Employee employee : employees) {
                    q5Didle.setEmployeeId(employee.getNumber());
                    q5Didle.setEmployeeName(employee.getName());
                    q5Didle.setMonth(employee.getMonths().toString());
                    q5DidleService.addDidleInfo(q5Didle); // 添加单元空转信息
                }
            }
        } catch (Exception e) {
            logger.error("tid={} | msg=从Q5系统中按月获取业务单元空转信息异常", e);
        }
    }

    /**
     * 获取项目经理交叉月份信息
     * @param year
     */
    public void addQ5CrossMonth(String year){
        try {
            List<Q5Project> projectLists = q5ProjectService.list(); // 从q5_project表中获取项目编号、项目经理编号
            for (Q5Project project : projectLists) {
                Q5GetDailyReport q5GetDailyReport = JSON.parseObject(
                        q5PmCrossMonthService.getCrossMonth(project.getProjectNum(), year, project.getManagerNum()),
                        Q5GetDailyReport.class); // 从Q5系统中获取项目经理交叉月份信息
                Q5PmCrossMonth q5PmCrossMonth = new Q5PmCrossMonth();
                q5PmCrossMonth.setWorkTime(year);
                q5PmCrossMonth.setProjectNum(project.getProjectNum());
                q5PmCrossMonth.setPmNum(project.getManagerNum());
                if (q5PmCrossMonthService.select(q5PmCrossMonth) != null) { // 如果该项目已存在，则删除，重新取数据写入数据库中
                    q5PmCrossMonthService.delete(q5PmCrossMonth);
                }
                q5PmCrossMonth.setPmName(project.getManagerName());
                q5PmCrossMonth.setCreateTime(new Date());
                q5PmCrossMonth.setProjectManHour(q5GetDailyReport.getProMonths());
                q5PmCrossMonth.setCrossMonth(q5GetDailyReport.getCrossMonths());
                q5PmCrossMonth.setTotalManHour(q5GetDailyReport.getPMMonths());
                q5PmCrossMonthService.insert(q5PmCrossMonth);// 添加数据
            }
        } catch (Exception e) {
            logger.error("tid={} | msg=从Q5系统中按年获取项目经理交叉月份信息异常", e);
        }
    }
}
