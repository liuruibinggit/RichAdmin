package cn.richinfo.richadmin.Service.Project.ProjectImp;

import cn.richinfo.richadmin.Mapper.ProjectMapper.DiagnosticReportMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.DiagnosticTextMapper;
import cn.richinfo.richadmin.Service.Project.DiagnosticTextService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 * DiagnosticTextServiceImpl
 */
@Service
public class DiagnosticTextServiceImpl implements DiagnosticTextService {

    Logger logger= LoggerFactory.getLogger(DiagnosticTextServiceImpl.class);

    @Autowired
    private DiagnosticTextMapper diagnosticTextMapper;

    @Autowired
    private DiagnosticReportMapper diagnosticReportMapper;

    @Override
    public void deleteDiagnosticreportByYearAndMonth(String yearAndMonth) {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
        String startYearAndMonth=yearAndMonth+"-01";
        try {
            diagnosticReportMapper.deleteDiagnosticreportByYearAndMonth(formatter.parse(startYearAndMonth));
            logger.error("删除-DiagnosticreportByYearAndMonth-成功");
        } catch (ParseException e) {
            logger.error("删除-DiagnosticreportByYearAndMonth-异常",e);
        }
    }

    @Override
    public void deleteDiagnostictextByYearAndMonth(String yearAndMonth) {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
        String startYearAndMonth=yearAndMonth+"-01";
        try {
            Date parse = formatter.parse(startYearAndMonth);
            diagnosticTextMapper.deleteDiagnostictextByYearAndMonth(parse);
            logger.error("删除-DiagnostictextByYearAndMonth-成功");
        }catch (Exception e){
            logger.error("删除-DiagnostictextByYearAndMonth-异常",e);
        }

    }
}
