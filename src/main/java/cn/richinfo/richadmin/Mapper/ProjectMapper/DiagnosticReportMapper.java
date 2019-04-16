package cn.richinfo.richadmin.Mapper.ProjectMapper;

import cn.richinfo.richadmin.Entity.project.DiagnosticReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Mapper
public interface DiagnosticReportMapper {

    int deleteByPrimaryKey(Integer diagnosticReportId);
    int insert(DiagnosticReport record);
    int insertSelective(DiagnosticReport record);
    DiagnosticReport selectByPrimaryKey(Integer diagnosticReportId);
    int updateByPrimaryKeySelective(DiagnosticReport record);
    int updateByPrimaryKey(DiagnosticReport record);
    DiagnosticReport selectByProjectNum(String projectNum);
    void deleteByProjectNum(String projectNum);
    List<DiagnosticReport> selectAllByProjectNum(String projectNum);
    void updateByprojectNum(String projectNum);
    void deleteDiagnosticreportByYearAndMonth(@Param("startYearAndMonth") Date startYearAndMonth);

}
