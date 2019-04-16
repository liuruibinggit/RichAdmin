package cn.richinfo.richadmin.Service.q5.Q5ProjectServiceImpl;

import cn.richinfo.richadmin.Entity.Q5.Q5Didle;
import cn.richinfo.richadmin.Mapper.Q5.Q5DidleMapper;
import cn.richinfo.richadmin.Service.q5.Q5DidleService;
import cn.richinfo.richadmin.common.utils.ConnectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/12 0012.
 */
@Service
public class Q5DidleServiceImpl implements Q5DidleService {

    @Autowired
    private Q5DidleMapper q5DidleMapper;

    @Autowired
    private Environment environment;

    /**
     * 按月获取业务单元空转信息
     */
    public String getDidle(String unitnum, String year, String month) throws IOException{
        String path = environment.getProperty("Q5_url") + "?action=" + environment.getProperty("didleAction") + "&unitnum="
                + unitnum + "&year=" + year + "&month=" + month;
        return ConnectUtil.sendPost(path);
    }

    public List<Q5Didle> queryByDate(Q5Didle q5Didle) {
        return q5DidleMapper.queryByDate(q5Didle);
    }

    public void delDidleInfo(List<Q5Didle> q5Didles) {
        for (Q5Didle q5Didle : q5Didles) {
            q5DidleMapper.delDidleInfo(q5Didle);
        }
    }

    public void addDidleInfo(Q5Didle q5Didle){
        q5DidleMapper.addDidleInfo(q5Didle);
    }
}
