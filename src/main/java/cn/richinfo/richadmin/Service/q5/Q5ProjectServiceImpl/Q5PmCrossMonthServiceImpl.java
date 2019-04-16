package cn.richinfo.richadmin.Service.q5.Q5ProjectServiceImpl;

import cn.richinfo.richadmin.Entity.Q5.Q5PmCrossMonth;
import cn.richinfo.richadmin.Entity.project.ApprovalInformation;
import cn.richinfo.richadmin.Entity.project.Q5Project;
import cn.richinfo.richadmin.Mapper.Q5.Q5PmCrossMonthMapper;
import cn.richinfo.richadmin.Service.q5.Q5PmCrossMonthService;
import cn.richinfo.richadmin.Service.q5.Q5ProjectService;
import cn.richinfo.richadmin.common.utils.ConnectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Service
public class Q5PmCrossMonthServiceImpl implements Q5PmCrossMonthService {

    @Autowired
    private Q5PmCrossMonthMapper q5PmCrossMonthMapper;

    @Autowired
    private Environment environment;

    @Override
    public String getCrossMonth(String projectNum,String year,String pmNum)throws IOException{
        String path = environment.getProperty("Q5_url") + "?action=" + environment.getProperty("crossAction") + "&pronum="
                + projectNum + "&year=" + year + "&empnum=" + pmNum;
        return ConnectUtil.sendPost(path);
    }

    @Override
    public void insert(Q5PmCrossMonth q5PmCrossMonth){
        q5PmCrossMonthMapper.insert(q5PmCrossMonth);
    }

    @Override
    public Q5PmCrossMonth select(Q5PmCrossMonth q5PmCrossMonth){
        return q5PmCrossMonthMapper.selectCrossMonth(q5PmCrossMonth);
    }

    @Override
    public void delete(Q5PmCrossMonth q5PmCrossMonth){
        q5PmCrossMonthMapper.delete(q5PmCrossMonth);
    }
}
