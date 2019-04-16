package cn.richinfo.richadmin.Mapper.BusinessLine;

import cn.richinfo.richadmin.Entity.businessLine.BusinessLine;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/12 0012.
 */
@Mapper
public interface BusinessLineMapper {

    public void addBusinessLine(BusinessLine businessLine);

    public BusinessLine queryBusinessLineName(String businessLineName);

    public void delBusinessLine(String businessLineName);

    public void updateBusinessLine(BusinessLine businessLine);

    public List<BusinessLine> getBusinessLineList();

    public List<String> getBusinessLineName();

    public BusinessLine queryById(String businessLineId);

    public BusinessLine queryByName(String businessLine);
}
