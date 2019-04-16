package cn.richinfo.richadmin.common.vo.project;

import cn.richinfo.richadmin.Entity.project.Employee;
import lombok.Data;

/**
 * Created by LiuRuibing on 2019/4/8 0008.
 */
@Data
public class Q5GetDailyReport {
    private int state;
    private String ErrorMsg;
    private String Number;
    private String Name;
    private int Count;
    private String ProMonths;
    private String CrossMonths;
    private String PMMonths;

    private java.util.List<Employee> List;
}
