package cn.richinfo.richadmin.Mapper.BiddingAndContract;

import cn.richinfo.richadmin.Entity.BiddingAndContract.ContractInformation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Mapper
public interface ContractInformationMapper {

    /**
     * 查询时间段内的合同
     * @param startTime  起始时间
     * @param stopTime   终止时间
     * @return
     */
    List<ContractInformation> selectSiging(@Param("startTime")String startTime, @Param("stopTime") String stopTime);

    /**
     * 根据项目编号获取合同信息
     * @param projectNum 项目编号
     * @return
     */
    ContractInformation getContarctByProjectNum(String projectNum);

    /**
     * 根据项目名称获取合同信息
     * @param projectName 项目名称
     * @return
     */
    ContractInformation getContractFormSingning(String projectName);

    /**
     * 更新操作
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ContractInformation record);

    ContractInformation selectExcessGrossProfit2(String projectName) ;
}
