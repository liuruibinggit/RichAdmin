package cn.richinfo.richadmin.SchedualConfigs;

import cn.richinfo.richadmin.Entity.project.ApprovalInformation;
import cn.richinfo.richadmin.Entity.project.ProjectCost;
import cn.richinfo.richadmin.Entity.project.Q5Project;
import cn.richinfo.richadmin.Mapper.ProjectMapper.ApprovalInformationMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.ProjectCostMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.Q5ProjectMapper;
import cn.richinfo.richadmin.common.utils.ConnectUtil;
import cn.richinfo.richadmin.common.utils.DateUtil;
import cn.richinfo.richadmin.common.utils.TimeUtil;
import cn.richinfo.richadmin.common.vo.project.Q5GetIncreaseHoursReport;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/8 0008.
 */
@Component
public class GetIncreaseHours {

    private static final Logger logger = LoggerFactory.getLogger(GetIncreaseHours.class);

    @Autowired
    private ApprovalInformationMapper approvalInformationMapper;

    @Autowired
    private ProjectCostMapper projectCostMapper;

    @Autowired
    private Q5ProjectMapper q5ProjectMapper;

    @Autowired
    private Environment environment;


    //注入映射SchedualTasks.properties的映射实体类
//    @Autowired
//    private SchedualTasksPro schedualTasksPro;

    public void run() throws ParseException, IOException {
        logger.info("定时获取项目滚动工时任务开始....................");
        List<ApprovalInformation> list =  approvalInformationMapper.selectApproval();
        if(list != null && list.size()>0){
            for(ApprovalInformation app : list){
                List<Q5Project> listQ5 =  q5ProjectMapper.selectByPrimaryKey(app.getProjectNum());
                if(listQ5.size() >= 3){
                    double countHours = q5ProjectMapper.getProjectCountHours(app.getProjectNum());
                    String stopTime = DateUtil.formatDate(app.getStopTime());
                    String startTime = DateUtil.formatDate(new Date());
                    if(startTime != null && !stopTime.equals("")){
                        List<String> listTime = TimeUtil.getMonthBetween(startTime, stopTime);
                        double rollCostHours = 0.0;
                        if(listTime != null && listTime.size()>0){
                            for(String time : listTime){
                                String []a =time.split("-");
                                a[1] = Integer.parseInt(a[1])+"";
                                String resport =getIncreaseHours(app.getProjectNum(),a[0],a[1]);
                                Q5GetIncreaseHoursReport q5r = JSON.parseObject(resport,Q5GetIncreaseHoursReport.class);
                                if(q5r.getHours()> 0){
                                    ProjectCost projectCost = new ProjectCost();
                                    projectCost.setProjectNum(app.getProjectNum());
                                    projectCost.setPlanMonth(q5r.getHours()+"");
                                    if(Integer.parseInt(a[1]) < 10){
                                        a[1] = "0"+ a[1];
                                    }
                                    projectCost.setPlanYear(a[0]+a[1]);
                                    ProjectCost pc = projectCostMapper.selectByNumYear(app.getProjectNum(),a[0]+a[1]);
                                    rollCostHours += q5r.getHours();
                                    if(pc != null){
                                        projectCostMapper.updateIncreateHours(projectCost);
                                    }else{
                                        projectCostMapper.insertSelective(projectCost);
                                    }

                                }else{
                                    ProjectCost projectCost = new ProjectCost();
                                    projectCost.setProjectNum(app.getProjectNum());
                                    if(Integer.parseInt(a[1]) < 10){
                                        a[1] = "0"+ a[1];
                                    }
                                    projectCost.setPlanYear(a[0]+a[1]);
                                    ProjectCost pc = projectCostMapper.selectByNumYear(app.getProjectNum(),a[0]+a[1]);
                                    if(pc == null){
                                        projectCostMapper.insertSelective(projectCost);
                                    }
                                }
                            }
                        }
                        ApprovalInformation appi = new ApprovalInformation();
                        appi.setProjectNum(app.getProjectNum());
                        appi.setCostHours(countHours+rollCostHours);
                        approvalInformationMapper.updateCostHours(appi);
                    }
                }
            }
        }
        logger.info("定时获取项目滚动工时任务结束....................");
    }

    public String getIncreaseHours(String pronum,String year,String month) throws IOException{
        //使用Spring的Environment类获取配置文件中的值
        String path = environment.getProperty("Q5_url")
                + "?action=" + environment.getProperty("GetIncreaseHours")+ "&pronum="
                + pronum + "&year="+ year + "&month=" +month;
        System.out.println(path);
        return ConnectUtil.sendPost(path);
    }
}
