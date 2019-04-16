package cn.richinfo.richadmin.Mapper.ProjectMapper;

import cn.richinfo.richadmin.Entity.project.DiagnosticText;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Mapper
public interface DiagnosticTextMapper {
    int deleteByPrimaryKey(Integer diagnosticTextId);
    int insert(DiagnosticText record);
    int insertSelective(DiagnosticText record);
    DiagnosticText selectByPrimaryKey(Integer diagnosticTextId);
    int updateByPrimaryKeySelective(DiagnosticText record);
    int updateByPrimaryKey(DiagnosticText record);
    void deleteAll();
    List<DiagnosticText> selectByProjectNum(String projectNum);
    void deleteDiagnostictextByYearAndMonth(Date startYearAndMonth);
}
