package cn.richinfo.richadmin.Mapper.ProjectValue;

import cn.richinfo.richadmin.Entity.projectValue.ProjectMonthValue;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by LiuRuibing on 2019/4/16 0016.
 */
@Mapper
public interface ProjectMonthValueMapper {

    void insert(ProjectMonthValue projectMonthValue);
}
