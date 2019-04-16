package cn.richinfo.richadmin.Service.businessLine;

import cn.richinfo.richadmin.Entity.businessLine.BusinessLine;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/12 0012.
 */
public interface BusinessLineService {

    /*
	 * 添加部门
	 */
    public void addBusinessLine(BusinessLine businessLine);
    /*
     * 通过部门名称查询
     */
    public BusinessLine queryBusinessLineName(String businessLineName);
    /*
     * 通过部门Id查询
     */
    public BusinessLine queryById(String businessLineId) ;

    /*
     * 删除部门
     */
    public void delBusinessLine(String businessLineName);

    /*
     * 更新部门
     */
    public void updateBusinessLine(BusinessLine businessLine);
    /**
     * 获取所有的业务线
     * @return
     */
    public List<String> getdeptNameList();

    public List<BusinessLine> getBusinessLineList();
}
