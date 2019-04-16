package cn.richinfo.richadmin.Mapper.Check;

import cn.richinfo.richadmin.Entity.check.ManagerCheck;
import cn.richinfo.richadmin.common.model.check.ManagerCheckModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by LiuRuibing on 2019/4/15 0015.
 */
@Mapper
public interface ManagerCheckMapper {
    ManagerCheck selectByPrimaryKeyAndYear(Map<String, String> map1);

    void insertSelective(ManagerCheck managerCheck);

    void deleteByProjectManagerAndYear(Map<String, String> map);

    List<ManagerCheck> getManagerCheckList(ManagerCheckModel managerCheckModel);

    List<ManagerCheck> getManagerCheckList(ManagerCheckModel managerCheckModel,RowBounds rowBounds);

    void updateByPrimaryKeySelective(ManagerCheckModel managerCheckModel);
}
