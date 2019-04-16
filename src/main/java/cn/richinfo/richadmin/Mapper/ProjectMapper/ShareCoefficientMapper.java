package cn.richinfo.richadmin.Mapper.ProjectMapper;

import cn.richinfo.richadmin.Entity.project.ShareCoefficient;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by LiuRuibing on 2019/4/2 0002.
 */
@Mapper
public interface ShareCoefficientMapper {

    /**
     * 根据部门名称获取公摊系数  按时间降序
     * @param businessLine
     * @return List<ShareCoefficient>
     */
    public List<ShareCoefficient> selectShareByDeptName(String businessLine);
}
