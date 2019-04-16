package cn.richinfo.richadmin.Mapper.Q5;

import cn.richinfo.richadmin.Entity.Q5.Q5Didle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/12 0012.
 */
@Mapper
public interface Q5DidleMapper {

    List<Q5Didle> queryByDate(Q5Didle q5Didle);

    void delDidleInfo(Q5Didle q5Didle);

    void addDidleInfo(Q5Didle q5Didle);
}
