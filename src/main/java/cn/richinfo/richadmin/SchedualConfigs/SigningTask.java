package cn.richinfo.richadmin.SchedualConfigs;

import cn.richinfo.richadmin.Entity.BiddingAndContract.BiddingInformation;
import cn.richinfo.richadmin.Entity.BiddingAndContract.ContractInformation;
import cn.richinfo.richadmin.Entity.project.ApprovalInformation;
import cn.richinfo.richadmin.Mapper.BiddingAndContract.BiddingInformationMapper;
import cn.richinfo.richadmin.Mapper.BiddingAndContract.ContractInformationMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.ApprovalInformationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;


/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Component
public class SigningTask {

    private static final Logger logger = LoggerFactory.getLogger(SigningTask.class);

    @Autowired
    private ApprovalInformationMapper approvalInformationMapper;

    @Autowired
    private BiddingInformationMapper biddingInformationMapper;

    @Autowired
    private ContractInformationMapper contractInformationMapper;


    public void run() throws ParseException {
        logger.info("定时计算投标与合同中的签单额与业务净收入任务开始....................");
        Calendar c = Calendar.getInstance();
        int years = c.get(Calendar.YEAR);
        String startDate = years - 1 + "-01-01 00:00:00";
        String stopDate = years + "-12-31 23:59:59";
        List<BiddingInformation> listBidding = biddingInformationMapper.selectSiging();
        List<ContractInformation> listContract = contractInformationMapper.selectSiging(startDate, stopDate);
        if (listBidding != null && listBidding.size() > 0) {
            for (int i = 0; i < listBidding.size(); i++) {
                double sigingAmount = 0.0; // 签单额
                double outsourcingCost = 0.0; // 第三方外包成本
                double purchasingCost = 0.0; // 第三方采购成本
                int year = listBidding.get(i).getProjectTerm();
                int yearDiff = year % 12 == 0 ? year / 12 : (year / 12) + 1;
                if(yearDiff == 0){
                    yearDiff = 1;
                }
                BiddingInformation biddinformation = new BiddingInformation();
                biddinformation.setBidId(listBidding.get(i).getBidId());
                ContractInformation con = new ContractInformation();
                ApprovalInformation app = new ApprovalInformation();
                if(listBidding.get(i).getProjectNum() != null && !listBidding.get(i).getProjectNum().trim().equals("")){
                    con = contractInformationMapper.getContarctByProjectNum(listBidding.get(i).getProjectNum());
                    app = approvalInformationMapper.selectImplementProject(listBidding.get(i).getProjectNum());
                }else{
                    con = contractInformationMapper.getContractFormSingning(listBidding.get(i).getProjectName());
                    app = approvalInformationMapper.selectByProjectName(listBidding.get(i).getProjectName());
                }
                // 过路单计算方法
                if (listBidding.get(i).getIsOutsourcing().equals("过路单")) {
                    if (con != null) {
                        sigingAmount = con.getContractAmount();
                        biddinformation.setSigningAmont(sigingAmount);
                    } else {
                        sigingAmount = listBidding.get(i).getFinalOffer();
                        biddinformation.setSigningAmont(sigingAmount);
                    }
                    double taxRate =0.0;
                    if(!listBidding.get(i).getTaxRate().equals("/")){
                        taxRate = Double.parseDouble(listBidding.get(i).getTaxRate());
                    }
                    double netBusinessIncome = listBidding.get(i).getFinalOffer() / (1+taxRate) - listBidding.get(i).getBudgetedCost();
                    biddinformation.setNetBusinessIncome(netBusinessIncome);
                }else {
                    // 判断中标项目的合同是否签订，如果已签订则签单额=合同金额/合同年限
                    // 如果合同无起始结束时间则 签单额=合同额
                    //2018年4月25日晚上7点47分 徐双说明签单额不用除以年限	净收入需要除以年限
                    if (con != null) {
                        if (con.getEstimateContractAmount() != null) {
                            sigingAmount = con.getEstimateContractAmount() ;
                        } else {
                            sigingAmount = con.getContractAmount();
                        }
                        biddinformation.setSigningAmont(sigingAmount);
                        // 判断项目是否立项，以拿到项目的第三方采购成本与第三方外包成本
                        //徐双明确第三方采购和第三方外包不需要除以年限，等到算出净收入后再除以年限
                        if (app != null) {
                            outsourcingCost = app.getOutsourcingCost() ;
                            purchasingCost = app.getPurchasingCost() ;
                        } else {
                            outsourcingCost = listBidding.get(i).getOutsourcingCost() ;
                            purchasingCost = listBidding.get(i).getPurchaseCost() ;
                        }
                    } else {
                        // 如果中标项目未签订合同则签单额=中标报价金额/中标信息中的项目周期年限
                        if (listBidding.get(i).getProjectTerm() != null
                                && !listBidding.get(i).getProjectTerm().equals("")) {
                            int projectTerm = listBidding.get(i).getProjectTerm() % 12 == 0
                                    ? listBidding.get(i).getProjectTerm() / 12
                                    : listBidding.get(i).getProjectTerm() / 12 + 1;
                            if (projectTerm != 0) {
                                if (app != null) {
                                    sigingAmount = app.getContractAmount();
                                    biddinformation.setSigningAmont(sigingAmount);
                                    outsourcingCost = app.getOutsourcingCost() ;
                                    purchasingCost = app.getPurchasingCost();
                                } else {
                                    sigingAmount = listBidding.get(i).getFinalOffer();
                                    biddinformation.setSigningAmont(sigingAmount);
                                    outsourcingCost = listBidding.get(i).getOutsourcingCost();
                                    purchasingCost = listBidding.get(i).getPurchaseCost();
                                }
                            }
                        } else {
                            if (app != null) {
                                sigingAmount = app.getContractAmount();
                                biddinformation.setSigningAmont(sigingAmount);
                                outsourcingCost = app.getOutsourcingCost();
                                purchasingCost = app.getPurchasingCost();
                            } else {
                                sigingAmount = listBidding.get(i).getFinalOffer();
                                biddinformation.setSigningAmont(sigingAmount);
                                outsourcingCost = listBidding.get(i).getOutsourcingCost();
                                purchasingCost = listBidding.get(i).getPurchaseCost();
                            }
                        }
                    }
                    double a = outsourcingCost / sigingAmount;
                    if (a < 0.2) {
                        biddinformation.setNetBusinessIncome((sigingAmount - outsourcingCost - purchasingCost)/yearDiff);
                    }
                    if (a >= 0.2 && a < 0.6) {
                        biddinformation.setNetBusinessIncome((sigingAmount - purchasingCost)/yearDiff * 0.8 );
                    }
                    if (a >= 0.6) {
                        biddinformation.setNetBusinessIncome((sigingAmount - purchasingCost)/yearDiff * 0.6 );
                    }
                }
                biddingInformationMapper.updateByPrimaryKeySelective(biddinformation);
            }
        }
        if (listContract != null && listContract.size() > 0){
            for(ContractInformation cont : listContract){
                double outsourcingCost = 0.0; // 第三方外包成本
                double purchasingCost = 0.0; // 第三方采购成本
                double otherCost =0.0;
                double sigingAmount =0.0;
                ContractInformation coni = new ContractInformation();
                coni.setContactId(cont.getContactId());
                if(cont.getEstimateContractAmount() != null){
                    sigingAmount = cont.getEstimateContractAmount();
                }else{
                    sigingAmount = cont.getContractAmount();
                }
                coni.setSigningAmont(sigingAmount);
                BiddingInformation bid = biddingInformationMapper.getSigningByName(cont.getProjectName());
                if(bid != null){
                    outsourcingCost = bid.getOutsourcingCost();
                    purchasingCost = bid.getPurchaseCost();
                    otherCost = bid.getOtherCost();
                    coni.setNetBusinessIncome(sigingAmount-outsourcingCost-purchasingCost-otherCost);
                }else{
                    coni.setNetBusinessIncome(sigingAmount);
                }
                contractInformationMapper.updateByPrimaryKeySelective(coni);
            }
        }
        logger.info("定时计算投标与合同中的签单额与业务净收入任务结束..................................");
    }
}
