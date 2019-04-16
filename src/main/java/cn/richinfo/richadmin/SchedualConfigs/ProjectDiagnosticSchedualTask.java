package cn.richinfo.richadmin.SchedualConfigs;

import cn.richinfo.richadmin.Entity.project.ResultVo;
import cn.richinfo.richadmin.Service.Project.DiagnosticTextService;
import cn.richinfo.richadmin.Service.Project.ProjectAchievementsService;
import cn.richinfo.richadmin.Service.Project.ProjectStatusScoreService;
import cn.richinfo.richadmin.common.DiagnosticReportTask;
import cn.richinfo.richadmin.common.GetProjectStatusSecoreTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.Calendar;


/**
 * Created by Administrator
 * on 2019/4/1 0001.
 * 项目诊断定时任务
 */
@Component
public class ProjectDiagnosticSchedualTask {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDiagnosticSchedualTask.class);

    @Autowired
    private ProjectStatusScoreService projectStatusScoreService;

    @Autowired
    private DiagnosticTextService diagnosticTextService;

    @Autowired
    private ProjectAchievementsService projectAchievementsService;

    @Autowired
    private DiagnosticReportTask diagnosticReportTask;

    @Autowired
    private GetProjectStatusSecoreTask getProjectStatusSecoryTask;

    @Autowired
    private Environment environment;

    /**
     * 定时计算项目诊断定时任务
     */
    @Transactional
    public ResultVo DiagnosticProjects(){
        ResultVo resultVo=new ResultVo();
        Calendar now = Calendar.getInstance();
        String yearAndMonth="";
        int year = now.get(Calendar.YEAR);
        int month=now.get(Calendar.MONTH);
        int currMonth=now.get(Calendar.MONTH)+1;
        String tempcurrMonth=(currMonth>0 && currMonth<10)? ("0"+currMonth) : currMonth+"";
        String currentYearAndMonth=year+"-"+tempcurrMonth;
        String tempMonth="";
        if(month==0){
            year=year-1;
            month=12;
        }
        if(month>0 && month<10){
            tempMonth="0"+month;
        }else{
            tempMonth=month+"";
        }
        yearAndMonth=year+""+tempMonth;
        try {
            //在进行项目诊断之前先进性删除原油数据操作
            projectStatusScoreService.deleteProjectStatusSecoreByYearAndMonth(yearAndMonth);
            diagnosticTextService.deleteDiagnostictextByYearAndMonth(currentYearAndMonth);
            diagnosticTextService.deleteDiagnosticreportByYearAndMonth(currentYearAndMonth);
            //开始进行项目诊断操作
           projectAchievementsService.inserProject();
           diagnosticReportTask.run();
           getProjectStatusSecoryTask.run();
           logger.info("执行-DiagnosticProjects-成功,执行项目诊断成功。。。。。");
        }catch (Exception e){
            logger.error("执行-DiagnosticProjects-异常",e);
        }
        return resultVo;
    }

}
