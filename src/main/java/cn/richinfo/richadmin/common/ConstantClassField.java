package cn.richinfo.richadmin.common;

/**
 * Created by Administrator on 2019/4/1 0001.
 */
public class ConstantClassField {


    public static final String TESTSCHEDUAL = "0/2 * * * * *";//每2秒执行一次
    /**
     * 项目诊断定时任务：默认每天凌晨1点执行
     */
    public static final String DIAGNOSTICPROJECTS="0 0 1 * * ?";//每天凌晨1点执行

    /**
     * 定时获取项目信息库（默认每隔八小时执行一次）
     */
    public  static final String UPDATEPROJECTLIBRARYDATACRON="0 0 0/8 * * ?";

    /**
     * 获取公司及各个单元各个维度平均得分计算
     */
    public static final String GETCORPAVGSECORETASK="0 0 23 28 * ?";

    /**
     *获取项目滚动工时预算
     */
    public static final String  GETINCREASEHOURSDATE="0 0 2 28 * ?";

    /**
     * 定时获取项目结束日期
     */
    public static final String GETPROJECTSTOPTIEDATE="0 0 20 10 * ?";

    /**
     * 定时取Q5系统数据  每个月5号获取上一个月的数据
     */
    public static final String TASKDATE="0 0 2 28 * ?";

    /**
     * 定时计算投标与签单额与业务净收
     */
    public static final String SIGNINGDATE="0 3 19 28 * ?";

    /**
     * 定时计算超额毛利计算表  朝九晚五工作时间内每半小时
     */
    public static final String EXCESSGROSSPROFITDATE="0 0 1 5 * ?";

    /**
     * #定时计算项目考核
     * projectCheckDate=0 0 1 5 * ?
     * managerCheckDate=0 0 1 5 * ?
     */
    public static final String PROJECTCHECKDATE="0 0 1 5 * ?";
    public static final String MANAGERCHECKDATE="0 0 1 5 * ?";

    /**
     * #定时计算考核表
     */
    public static final String CHECKDATE="0 0 12 * * ?";
    public static final String WINBIDDINGDATE="0 0 0/4 * * ?";
    public static final String VALUETASK="0 0 1 5 * ?";

    /**
     * 项目进度里程碑为：初验
     */
    public static final String CHUYAN="初验";
    /**
     * 项目进度里程碑为：终验
     */
    public static final String ZHONGYAN="终验";
    /**
     * 项目回款里程碑为：初验款
     */
    public static final String CHUYANKUAN="初验款";
    /**
     * 项目回款里程碑为：终验款
     */
    public static final String ZHONGYANKUAN="终验款";
    /**
     * 项目回款里程碑为：尾款
     */
    public static final String WEIKUAN="尾款";

}
