package cn.richinfo.richadmin.common.vo.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LiuRuibing on 2019/4/4 0004.
 */
public class DiagnosticLevelVo {

    List<Map<?,?>> costList=new ArrayList<Map<?,?>>();  //成本等级list
    List<Map<?,?>> speedList=new ArrayList<Map<?,?>>();  //进度等级list
    List<Map<?,?>> manhourList=new ArrayList<Map<?,?>>();
    List<Map<?,?>> paymentList=new ArrayList<Map<?,?>>();
    List<Map<?,?>> grossprofitList=new ArrayList<>();
    List<Map<?,?>> settlementrList=new ArrayList<Map<?,?>>();
    public List<Map<?, ?>> getCostList() {
        return costList;
    }
    public void setCostList(List<Map<?, ?>> costList) {
        this.costList = costList;
    }
    public List<Map<?, ?>> getSpeedList() {
        return speedList;
    }
    public void setSpeedList(List<Map<?, ?>> speedList) {
        this.speedList = speedList;
    }
    public List<Map<?, ?>> getManhourList() {
        return manhourList;
    }
    public void setManhourList(List<Map<?, ?>> manhourList) {
        this.manhourList = manhourList;
    }
    public List<Map<?, ?>> getPaymentList() {
        return paymentList;
    }
    public void setPaymentList(List<Map<?, ?>> paymentList) {
        this.paymentList = paymentList;
    }
    public List<Map<?, ?>> getGrossprofitList() {
        return grossprofitList;
    }
    public void setGrossprofitList(List<Map<?, ?>> grossprofitList) {
        this.grossprofitList = grossprofitList;
    }
    public List<Map<?, ?>> getSettlementrList() {
        return settlementrList;
    }
    public void setSettlementrList(List<Map<?, ?>> settlementrList) {
        this.settlementrList = settlementrList;
    }
}
