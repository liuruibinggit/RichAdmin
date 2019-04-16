package cn.richinfo.richadmin.common.vo.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/4 0004.
 */
public class DiagnosticReportVo {
    private String projectNum;
    private String projectName;

    private Integer cost;
    private int speed;
    private int manhour;
    private int payment;
    private int grossprofit;
    private int settlementr;

    private Integer costlevel;
    private String speedlevel;
    private int manhourlevel;
    private String paymentlevel;
    private int grossprofitlevel;
    private int settlementrlevel;

    private Double costValue;
    private Double speedValue;
    private Double manhourValue;
    private Double paymentValue;
    private Double grossprofitValue;
    private Double settlementrValue;

    private Date month;

    private Integer costComparedWithLastMonth; //成本和上个月的对比
    private Integer speedComparedWithLastMonth;  //进度和上个月的对比
    private Integer manhourComparedWithLastMonth; //工时和上个月的对比
    private Integer paymentComparedWithLastMonth;
    private Integer grossprofitComparedWithLastMonth;
    private Integer settlementrComparedWithLastMonth;

    List<DiagnosticTextVo> costList=new ArrayList<>();
    List<DiagnosticTextVo> speedList=new ArrayList<DiagnosticTextVo>();
    List<DiagnosticTextVo> manhourList=new ArrayList<DiagnosticTextVo>();
    List<DiagnosticTextVo> paymentList=new ArrayList<DiagnosticTextVo>();
    List<DiagnosticTextVo> grossprofitList=new ArrayList<DiagnosticTextVo>();
    List<DiagnosticTextVo> settlementrList=new ArrayList<DiagnosticTextVo>();

    List<DiagnosticScoreVo> list=new ArrayList<DiagnosticScoreVo>();

    //List<DiagnosticFeedback> feedbackList=new ArrayList<DiagnosticFeedback>();


    public Double getCostValue() {
        return costValue;
    }
    public void setCostValue(Double costValue) {
        this.costValue = costValue;
    }
    public Double getSpeedValue() {
        return speedValue;
    }
    public void setSpeedValue(Double speedValue) {
        this.speedValue = speedValue;
    }
    public Double getManhourValue() {
        return manhourValue;
    }
    public void setManhourValue(Double manhourValue) {
        this.manhourValue = manhourValue;
    }
    public Double getPaymentValue() {
        return paymentValue;
    }
    public void setPaymentValue(Double paymentValue) {
        this.paymentValue = paymentValue;
    }
    public Double getGrossprofitValue() {
        return grossprofitValue;
    }
    public void setGrossprofitValue(Double grossprofitValue) {
        this.grossprofitValue = grossprofitValue;
    }
    public Double getSettlementrValue() {
        return settlementrValue;
    }
    public void setSettlementrValue(Double settlementrValue) {
        this.settlementrValue = settlementrValue;
    }
    public Date getMonth() {
        return month;
    }
    public void setMonth(Date month) {
        this.month = month;
    }
    public Integer getCostComparedWithLastMonth() {
        return costComparedWithLastMonth;
    }
    public void setCostComparedWithLastMonth(Integer costComparedWithLastMonth) {
        this.costComparedWithLastMonth = costComparedWithLastMonth;
    }
    public Integer getSpeedComparedWithLastMonth() {
        return speedComparedWithLastMonth;
    }
    public void setSpeedComparedWithLastMonth(Integer speedComparedWithLastMonth) {
        this.speedComparedWithLastMonth = speedComparedWithLastMonth;
    }
    public Integer getManhourComparedWithLastMonth() {
        return manhourComparedWithLastMonth;
    }
    public void setManhourComparedWithLastMonth(Integer manhourComparedWithLastMonth) {
        this.manhourComparedWithLastMonth = manhourComparedWithLastMonth;
    }
    public Integer getPaymentComparedWithLastMonth() {
        return paymentComparedWithLastMonth;
    }
    public void setPaymentComparedWithLastMonth(Integer paymentComparedWithLastMonth) {
        this.paymentComparedWithLastMonth = paymentComparedWithLastMonth;
    }
    public Integer getGrossprofitComparedWithLastMonth() {
        return grossprofitComparedWithLastMonth;
    }
    public void setGrossprofitComparedWithLastMonth(
            Integer grossprofitComparedWithLastMonth) {
        this.grossprofitComparedWithLastMonth = grossprofitComparedWithLastMonth;
    }
    public Integer getSettlementrComparedWithLastMonth() {
        return settlementrComparedWithLastMonth;
    }
    public void setSettlementrComparedWithLastMonth(
            Integer settlementrComparedWithLastMonth) {
        this.settlementrComparedWithLastMonth = settlementrComparedWithLastMonth;
    }
    public List<DiagnosticScoreVo> getList() {
        return list;
    }
    public void setList(List<DiagnosticScoreVo> list) {
        this.list = list;
    }
    public String getProjectNum() {
        return projectNum;
    }
    public void setProjectNum(String projectNum) {
        this.projectNum = projectNum;
    }
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getCost() {
        return cost;
    }
    public void setCost(Integer cost) {
        this.cost = cost;
    }
    public Integer getCostlevel() {
        return costlevel;
    }
    public void setCostlevel(Integer costlevel) {
        this.costlevel = costlevel;
    }
    public List<DiagnosticTextVo> getCostList() {
        return costList;
    }
    public void setCostList(List<DiagnosticTextVo> costList) {
        this.costList = costList;
    }
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getManhour() {
        return manhour;
    }
    public void setManhour(int manhour) {
        this.manhour = manhour;
    }
    public int getPayment() {
        return payment;
    }
    public void setPayment(int payment) {
        this.payment = payment;
    }
    public int getGrossprofit() {
        return grossprofit;
    }
    public void setGrossprofit(int grossprofit) {
        this.grossprofit = grossprofit;
    }
    public int getSettlementr() {
        return settlementr;
    }
    public void setSettlementr(int settlementr) {
        this.settlementr = settlementr;
    }

    public int getManhourlevel() {
        return manhourlevel;
    }
    public void setManhourlevel(int manhourlevel) {
        this.manhourlevel = manhourlevel;
    }

    public String getSpeedlevel() {
        return speedlevel;
    }
    public void setSpeedlevel(String speedlevel) {
        this.speedlevel = speedlevel;
    }
    public String getPaymentlevel() {
        return paymentlevel;
    }
    public void setPaymentlevel(String paymentlevel) {
        this.paymentlevel = paymentlevel;
    }
    public int getGrossprofitlevel() {
        return grossprofitlevel;
    }
    public void setGrossprofitlevel(int grossprofitlevel) {
        this.grossprofitlevel = grossprofitlevel;
    }
    public int getSettlementrlevel() {
        return settlementrlevel;
    }
    public void setSettlementrlevel(int settlementrlevel) {
        this.settlementrlevel = settlementrlevel;
    }
    public List<DiagnosticTextVo> getSpeedList() {
        return speedList;
    }
    public void setSpeedList(List<DiagnosticTextVo> speedList) {
        this.speedList = speedList;
    }
    public List<DiagnosticTextVo> getManhourList() {
        return manhourList;
    }
    public void setManhourList(List<DiagnosticTextVo> manhourList) {
        this.manhourList = manhourList;
    }
    public List<DiagnosticTextVo> getPaymentList() {
        return paymentList;
    }
    public void setPaymentList(List<DiagnosticTextVo> paymentList) {
        this.paymentList = paymentList;
    }
    public List<DiagnosticTextVo> getGrossprofitList() {
        return grossprofitList;
    }
    public void setGrossprofitList(List<DiagnosticTextVo> grossprofitList) {
        this.grossprofitList = grossprofitList;
    }
    public List<DiagnosticTextVo> getSettlementrList() {
        return settlementrList;
    }
    public void setSettlementrList(List<DiagnosticTextVo> settlementrList) {
        this.settlementrList = settlementrList;
    }
//    public List<DiagnosticFeedback> getFeedbackList() {
//        return feedbackList;
//    }
//    public void setFeedbackList(List<DiagnosticFeedback> feedbackList) {
//        this.feedbackList = feedbackList;
//    }




}
