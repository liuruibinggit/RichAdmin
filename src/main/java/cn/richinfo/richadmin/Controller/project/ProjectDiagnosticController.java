package cn.richinfo.richadmin.Controller.project;

import cn.richinfo.richadmin.Entity.project.CropAvgSecore;
import cn.richinfo.richadmin.Entity.project.ProjectStatusSecore;
import cn.richinfo.richadmin.Entity.project.ResultVo;
import cn.richinfo.richadmin.Mapper.ProjectMapper.CorpAvgSecoreMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.ProjectStatusSecoreMapper;
import cn.richinfo.richadmin.SchedualConfigs.ProjectDiagnosticSchedualTask;
import cn.richinfo.richadmin.common.ConstantClassField;
import cn.richinfo.richadmin.common.utils.DoubleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;

/**
 * Created by LiuRuibing on 2019/4/10 0010.
 */
@Controller
@RequestMapping(value = "/projectDiagnostic")
public class ProjectDiagnosticController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDiagnosticController.class);
    @Autowired
    private ProjectDiagnosticSchedualTask projectDiagnosticSchedualTask;

    @Autowired
    private ProjectStatusSecoreMapper projectStatusSecoreMapper;

    @Autowired
    private CorpAvgSecoreMapper corpAvgSecoreMapper;

    //项目诊断程序：支持手动调用和定时器调用
    @RequestMapping(value = "/projectDiagnostic")
    @ResponseBody
    @Scheduled(cron = ConstantClassField.DIAGNOSTICPROJECTS)
    public ResultVo diagnosticProjects() {
        try {
            projectDiagnosticSchedualTask.DiagnosticProjects();
            return new ResultVo("0", "项目诊断完成");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultVo("1", "项目诊断异常", e);
        }
    }

    //获取公司及各个单元各个维度平均得分计算
    @RequestMapping(value = "/getCorpAvgSecoreTask")
    @ResponseBody
    @Scheduled(cron = ConstantClassField.GETCORPAVGSECORETASK)
    public ResultVo GetCorpAvgSecoreTask() {
        logger.info("计算公司及各个单元各项维度平均得分情况定时任务开始。。。。。");
        CropAvgSecore corpAvgSecore = new CropAvgSecore();
        ProjectStatusSecore projectStatusSecore = new ProjectStatusSecore();
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        String monthNow = "";
        if (month == 0) {
            monthNow = year - 1 + "12";
        } else if (month < 10) {
            monthNow = year + "0" + month;
        } else {
            monthNow = year + "" + month;
        }
        projectStatusSecore.setMonth(Integer.parseInt(monthNow));
        corpAvgSecore.setMonth(Integer.parseInt(monthNow));
        ProjectStatusSecore projectSecoreCorp = projectStatusSecoreMapper.getAvgSecore(projectStatusSecore);
        if (projectSecoreCorp != null) {
            corpAvgSecore.setCorpSercore(DoubleUtil.doubleToTwo(projectSecoreCorp.getCountSecory()));
            corpAvgSecore.setCorpSpeedSercore(DoubleUtil.doubleToTwo(projectSecoreCorp.getSpeedSecory()));
            corpAvgSecore.setCorpSettlementSecore(DoubleUtil.doubleToTwo(projectSecoreCorp.getSettlementrSecory()));
            corpAvgSecore.setCorpReturnSecore(DoubleUtil.doubleToTwo(projectSecoreCorp.getPaySecory()));
            corpAvgSecore.setCorpMaoriSercore(DoubleUtil.doubleToTwo(projectSecoreCorp.getGrossprofitSecory()));
        }
        projectStatusSecore.setDept("业务一单元");
        ProjectStatusSecore projectSecoreYw1 = projectStatusSecoreMapper.getAvgSecore(projectStatusSecore);
        if (projectSecoreYw1 != null) {
            corpAvgSecore.setYw1Sercore(DoubleUtil.doubleToTwo(projectSecoreYw1.getCountSecory()));
            corpAvgSecore.setYw1SpeedSercore(DoubleUtil.doubleToTwo(projectSecoreYw1.getSpeedSecory()));
            corpAvgSecore.setYw1SettlementSecore(DoubleUtil.doubleToTwo(projectSecoreYw1.getSettlementrSecory()));
            corpAvgSecore.setYw1ReturnSecore(DoubleUtil.doubleToTwo(projectSecoreYw1.getPaySecory()));
            corpAvgSecore.setYw1MaoriSercore(DoubleUtil.doubleToTwo(projectSecoreYw1.getGrossprofitSecory()));
        }
        projectStatusSecore.setDept("业务二单元");
        ProjectStatusSecore projectSecoreYw2 = projectStatusSecoreMapper.getAvgSecore(projectStatusSecore);
        if (projectSecoreYw2 != null) {
            corpAvgSecore.setYw2Sercore(DoubleUtil.doubleToTwo(projectSecoreYw2.getCountSecory()));
            corpAvgSecore.setYw2SpeedSercore(DoubleUtil.doubleToTwo(projectSecoreYw2.getSpeedSecory()));
            corpAvgSecore.setYw2SettlementSecore(DoubleUtil.doubleToTwo(projectSecoreYw2.getSettlementrSecory()));
            corpAvgSecore.setYw2ReturnSecore(DoubleUtil.doubleToTwo(projectSecoreYw2.getPaySecory()));
            corpAvgSecore.setYw2MaoriSercore(DoubleUtil.doubleToTwo(projectSecoreYw2.getGrossprofitSecory()));
        }
        projectStatusSecore.setDept("北方市场中心");
        ProjectStatusSecore projectSecoreBf = projectStatusSecoreMapper.getAvgSecore(projectStatusSecore);
        if (projectSecoreBf != null) {
            corpAvgSecore.setBfSercore(DoubleUtil.doubleToTwo(projectSecoreBf.getCountSecory()));
            corpAvgSecore.setBfSpeedSercore(DoubleUtil.doubleToTwo(projectSecoreBf.getSpeedSecory()));
            corpAvgSecore.setBfSettlementSecore(DoubleUtil.doubleToTwo(projectSecoreBf.getSettlementrSecory()));
            corpAvgSecore.setBfReturnSecore(DoubleUtil.doubleToTwo(projectSecoreBf.getPaySecory()));
            corpAvgSecore.setBfMaoriSercore(DoubleUtil.doubleToTwo(projectSecoreBf.getGrossprofitSecory()));
        }
        projectStatusSecore.setDept("南方市场中心");
        ProjectStatusSecore projectSecoreNf = projectStatusSecoreMapper.getAvgSecore(projectStatusSecore);
        if (projectSecoreNf != null) {
            corpAvgSecore.setNfSercore(DoubleUtil.doubleToTwo(projectSecoreNf.getCountSecory()));
            corpAvgSecore.setNfSpeedSercore(DoubleUtil.doubleToTwo(projectSecoreNf.getSpeedSecory()));
            corpAvgSecore.setNfSettlementSecore(DoubleUtil.doubleToTwo(projectSecoreNf.getSettlementrSecory()));
            corpAvgSecore.setNfReturnSecore(DoubleUtil.doubleToTwo(projectSecoreNf.getPaySecory()));
            corpAvgSecore.setNfMaoriSercore(DoubleUtil.doubleToTwo(projectSecoreNf.getGrossprofitSecory()));
        }
        projectStatusSecore.setDept("企业业务单元");
        ProjectStatusSecore projectSecoreQy = projectStatusSecoreMapper.getAvgSecore(projectStatusSecore);
        if (projectSecoreQy != null) {
            corpAvgSecore.setQySercore(DoubleUtil.doubleToTwo(projectSecoreQy.getCountSecory()));
            corpAvgSecore.setQySpeedSercore(DoubleUtil.doubleToTwo(projectSecoreQy.getSpeedSecory()));
            corpAvgSecore.setQySettlementSecore(DoubleUtil.doubleToTwo(projectSecoreQy.getSettlementrSecory()));
            corpAvgSecore.setQyReturnSecore(DoubleUtil.doubleToTwo(projectSecoreQy.getPaySecory()));
            corpAvgSecore.setQyMaoriSercore(DoubleUtil.doubleToTwo(projectSecoreQy.getGrossprofitSecory()));
        }
        projectStatusSecore.setDept("数字营销组");
        ProjectStatusSecore projectSecoreSz = projectStatusSecoreMapper.getAvgSecore(projectStatusSecore);
        if (projectSecoreSz != null) {
            corpAvgSecore.setSzSercore(DoubleUtil.doubleToTwo(projectSecoreSz.getCountSecory()));
            corpAvgSecore.setSzSpeedSercore(DoubleUtil.doubleToTwo(projectSecoreSz.getSpeedSecory()));
            corpAvgSecore.setSzSettlementSecore(DoubleUtil.doubleToTwo(projectSecoreSz.getSettlementrSecory()));
            corpAvgSecore.setSzReturnSecore(DoubleUtil.doubleToTwo(projectSecoreSz.getPaySecory()));
            corpAvgSecore.setSzMaoriSercore(DoubleUtil.doubleToTwo(projectSecoreSz.getGrossprofitSecory()));
        }
        corpAvgSecoreMapper.insert(corpAvgSecore);
        logger.info("计算公司及各个单元各项维度平均得分情况定时任务结束。。。。。");
        return new ResultVo("0", "计算公司及各个单元各项维度平均得分情况定时任务结束。。。。。");
    }
}
