package cn.richinfo.richadmin.Service.q5;

import cn.richinfo.richadmin.Entity.Q5.Q5Didle;

import java.io.IOException;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/12 0012.
 */
public interface Q5DidleService {

    /**
     * 按月获取业务单元空转信息
     */
    public String getDidle(String unitnum, String year, String month) throws IOException;

    public List<Q5Didle> queryByDate(Q5Didle q5Didle);

    public void delDidleInfo(List<Q5Didle> q5Didles);

    public void addDidleInfo(Q5Didle q5Didle);
}
