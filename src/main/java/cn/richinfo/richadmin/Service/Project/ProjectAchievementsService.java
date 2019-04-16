package cn.richinfo.richadmin.Service.Project;


import cn.richinfo.richadmin.common.vo.project.DiagnosticReportVo;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
public interface ProjectAchievementsService {

    void inserProject();

    Object getFrossprofit(String projectNum);

    Object diagnosticReport(String projectNum);

    Object diagnosticPayMentReport(String projectNum,DiagnosticReportVo diagnosticReportVo);
    
    Object diagnosticSpeedReport(String projectNum,DiagnosticReportVo diagnosticReportVo);

}
