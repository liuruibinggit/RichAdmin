package cn.richinfo.richadmin.SchedualConfigs;

import cn.richinfo.richadmin.Entity.check.ManagerCheck;
import cn.richinfo.richadmin.Entity.check.ProjectCheck;
import cn.richinfo.richadmin.Entity.project.ApprovalInformation;
import cn.richinfo.richadmin.Entity.project.Q5WorkDetails;
import cn.richinfo.richadmin.Entity.user.User;
import cn.richinfo.richadmin.Mapper.Check.CheckMapper;
import cn.richinfo.richadmin.Mapper.Check.ManagerCheckMapper;
import cn.richinfo.richadmin.Mapper.Check.ProjectCheckMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.*;
import cn.richinfo.richadmin.Mapper.UnitCost.UnitCostInfoMapper;
import cn.richinfo.richadmin.common.utils.UseOtherSystemInterface;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Component
public class ManagerCheckTask {
    private static final Logger logger = LoggerFactory.getLogger(ManagerCheckTask.class);

    @Autowired
    ApprovalInformationMapper approvalInformationMapper;
    @Autowired
    PlanCostMapper planCostMapper;
    @Autowired
    PlanActualMapper planActualMapper;
    @Autowired
    ProjectCostMapper projectCostMapper;
    @Autowired
    ProjectAchievementsMapper projectAchievementsMapper;
    @Autowired
    CheckMapper checkMapper;
    @Autowired
    Q5ProjectMapper q5ProjectMapper;
    @Autowired
    Q5WorkDetailsMapper q5WorkDetailsMapper;
    @Autowired
    private UnitCostInfoMapper unitCostInfoMapper;
    @Autowired
    ProjectCheckMapper projectCheckMapper;
    @Autowired
    ManagerCheckMapper managerCheckMapper;
    @Autowired
    UseOtherSystemInterface useOtherSystemInterface;


    public void run() throws ParseException{
        logger.info("定时计算项目经理考核信息任务开始....................");
        //String year="2018";
        for(int year=2018;year>=2017;year--){
            List<ApprovalInformation> approvalList=new ArrayList<ApprovalInformation>();
            approvalList=approvalInformationMapper.selectApproval();
            for(ApprovalInformation approval:approvalList){
                ManagerCheck managerCheck=new ManagerCheck();
                managerCheck.setProjectManager(approval.getProjectManager());    //项目经理
                managerCheck.setBusinessLine(approval.getPmoDept());              //项目经理所属业务单元
                managerCheck.setDept(approval.getIsDept());                       //项目经理所属部门
                String projectNum =approval.getProjectNum();
                if(null!=q5ProjectMapper.selectByPrimaryKey(projectNum)){
                    String managerNum=q5ProjectMapper.selectByPrimaryKey(projectNum).get(0).getManagerNum();
                    managerCheck.setManagerNum(managerNum);//项目经理工号
                    if(null!=managerNum){
                        User user = null;
                        try {
                            user = JSON.parseObject(useOtherSystemInterface.sendPost(managerNum), User.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        managerCheck.setPosition(user.getPosition());                    //项目经理的职位
                    }else{
                        managerCheck.setPosition("");
                    }
                }

                //项目经理考核得分
                Double managerCheckScore=0.0;
                String projectManager=managerCheck.getProjectManager();//项目经理
                Map<String,String> map=new HashMap<>();
                map.put("projectManager", projectManager);
                map.put("year", year+"");
                List<ProjectCheck> list=projectCheckMapper.selectByProjectManagerAndYear(map);
                for(ProjectCheck projectCheck:list){
                    managerCheckScore+=projectCheck.getProjectCheckScore();
                }
                managerCheck.setManagerCheckScore(managerCheckScore);

                ManagerCheck managerCheck1=managerCheckMapper.selectByPrimaryKeyAndYear(map);
                if(null!=managerCheck1){
                    //奖励倍数
                    if(null!=managerCheck1.getBonusTimes()){
                        managerCheck.setBonusTimes(managerCheck1.getBonusTimes());
                    }
                    //工资
                    if(null!=managerCheck1.getSalary()){
                        managerCheck.setSalary(managerCheck1.getSalary());
                    }

                    //项目经理个人年度工作工时
                    if(null!=managerCheck1.getActualManagerHours()){
                        managerCheck.setActualManagerHours(managerCheck1.getActualManagerHours());
                    }
                }

                //基础奖金
                Double basicBonus=0.0;
                if(null!=managerCheck.getBonusTimes() && null!=managerCheck.getSalary()){
                    basicBonus=managerCheck.getBonusTimes()*managerCheck.getSalary();
                }
                managerCheck.setBasicBonus(basicBonus);

                //项目经理个人总报工工时
                Double managerHours=0.0;
                List<Q5WorkDetails> q5WorkDetailsList=q5WorkDetailsMapper.selectByProjectManagerAndYear(map);
                for(Q5WorkDetails Q5WorkDetails:q5WorkDetailsList){
                    managerHours+=Double.valueOf(Q5WorkDetails.getWorkHours());
                }
                managerCheck.setManagerHours(managerHours);
                //年终奖金
                Double decemberBonus=0.0;
                if(null!=managerCheck.getActualManagerHours()&&managerCheck.getActualManagerHours()!=0 ){
                    decemberBonus=basicBonus*(managerHours/managerCheck.getActualManagerHours());
                }
                managerCheck.setDecemberBonus(decemberBonus);
                managerCheck.setYear(year+"");

                //加到数据库中
                if(null!=managerCheck1){
                    managerCheckMapper.deleteByProjectManagerAndYear(map);
                }
                managerCheckMapper.insertSelective(managerCheck);
            }
        }

        logger.info("定时计算考核信息任务结束..................................");
    }

}
