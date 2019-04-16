package cn.richinfo.richadmin.Service.finance.financeImpl;

import cn.richinfo.richadmin.Entity.finance.BusinessIncomeMargin;
import cn.richinfo.richadmin.Entity.finance.BusinessIncomeValue;
import cn.richinfo.richadmin.Mapper.Finance.*;
import cn.richinfo.richadmin.Service.finance.BusinessIncomeService;
import cn.richinfo.richadmin.common.dto.BusinessIncomeMarginDto;
import cn.richinfo.richadmin.common.model.finance.BusinessIncomeModel;
import cn.richinfo.richadmin.common.utils.ArithUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/9 0009.
 */
@Service
public class BusinessIncomeServiceImpl implements BusinessIncomeService {

    @Autowired
    private BusinessIncomeMapper businessIncomeMapper;

    @Autowired
    private BusinessIncomeMonthMarginMapper businessIncomeMonthMarginMapper;

    @Autowired
    private BusinessIncomeBudgetMapper businessIncomeBudgetMapper;

    @Autowired
    private BusinessCostBudgetMapper businessCostBudgetMapper;

    @Autowired
    private BusinessIncomeSemiannualMarginMapper businessIncomeSemiannualMarginMapper;

    @Autowired
    private BusinessIncomeAnnualMarginMapper businessIncomeAnnualMarginMapper;

    @Override
    public void addBusinessIncomeMonthMargin() {
        String date =businessIncomeMapper.maxDate();
        businessIncomeMonthMarginMapper.deleteByDate(date);
        for(int i=1;i<=12;i++){
            BusinessIncomeMarginDto businessIncomeMargin = new BusinessIncomeMarginDto();
            businessIncomeMargin.setMonth(i+"");
            addMargin(businessIncomeMargin,date);
        }
    }

    public void addMargin(BusinessIncomeMarginDto businessIncomeMargin,String date){
        List<String> businessLineList = businessIncomeMapper.getBusinessLineList();
        //String date = businessIncomeMapper.maxDate();
        businessIncomeMargin.setDate(date);
        for(String businessLine : businessLineList){ //遍历业务单元
//		String businessLine ="南方市场中心";
            BusinessIncomeValue incomeValue = new BusinessIncomeValue();
            String year = "2019";
            BeanUtils.copyProperties(businessIncomeMargin, incomeValue);
            incomeValue.setYear(year);
            if(!businessLine.equals("")){
                incomeValue.setSigningBusinessUnit(businessLine);
            }
            ArrayList<String> incomeList = (ArrayList<String>) businessIncomeMapper.getProjectNameList(incomeValue); //获取某个月份段预测业务收入的项目名称
            ArrayList<String> incomeBudgetList = (ArrayList<String>) businessIncomeBudgetMapper.getProjectNameList(incomeValue);//获取某个月份段预计业务收入项目名称

            ArrayList<String> commonList =(ArrayList<String>) incomeList.clone();//获取预测业务收入项目名称和 预计业务收入项目名称的交集
            ArrayList<String> actualList = (ArrayList<String>) incomeList.clone();//预测表有，预计表无
            ArrayList<String> budgetList =(ArrayList<String>)incomeBudgetList.clone(); //预测表无，预计表有

            commonList.retainAll(incomeBudgetList);
            actualList.removeAll(incomeBudgetList);
            budgetList.removeAll(incomeList);

            for(String projectName : commonList){ //处理 预测业务收入 与预计业务收入项目名称一致的情况
                BusinessIncomeMarginDto businessIncomeMarginDto = new BusinessIncomeMarginDto();
                BeanUtils.copyProperties(businessIncomeMargin, businessIncomeMarginDto);
                businessIncomeMarginDto.setProjectName(projectName);
                if( !businessLine.equals("")){
                    businessIncomeMarginDto.setSigningBusinessUnit(businessLine);
                }

                //获取预测业务收入
                BusinessIncomeMarginDto	temp =businessIncomeMapper.sum(businessIncomeMarginDto);
                Double income = temp.getMonthValue();
                //获取预计业务收入
                Double incomePre = businessIncomeBudgetMapper.sum(businessIncomeMarginDto).getMonthValue();
                //获取预计业务成本
                Double costPre = 0.00;
                if(businessCostBudgetMapper.sum(businessIncomeMarginDto)!= null){
                    costPre =businessCostBudgetMapper.sum(businessIncomeMarginDto);
                }

                Double actual = income; //本月实际
                Double kpi = ArithUtil.sub(incomePre, costPre);//预计净收入（本月预计）= 预计业务收入- 预计业务成本
                Double margin = ArithUtil.sub(actual, kpi);//本月差额
                Double rate = 0.0;
                if(actual != 0.00 &&  kpi!= 0.00){
                    rate =ArithUtil.div(kpi,actual);
                }

                if(actual != 0.00 ||  kpi!= 0.00){
                    BusinessIncomeMargin incomeMargin = new BusinessIncomeMargin();
                    temp.setDate(businessIncomeMargin.getDate());
                    BeanUtils.copyProperties(temp, incomeMargin);

                    List<String> actualMonthList = businessIncomeMapper.getMonthByProject(temp); //签署月份
                    List<String> kpiMonthList = businessIncomeBudgetMapper.getMonthByProject(temp); //KPI月份业务收入
                    List<String> kpiMonthCostList = businessCostBudgetMapper.getMonthByProject(temp);//KPI月份（业务成本）

                    if(kpiMonthList.size() != 0 && kpiMonthCostList.size() != 0){ //计算业务净收入的月份列表
                        if(Integer.valueOf(kpiMonthList.get(0))>Integer.valueOf(kpiMonthCostList.get(0))){
                            kpiMonthList = kpiMonthCostList;
                        }
                    }

                    Integer month =12;
                    if(businessIncomeMargin.getEndMonth() != null && !businessIncomeMargin.getEndMonth().equals("")){
                        month =businessIncomeMargin.getEndMonth(); //将截止月份作为报告月份
                    }
                    if(businessIncomeMargin.getMonth() != null && !businessIncomeMargin.getMonth().equals("")){
                        month = Integer.valueOf(businessIncomeMargin.getMonth());
                    }

                    if(actualMonthList.size()== 0){  //在滚动预测表中为0的情况 :报告月份 <签署月份
                        if(month >= Integer.valueOf(kpiMonthList.get(0))){  //报告月份 >= KPI月份
                            incomeMargin.setOrderType("延期订单");
                        }
                    }else if(kpiMonthList.size() == 0){ //财务预算表中为0的情况 报告月份 <KPI月份
                        if(month >= Integer.valueOf(actualMonthList.get(0))){  //报告月份 >= KPI 月份 && 报告月份 >= 签署月份
                            incomeMargin.setOrderType("提前签单");
                        }
                    }else if(actualMonthList.size() != 0 && kpiMonthList.size() != 0){
                        if(month >= Integer.valueOf(actualMonthList.get(0))  //报告月份 >= KPI 月份 && 报告月份 >= 签署月份
                                && month >= Integer.valueOf(kpiMonthList.get(0))){
                            BusinessIncomeModel businessModel = new BusinessIncomeModel();
                            BeanUtils.copyProperties(incomeMargin, businessModel);
                            if(margin == 0){
                                incomeMargin.setOrderType("签单一致");
                            }else{
                                incomeMargin.setOrderType("计划内订单");
                            }
                        }else if( month < Integer.valueOf(actualMonthList.get(0)) //报告月份 <签署月份 && 报告月份 >= KPI月份
                                && month >= Integer.valueOf(kpiMonthList.get(0))){
                            incomeMargin.setOrderType("延期订单");
                        }else if(month >= Integer.valueOf(actualMonthList.get(0))
                                && month < Integer.valueOf(kpiMonthList.get(0))){ //报告月份>=签署月份  && 报告月份 <KPI月份
                            incomeMargin.setOrderType("提前签单");
                        }
                    }
                    incomeMargin.setYear(year);
                    incomeMargin.setMonthActual(actual);
                    incomeMargin.setMonthKpi(kpi);
                    incomeMargin.setMonthMargin(margin);
                    incomeMargin.setMonthRate(rate);
                    incomeMargin.setBusinessLine(businessLine);
                    incomeMargin.setDate(date);

                    if(businessIncomeMargin.getMonth() != null && !businessIncomeMargin.getMonth().equals("")){
                        incomeMargin.setMonth(businessIncomeMargin.getMonth()+"");
                        businessIncomeMonthMarginMapper.insert(incomeMargin);
                    }else if((businessIncomeMargin.getStartMonth() != null && !businessIncomeMargin.getStartMonth().equals(""))
                            ||	businessIncomeMargin.getEndMonth() != null && !businessIncomeMargin.getEndMonth().equals("") ){
                        incomeMargin.setMonth(""+businessIncomeMargin.getStartMonth()+businessIncomeMargin.getEndMonth());
                        businessIncomeSemiannualMarginMapper.insert(incomeMargin);
                    }else{
                        businessIncomeAnnualMarginMapper.insert(incomeMargin);
                    }
                }
            }

            for(String projectName: actualList){
                BusinessIncomeMarginDto businessIncomeMarginDto = new BusinessIncomeMarginDto();
                BeanUtils.copyProperties(businessIncomeMargin, businessIncomeMarginDto);
                businessIncomeMarginDto.setProjectName(projectName);
                if(!businessLine.equals("")){
                    businessIncomeMarginDto.setSigningBusinessUnit(businessLine);
                }

                //获取预测业务收入
                businessIncomeMarginDto=businessIncomeMapper.sum(businessIncomeMarginDto);
                Double monthIncome = businessIncomeMarginDto.getMonthValue();
                if(monthIncome != 0.00){
                    BusinessIncomeMargin incomeMargin = new BusinessIncomeMargin();
                    BeanUtils.copyProperties(businessIncomeMarginDto, incomeMargin);
                    incomeMargin.setOrderType("新增订单");
                    incomeMargin.setYear(year);
                    incomeMargin.setMonthActual(monthIncome);
                    incomeMargin.setMonthKpi(0.00);
                    incomeMargin.setMonthMargin(monthIncome);
                    incomeMargin.setMonthRate(0.00);
                    incomeMargin.setBusinessLine(businessLine);
                    incomeMargin.setDate(date);

                    if(businessIncomeMargin.getMonth() != null && !businessIncomeMargin.getMonth().equals("")){
                        incomeMargin.setMonth(businessIncomeMargin.getMonth()+"");
                        businessIncomeMonthMarginMapper.insert(incomeMargin);
                    }else if((businessIncomeMargin.getStartMonth() != null && !businessIncomeMargin.getStartMonth().equals(""))
                            ||	businessIncomeMargin.getEndMonth() != null && !businessIncomeMargin.getEndMonth().equals("") ){
                        incomeMargin.setMonth(""+businessIncomeMargin.getStartMonth()+businessIncomeMargin.getEndMonth());
                        businessIncomeSemiannualMarginMapper.insert(incomeMargin);
                    }else{
                        businessIncomeAnnualMarginMapper.insert(incomeMargin);
                    }
                }
            }

            for(String projectName : budgetList){
                BusinessIncomeMarginDto businessIncomeMarginDto = new BusinessIncomeMarginDto();
                BeanUtils.copyProperties(businessIncomeMargin, businessIncomeMarginDto);
                businessIncomeMarginDto.setProjectName(projectName);
                if(!businessLine.equals("")){
                    businessIncomeMarginDto.setSigningBusinessUnit(businessLine);
                }

                BusinessIncomeMarginDto temp=businessIncomeBudgetMapper.sum(businessIncomeMarginDto);
                Double costPre =0.00;
                if(businessCostBudgetMapper.sum(businessIncomeMarginDto)!= null){
                    costPre =businessCostBudgetMapper.sum(businessIncomeMarginDto);
                }
                Double monthIncome = temp.getMonthValue()-costPre;

                if(monthIncome != 0.00){
                    BusinessIncomeMargin incomeMargin = new BusinessIncomeMargin();
                    BeanUtils.copyProperties(temp, incomeMargin);
                    if(incomeMargin.getProjectName().contains("未知项目")){ //规则：未知项目属于新增订单
                        incomeMargin.setOrderType("新增订单");
                    }else{
                        incomeMargin.setOrderType("丢单");
                    }
                    incomeMargin.setYear(year);
                    incomeMargin.setMonthActual(0.00);
                    incomeMargin.setMonthKpi(monthIncome);
                    incomeMargin.setMonthMargin(-monthIncome);
                    incomeMargin.setMonthRate(0.00);
                    incomeMargin.setBusinessLine(businessLine);
                    incomeMargin.setDate(date);
                    if(businessIncomeMargin.getMonth() != null && !businessIncomeMargin.getMonth().equals("")){
                        incomeMargin.setMonth(businessIncomeMargin.getMonth()+"");
                        businessIncomeMonthMarginMapper.insert(incomeMargin);
                    }else if((businessIncomeMargin.getStartMonth() != null && !businessIncomeMargin.getStartMonth().equals(""))
                            ||	businessIncomeMargin.getEndMonth() != null && !businessIncomeMargin.getEndMonth().equals("") ){
                        incomeMargin.setMonth(""+businessIncomeMargin.getStartMonth()+businessIncomeMargin.getEndMonth());
                        businessIncomeSemiannualMarginMapper.insert(incomeMargin);
                    }else{
                        businessIncomeAnnualMarginMapper.insert(incomeMargin);
                    }
                }
            }
        }
    }
}
