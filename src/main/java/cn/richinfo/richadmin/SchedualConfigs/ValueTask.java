package cn.richinfo.richadmin.SchedualConfigs;

import cn.richinfo.richadmin.Entity.project.ProjectMonthOutPut;
import cn.richinfo.richadmin.Entity.project.Q5Project;
import cn.richinfo.richadmin.Entity.project.Q5WorkDetails;
import cn.richinfo.richadmin.Entity.projectValue.ProjectLibrary;
import cn.richinfo.richadmin.Entity.projectValue.ProjectMonthValue;
import cn.richinfo.richadmin.Mapper.ProjectMapper.Q5ProjectMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.Q5WorkDetailsMapper;
import cn.richinfo.richadmin.Mapper.ProjectValue.ProjectLibraryMapper;
import cn.richinfo.richadmin.Mapper.ProjectValue.ProjectMonthOutPutMapper;
import cn.richinfo.richadmin.Mapper.ProjectValue.ProjectMonthValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/16 0016.
 */

@Component
public class ValueTask {

    @Autowired
    ProjectMonthOutPutMapper projectMonthOutPutMapper;
    @Autowired
    ProjectLibraryMapper projectLibraryMapper;
    @Autowired
    Q5ProjectMapper q5ProjectMapper;
    @Autowired
    Q5WorkDetailsMapper q5WorkDetailsMapper;
    @Autowired
    ProjectMonthValueMapper projectMonthValueMapper;

    public void run() throws ParseException {
        Calendar c = Calendar.getInstance();
        int years = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)-1;
        StringBuffer monthWork = new StringBuffer();
        StringBuffer date = new StringBuffer();
        if(month<10){
            monthWork.append(years).append("-0").append(month);
        }else{
            monthWork.append(years).append("-").append(month);
        }
        date.append(years).append(month);
        List<ProjectMonthOutPut> listMonth = projectMonthOutPutMapper.selectMonnt(monthWork.toString());
        if(listMonth != null && listMonth.size()>0){
            ProjectMonthValue pmv = new ProjectMonthValue();
            for(ProjectMonthOutPut pmop:listMonth){
                ProjectLibrary project = projectLibraryMapper.selectProjectById(pmop.getPId());
                Q5Project q5Project = q5ProjectMapper.getMonthQ5(project.getProjectNum(), date.toString());
                Q5WorkDetails q5WorkDetails = new Q5WorkDetails();
                q5WorkDetails.setProjectNum(project.getProjectNum());
                q5WorkDetails.setWorkTime(date.toString());
                List<Q5WorkDetails> listQ5WorkDetails = q5WorkDetailsMapper.selectByPrimaryKey(q5WorkDetails);
                if(listQ5WorkDetails != null && listQ5WorkDetails.size()>0){
                    double countWork = Double.parseDouble(q5Project.getWorkingHoursCount());
                    pmv.setProjectName(project.getProjectName());
                    pmv.setProjectNum(project.getProjectNum());
                    pmv.setWorkTime(monthWork.toString());
                    if(countWork == 0.0){
                        pmv.setValue(pmop.getMonthOutPut());
                        projectMonthValueMapper.insert(pmv);
                    }else{
                        double yw1Value=0.0;
                        double yw2Value=0.0;
                        double nfscValue=0.0;
                        double bfscValue=0.0;
                        double qydyValue=0.0;
                        for(int i=0;i<listQ5WorkDetails.size();i++){
                            if(listQ5WorkDetails.get(i).getWorkDept() !=null && listQ5WorkDetails.get(i).getWorkDept().equals("业务一单元")){
                                yw1Value+=Double.parseDouble(listQ5WorkDetails.get(i).getWorkHours());
                            }
                            if(listQ5WorkDetails.get(i).getWorkDept() !=null && listQ5WorkDetails.get(i).getWorkDept().equals("业务二单元")){
                                yw2Value+=Double.parseDouble(listQ5WorkDetails.get(i).getWorkHours());
                            }
                            if(listQ5WorkDetails.get(i).getWorkDept() !=null && listQ5WorkDetails.get(i).getWorkDept().equals("北方市场中心")){
                                bfscValue+=Double.parseDouble(listQ5WorkDetails.get(i).getWorkHours());
                            }
                            if(listQ5WorkDetails.get(i).getWorkDept() !=null && listQ5WorkDetails.get(i).getWorkDept().equals("南方市场中心")){
                                nfscValue+=Double.parseDouble(listQ5WorkDetails.get(i).getWorkHours());
                            }
                            if(listQ5WorkDetails.get(i).getWorkDept() !=null && listQ5WorkDetails.get(i).getWorkDept().equals("企业业务单元")){
                                qydyValue+=Double.parseDouble(listQ5WorkDetails.get(i).getWorkHours());
                            }
                            if(listQ5WorkDetails.get(i).getWorkDept() !=null && listQ5WorkDetails.get(i).getWorkDept().equals("外包")){
                                if(project.getDept().equals("业务一单元")){
                                    yw1Value+=Double.parseDouble(listQ5WorkDetails.get(i).getWorkHours());
                                }
                                if(project.getDept().equals("业务二单元")){
                                    yw2Value+=Double.parseDouble(listQ5WorkDetails.get(i).getWorkHours());
                                }
                                if(project.getDept().equals("北方市场中心")){
                                    bfscValue+=Double.parseDouble(listQ5WorkDetails.get(i).getWorkHours());
                                }
                                if(project.getDept().equals("南方市场中心")){
                                    nfscValue+=Double.parseDouble(listQ5WorkDetails.get(i).getWorkHours());
                                }
                                if(project.getDept().equals("企业业务单元")){
                                    qydyValue+=Double.parseDouble(listQ5WorkDetails.get(i).getWorkHours());
                                }
                            }
                        }
                        if(yw1Value != 0.0){
                            pmv.setValue(pmop.getMonthOutPut()*yw1Value/countWork);
                            pmv.setDept("业务一单元");
                            projectMonthValueMapper.insert(pmv);
                        }
                        if(yw2Value != 0.0){
                            pmv.setValue(pmop.getMonthOutPut()*yw2Value/countWork);
                            pmv.setDept("业务二单元");
                            projectMonthValueMapper.insert(pmv);
                        }
                        if(bfscValue != 0.0){
                            pmv.setValue(pmop.getMonthOutPut()*bfscValue/countWork);
                            pmv.setDept("北方市场中心");
                            projectMonthValueMapper.insert(pmv);
                        }
                        if(nfscValue != 0.0){
                            pmv.setValue(pmop.getMonthOutPut()*nfscValue/countWork);
                            pmv.setDept("南方市场中心");
                            projectMonthValueMapper.insert(pmv);
                        }
                        if(qydyValue != 0.0){
                            pmv.setValue(pmop.getMonthOutPut()*qydyValue/countWork);
                            pmv.setDept("企业业务单元");
                            projectMonthValueMapper.insert(pmv);
                        }
                    }
                }else{
                    pmv.setDept(project.getDept());
                    pmv.setProjectName(project.getProjectName());
                    pmv.setProjectNum(project.getProjectNum());
                    pmv.setWorkTime(monthWork.toString());
                    pmv.setValue(pmop.getMonthOutPut());
                    projectMonthValueMapper.insert(pmv);
                }
            }
        }
    }
}
