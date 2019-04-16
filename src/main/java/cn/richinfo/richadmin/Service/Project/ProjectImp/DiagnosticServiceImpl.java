package cn.richinfo.richadmin.Service.Project.ProjectImp;

import cn.richinfo.richadmin.Entity.project.DiagnosticReport;
import cn.richinfo.richadmin.Entity.project.DiagnosticText;
import cn.richinfo.richadmin.Mapper.ProjectMapper.DiagnosticReportMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.DiagnosticTextMapper;
import cn.richinfo.richadmin.Service.Project.DiagnosticService;
import cn.richinfo.richadmin.common.vo.project.DiagnosticLevelVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LiuRuibing on 2019/4/4 0004.
 */
@Service
public class DiagnosticServiceImpl implements DiagnosticService {

    @Autowired
    private DiagnosticReportMapper diagnosticReportMapper;

    @Autowired
    private DiagnosticTextMapper diagnosticTextMapper;

    @Override
    public void insertDiagnosticReportSelective(DiagnosticReport diagnosticReport) {
        diagnosticReportMapper.insertSelective(diagnosticReport);
    }
    @Override
    public void insertDiagnosticTextSelective(DiagnosticText diagnosticText) {
        diagnosticTextMapper.insertSelective(diagnosticText);
    }
    @Override
    public DiagnosticReport selectDiagnosticReportByProjectNum(String projectNum) {
        DiagnosticReport diagnosticReport=diagnosticReportMapper.selectByProjectNum(projectNum);
        return diagnosticReport;
    }
    @Override
    public void deleteDiagnosticReportByProjectNum(String projectNum) {
        diagnosticReportMapper.deleteByProjectNum(projectNum);
    }
    @Override
    public void clearDiagnosticText() {
        diagnosticTextMapper.deleteAll();
    }
    @Override
    public List<DiagnosticText> selectDiagnosticTextByProjectNum(String projectNum) {

        return diagnosticTextMapper.selectByProjectNum(projectNum);
    }

    @Override
    public void deleteDiagnostictextByYearAndMonth(String yearAndMonth) {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
        String startYearAndMonth=yearAndMonth+"-01";
        try {
            Date parse = formatter.parse(startYearAndMonth);
            diagnosticTextMapper.deleteDiagnostictextByYearAndMonth(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void deleteDiagnosticreportByYearAndMonth(String yearAndMonth) {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
        String startYearAndMonth=yearAndMonth+"-01";
        try {
            diagnosticReportMapper.deleteDiagnosticreportByYearAndMonth(formatter.parse(startYearAndMonth));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public Object diagnosticLevel(String projectNum) {
        DiagnosticLevelVo diagnosticLevelVo=new DiagnosticLevelVo();
        List<DiagnosticReport> list=diagnosticReportMapper.selectAllByProjectNum(projectNum);
        for(DiagnosticReport diagnosticReport:list){
            Date month1=diagnosticReport.getMonth();  //月份
            DateFormat bf = new SimpleDateFormat("yyyy/MM");
            String month=bf.format(month1);
            String costlevel=diagnosticReport.getCostlevel()+"";
            String speedlevel=diagnosticReport.getSpeedlevel();
            String manhourlevel=diagnosticReport.getManhourlevel()+"";
            String paymentlevel=diagnosticReport.getPaymentlevel();
            String grossprofitlevel=diagnosticReport.getGrossprofitlevel()+"";
            String settlementrlevel=diagnosticReport.getSettlementrlevel()+"";
            Map<String,String> costMap=new HashMap<>();
            costMap.put("month", month);
            costMap.put("level", costlevel);
            diagnosticLevelVo.getCostList().add(costMap);

            Map<String,String> speedMap=new HashMap<String,String>();
            speedMap.put("month", month);
            speedMap.put("level", speedlevel);
            diagnosticLevelVo.getSpeedList().add(speedMap);

            Map<String,String> manhourMap=new HashMap<String,String>();
            manhourMap.put("month", month);
            manhourMap.put("level", manhourlevel);
            diagnosticLevelVo.getManhourList().add(manhourMap);

            Map<String,String> paymentMap=new HashMap<String,String>();
            paymentMap.put("month", month);
            paymentMap.put("level", paymentlevel);
            diagnosticLevelVo.getPaymentList().add(paymentMap);

            Map<String,String> grossprofitMap=new HashMap<String,String>();
            grossprofitMap.put("month", month);
            grossprofitMap.put("level", grossprofitlevel);
            diagnosticLevelVo.getGrossprofitList().add(grossprofitMap);

            Map<String,String> settlementrMap=new HashMap<String,String>();
            settlementrMap.put("month", month);
            settlementrMap.put("level", settlementrlevel);
            diagnosticLevelVo.getSettlementrList().add(settlementrMap);
        }
        return diagnosticLevelVo;
    }
}
