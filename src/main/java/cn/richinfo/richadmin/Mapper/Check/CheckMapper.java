package cn.richinfo.richadmin.Mapper.Check;

import cn.richinfo.richadmin.Entity.BiddingAndContract.Check;
import cn.richinfo.richadmin.common.model.check.CheckModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Mapper
public interface CheckMapper {

    double selectCustomerScore(String projectNum);

    void insertSelective(Check check);

    int getCheckListCount(CheckModel checkModel);

    List<Check> getCheckList(CheckModel checkModel, RowBounds rowBounds);

    List<Check> selectByProjectNum(String projectNum);

    void deleteByProjectNum(String projectNum);

    void updateCustomerScore(CheckModel checkModel);

}
