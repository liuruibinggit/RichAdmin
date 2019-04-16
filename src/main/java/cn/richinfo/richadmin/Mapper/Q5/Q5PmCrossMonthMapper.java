package cn.richinfo.richadmin.Mapper.Q5;

import cn.richinfo.richadmin.Entity.Q5.Q5PmCrossMonth;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Mapper
public interface Q5PmCrossMonthMapper {

    void insert(Q5PmCrossMonth param);

    Q5PmCrossMonth selectCrossMonth(Q5PmCrossMonth param);

    void delete(Q5PmCrossMonth param);

    /**
     * @param q5PmCrossMonth
     */
    List<Q5PmCrossMonth> selectCrossMonthByCondition(Q5PmCrossMonth q5PmCrossMonth);
}
