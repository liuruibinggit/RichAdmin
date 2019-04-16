package cn.richinfo.richadmin.Service.businessLine.businessLineServiceImpl;

import cn.richinfo.richadmin.Entity.businessLine.BusinessLine;
import cn.richinfo.richadmin.Mapper.BusinessLine.BusinessLineMapper;
import cn.richinfo.richadmin.Service.businessLine.BusinessLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/12 0012.
 */
@Service
public class BusinessLineServiceImpl implements BusinessLineService {


    @Autowired
    private BusinessLineMapper businessLineMapper;

    /*
	 * 添加部门
	 */
    public void addBusinessLine(BusinessLine businessLine) {
        businessLineMapper.addBusinessLine(businessLine);
    }
    /*
     * 通过部门名称查询
     */
    public BusinessLine queryBusinessLineName(String businessLineName) {
        return businessLineMapper.queryBusinessLineName(businessLineName);
    }

    /*
     * 通过部门Id查询
     */
    public BusinessLine queryById(String businessLineId) {
        return businessLineMapper.queryById(businessLineId);
    }

    /*
     * 删除部门
     */
    public void delBusinessLine(String businessLineName) {
        businessLineMapper.delBusinessLine(businessLineName);
    }

    /*
     * 更新部门
     */
    public void updateBusinessLine(BusinessLine businessLine) {
        businessLineMapper.updateBusinessLine(businessLine);
    }
    /**
     * 获取所有的业务线
     * @return
     */
    public List<String> getdeptNameList() {
        return businessLineMapper.getBusinessLineName();
    }

    public List<BusinessLine> getBusinessLineList() {
        return businessLineMapper.getBusinessLineList();
    }
}
