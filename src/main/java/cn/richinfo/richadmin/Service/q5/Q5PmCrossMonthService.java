package cn.richinfo.richadmin.Service.q5;

import cn.richinfo.richadmin.Entity.Q5.Q5PmCrossMonth;

import java.io.IOException;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
public interface Q5PmCrossMonthService {

    public String getCrossMonth(String projectNum,String year,String pmNum) throws IOException;

    public void insert(Q5PmCrossMonth q5PmCrossMonth);

    public Q5PmCrossMonth select(Q5PmCrossMonth q5PmCrossMonth);

    public void delete(Q5PmCrossMonth q5PmCrossMonth);
}
