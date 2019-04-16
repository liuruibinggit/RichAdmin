package cn.richinfo.richadmin.Mapper.ProjectMapper;

import cn.richinfo.richadmin.Entity.project.CropAvgSecore;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/12 0012.
 */
@Mapper
public interface CorpAvgSecoreMapper {

    void insert(CropAvgSecore corpAvgSecore);

    List<CropAvgSecore> selectCorpAvgSecore();
}
