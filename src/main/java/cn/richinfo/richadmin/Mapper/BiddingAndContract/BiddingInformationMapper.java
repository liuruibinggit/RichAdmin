package cn.richinfo.richadmin.Mapper.BiddingAndContract;

import cn.richinfo.richadmin.Entity.BiddingAndContract.BiddingInformation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Mapper
public interface BiddingInformationMapper {

    List<BiddingInformation> selectSiging();

    /**
     * 更新投标信息
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(BiddingInformation record);

    /**
     * 项目名称获取投标信息
     * @param projectName
     * @return
     */
    BiddingInformation getSigningByName(String projectName);
}
