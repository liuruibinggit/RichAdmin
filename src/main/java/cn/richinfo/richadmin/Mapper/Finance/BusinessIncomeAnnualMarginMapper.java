package cn.richinfo.richadmin.Mapper.Finance;

import cn.richinfo.richadmin.Entity.finance.BusinessIncomeMargin;
import cn.richinfo.richadmin.common.vo.finance.BusinessTotalVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/9 0009.
 */
@Mapper
public interface BusinessIncomeAnnualMarginMapper {

    void insert(BusinessIncomeMargin businessIncomeMargin);

    List<BusinessIncomeMargin> list(BusinessIncomeMargin businessIncomeMargin);

    List<BusinessIncomeMargin> list(BusinessIncomeMargin businessIncomeMargin,RowBounds row);

    BusinessTotalVo getTotalList(BusinessIncomeMargin businessIncomeMargin);

    //	Double getDeferred(BusinessIncomeMargin businessIncomeMargin);
    void deleteByDate(@Param("date") String date);
}
