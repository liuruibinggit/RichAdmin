package cn.richinfo.richadmin.Service.Project;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 * DiagnosticTextService
 */
public interface DiagnosticTextService {

    //根据年份和月份删除Diagnostictext
   void deleteDiagnostictextByYearAndMonth(String yearAndMonth);

    //根据年份和月份删除Diagnosticreport
    void deleteDiagnosticreportByYearAndMonth(String yearAndMonth);
}
