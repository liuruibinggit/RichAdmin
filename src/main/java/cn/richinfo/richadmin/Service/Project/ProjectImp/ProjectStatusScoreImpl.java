package cn.richinfo.richadmin.Service.Project.ProjectImp;

import cn.richinfo.richadmin.Mapper.ProjectMapper.ProjectStatusSecoreMapper;
import cn.richinfo.richadmin.Mapper.ProjectMapper.ProjectStatusSecoreMapper;
import cn.richinfo.richadmin.Service.Project.ProjectStatusScoreService;
import cn.richinfo.richadmin.Service.Project.ProjectStatusScoreService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2019/4/2 0002.
 */
@Service
public class ProjectStatusScoreImpl implements ProjectStatusScoreService {

    @Autowired
    private ProjectStatusSecoreMapper projectStatusSecoreMapper;

    @Override
    public void deleteProjectStatusSecoreByYearAndMonth(@Param("yearAndMonth") String yearAndMonth) {
        projectStatusSecoreMapper.deleteProjectStatusSecoreByYearAndMonth(yearAndMonth);
    }
}
