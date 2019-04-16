package cn.richinfo.richadmin.common;

import cn.richinfo.richadmin.Entity.project.ApprovalInformation;
import cn.richinfo.richadmin.Entity.project.DiagnosticReport;
import cn.richinfo.richadmin.Entity.project.DiagnosticText;
import cn.richinfo.richadmin.Mapper.ProjectMapper.ApprovalInformationMapper;
import cn.richinfo.richadmin.Service.Project.DiagnosticService;
import cn.richinfo.richadmin.Service.Project.ProjectAchievementsService;
import cn.richinfo.richadmin.common.vo.project.DiagnosticReportVo;
import cn.richinfo.richadmin.common.vo.project.DiagnosticScoreVo;
import cn.richinfo.richadmin.common.vo.project.DiagnosticTextVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/4 0004.
 */

@Component
public class DiagnosticReportTask {

    @Autowired
    private ApprovalInformationMapper approvalInformationMapper;

    @Autowired
    private ProjectAchievementsService projectAchievementsService;

    @Autowired
    private DiagnosticService diagnosticService;

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticReportTask.class);

    public void run(){
        List<ApprovalInformation> list = approvalInformationMapper.selectApp();
        for (int i = 0; i < list.size(); i++) {
            String projectNum = list.get(i).getProjectNum();
            DiagnosticReportVo diagnosticReportVo =
                    (DiagnosticReportVo) projectAchievementsService.diagnosticReport(projectNum);
            DiagnosticScoreVo diagnosticScoreVo = diagnosticReportVo.getList().get(0);
            // 此次将要保存到数据库中的本月的最新记录
            DiagnosticReport diagnosticReport = new DiagnosticReport();
            BeanUtils.copyProperties(diagnosticReportVo, diagnosticReport);
            diagnosticReport.setRollingMaoriScore(diagnosticScoreVo.getRollingMaoriScore());
            diagnosticReport.setAcceptanceProgressScore(diagnosticScoreVo.getAcceptanceProgressScore());
            diagnosticReport.setReturnProgressScore(diagnosticScoreVo.getReturnProgressScore());
            diagnosticReport.setSettlementRateScore(diagnosticScoreVo.getSettlementRateScore());
            diagnosticReport.setScore(diagnosticScoreVo.getScore());
            diagnosticReport.setProjectStatus(diagnosticScoreVo.getProjectStatus());
            // 当前数据库中的最新记录（上个月的结果）
            DiagnosticReport lastDiagnostic = diagnosticService.selectDiagnosticReportByProjectNum(projectNum);
            if (lastDiagnostic != null) {
                // 成本对比
                Integer diagnosticReportcostlevel = diagnosticReport.getCostlevel()==null ? 0 :diagnosticReport.getCostlevel();
                Integer lastDiagnosticcostlevel = lastDiagnostic.getCostlevel()==null ? 0 : lastDiagnostic.getCostlevel();
                if (diagnosticReportcostlevel > lastDiagnosticcostlevel) {
                    diagnosticReport.setCostComparedWithLastMonth(1); // 上升
                } else if (diagnosticReportcostlevel < lastDiagnosticcostlevel) {
                    diagnosticReport.setCostComparedWithLastMonth(2); // 下降
                } else if (diagnosticReportcostlevel == lastDiagnosticcostlevel) {
                    diagnosticReport.setCostComparedWithLastMonth(3); // 相等
                }
                // 工时对比
                if (diagnosticReport.getManhourlevel().intValue() > lastDiagnostic.getManhourlevel().intValue()) {
                    diagnosticReport.setManhourComparedWithLastMonth(1); // 上升
                }
                if (diagnosticReport.getManhourlevel().intValue() < lastDiagnostic.getManhourlevel().intValue()) {
                    diagnosticReport.setManhourComparedWithLastMonth(2); // 下降
                }
                if (diagnosticReport.getManhourlevel().intValue() == lastDiagnostic.getManhourlevel().intValue()) {
                    diagnosticReport.setManhourComparedWithLastMonth(3); // 相等
                }
                // 毛利率对比
                if (diagnosticReport.getGrossprofitlevel().intValue() > lastDiagnostic.getGrossprofitlevel()
                        .intValue()) {
                    diagnosticReport.setGrossprofitComparedWithLastMonth(1);
                }
                if (diagnosticReport.getGrossprofitlevel().intValue() < lastDiagnostic.getGrossprofitlevel()
                        .intValue()) {
                    diagnosticReport.setGrossprofitComparedWithLastMonth(2);
                }
                if (diagnosticReport.getGrossprofitlevel().intValue() == lastDiagnostic.getGrossprofitlevel()
                        .intValue()) {
                    diagnosticReport.setGrossprofitComparedWithLastMonth(3);
                }
                // 结算率对比
                if (diagnosticReport.getSettlementrlevel().intValue() > lastDiagnostic.getSettlementrlevel()
                        .intValue()) {
                    diagnosticReport.setSettlementrComparedWithLastMonth(1);
                }
                if (diagnosticReport.getSettlementrlevel().intValue() < lastDiagnostic.getSettlementrlevel()
                        .intValue()) {
                    diagnosticReport.setSettlementrComparedWithLastMonth(2);
                }
                if (diagnosticReport.getSettlementrlevel().intValue() == lastDiagnostic.getSettlementrlevel()
                        .intValue()) {
                    diagnosticReport.setSettlementrComparedWithLastMonth(3);
                }
                // 进度对比
                Integer speedlevel = Integer.valueOf(diagnosticReport.getSpeedlevel());// 当前进度
                Integer lastSpeedlevel = Integer.valueOf(lastDiagnostic.getSpeedlevel());// 上次进度等级
                if (speedlevel >= 0 && lastSpeedlevel >= 0) { // 两个进度都不是黄灯正常比
                    if (speedlevel > lastSpeedlevel) {
                        diagnosticReport.setSpeedComparedWithLastMonth(1);
                    } else if (speedlevel < lastSpeedlevel) {
                        diagnosticReport.setSpeedComparedWithLastMonth(2);
                    } else if (speedlevel == lastSpeedlevel) {
                        diagnosticReport.setSpeedComparedWithLastMonth(3);
                    }
                } else if (speedlevel < 0) { // 当前进度黄灯时
                    if (lastSpeedlevel == 0) {
                        diagnosticReport.setSpeedComparedWithLastMonth(1);// 上升
                    } else if (lastSpeedlevel < 0) {
                        diagnosticReport.setSpeedComparedWithLastMonth(3);// 相同
                    } else {
                        diagnosticReport.setSpeedComparedWithLastMonth(2);// 下降
                    }
                } else if (lastSpeedlevel < 0) { // 上次进度是黄灯时
                    if (speedlevel < 0) {
                        diagnosticReport.setSpeedComparedWithLastMonth(3);// 相同
                    } else if (speedlevel > 0) {
                        diagnosticReport.setSpeedComparedWithLastMonth(1);// 上升
                    } else if (speedlevel == 0) {
                        diagnosticReport.setSpeedComparedWithLastMonth(2);// 下降
                    }

                    // 回款对比
                    Integer paymentlevel = Integer.valueOf(diagnosticReport.getPaymentlevel());// 当前进度
                    Integer lastPaymentlevel = Integer.valueOf(lastDiagnostic.getPaymentlevel());// 上次进度等级
                    if (paymentlevel >= 0 && lastPaymentlevel >= 0) { // 两个进度都不是黄灯正常比
                        if (paymentlevel > lastPaymentlevel) {
                            diagnosticReport.setPaymentComparedWithLastMonth(1);
                        } else if (paymentlevel < lastPaymentlevel) {
                            diagnosticReport.setPaymentComparedWithLastMonth(2);
                        } else if (paymentlevel == lastPaymentlevel) {
                            diagnosticReport.setPaymentComparedWithLastMonth(3);
                        }
                    } else if (paymentlevel < 0) { // 当前进度黄灯时
                        if (lastPaymentlevel == 0) {
                            diagnosticReport.setPaymentComparedWithLastMonth(1);// 上升
                        } else if (lastPaymentlevel < 0) {
                            diagnosticReport.setPaymentComparedWithLastMonth(3);// 相同
                        } else {
                            diagnosticReport.setPaymentComparedWithLastMonth(2);// 下降
                        }
                    } else if (lastPaymentlevel < 0) { // 上次进度是黄灯时
                        if (paymentlevel < 0) {
                            diagnosticReport.setPaymentComparedWithLastMonth(3);// 相同
                        } else if (paymentlevel > 0) {
                            diagnosticReport.setPaymentComparedWithLastMonth(1);// 上升
                        } else if (paymentlevel == 0) {
                            diagnosticReport.setPaymentComparedWithLastMonth(2);// 下降
                        }
                    }
                }
            } else {
                diagnosticReport.setSpeedComparedWithLastMonth(3);
                diagnosticReport.setSettlementrComparedWithLastMonth(3);
                diagnosticReport.setManhourComparedWithLastMonth(3);
                diagnosticReport.setGrossprofitComparedWithLastMonth(3);
                diagnosticReport.setCostComparedWithLastMonth(3);
                diagnosticReport.setPaymentComparedWithLastMonth(3);

            }
            // 插入到项目诊断报告表中
            diagnosticService.insertDiagnosticReportSelective(diagnosticReport);
            // 把成本预警描述插入到表
            List<DiagnosticTextVo> costlist = diagnosticReportVo.getCostList();
            insertTableDataDesc(diagnosticReportVo,costlist,"cost");
            // 把进度预警描述插入到表
            List<DiagnosticTextVo> speedlist = diagnosticReportVo.getSpeedList();
            insertTableDataDesc(diagnosticReportVo,speedlist,"speed");
            // 把工时预警描述插入到表
            List<DiagnosticTextVo> manhourlist = diagnosticReportVo.getManhourList();
            insertTableDataDesc(diagnosticReportVo,manhourlist,"manhour");
            // 把回款预警描述插入到表
            List<DiagnosticTextVo> paymentlist = diagnosticReportVo.getPaymentList();
            insertTableDataDesc(diagnosticReportVo,paymentlist,"payment");
            // 把毛利预警描述插入到表
            List<DiagnosticTextVo> grossprofitlist = diagnosticReportVo.getGrossprofitList();
            insertTableDataDesc(diagnosticReportVo,grossprofitlist,"grossprofit");
            // 把结算预警描述插入到表
            List<DiagnosticTextVo> settlementrlist = diagnosticReportVo.getSettlementrList();
            insertTableDataDesc(diagnosticReportVo,settlementrlist,"settlementr");
        }
    }

    //插入描述信息公共方法
    public void insertTableDataDesc(DiagnosticReportVo diagnosticReportVo,List<DiagnosticTextVo> dataList,String warnType) {
        if (dataList != null && dataList.size() > 0) {
            for (DiagnosticTextVo diagnosticTextVo : dataList) {
                DiagnosticText diagnosticText = new DiagnosticText();
                BeanUtils.copyProperties(diagnosticTextVo, diagnosticText);
                diagnosticText.setProjectNum(diagnosticReportVo.getProjectNum());
                diagnosticText.setProjectName(diagnosticReportVo.getProjectName());
                diagnosticText.setWarnType(warnType);
                diagnosticService.insertDiagnosticTextSelective(diagnosticText);
            }
        }
    }
}
