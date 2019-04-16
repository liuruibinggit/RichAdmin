package cn.richinfo.richadmin.Service.Project;


import cn.richinfo.richadmin.Entity.project.DiagnosticReport;
import cn.richinfo.richadmin.Entity.project.DiagnosticText;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/4 0004.
 */
public interface DiagnosticService {

    void insertDiagnosticReportSelective(DiagnosticReport diagnosticReport);

    void insertDiagnosticTextSelective(DiagnosticText diagnosticText);

    DiagnosticReport selectDiagnosticReportByProjectNum(String projectNum);

    void deleteDiagnosticReportByProjectNum(String projectNum);

    void clearDiagnosticText();

    List<DiagnosticText> selectDiagnosticTextByProjectNum(String projectNum);

    Object diagnosticLevel(String projectNum);

    void deleteDiagnostictextByYearAndMonth(String yearAndMonth);

    void deleteDiagnosticreportByYearAndMonth(String yearAndMonth);
}
