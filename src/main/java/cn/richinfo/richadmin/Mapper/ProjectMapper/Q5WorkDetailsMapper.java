package cn.richinfo.richadmin.Mapper.ProjectMapper;

import cn.richinfo.richadmin.Entity.project.Q5WorkDetails;
import cn.richinfo.richadmin.common.model.project.HoursModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by LiuRuibing on 2019/4/8 0008.
 */
@Mapper
public interface Q5WorkDetailsMapper {

    int deleteByPrimaryKey(Integer pId);

    int insertSelective(Q5WorkDetails record);

    List<Q5WorkDetails> selectByPrimaryKey(Q5WorkDetails q5WorkDetails);

    int updateByPrimaryKeySelective(Q5WorkDetails record);

    void deleteDetails(Q5WorkDetails q5WorkDetails);

    List<Q5WorkDetails> selectByNumber(@Param("employeeNumber") String employeeNumber);

    Double selectAllHours(Q5WorkDetails q5WorkDetails);

    double selectAllHours1(Q5WorkDetails q5WorkDetails);

    List<Q5WorkDetails> selectByProjectManagerAndYear(Map<String, String> map);

    List<Q5WorkDetails> getHoursByYearAndWorkName(HoursModel hoursModel);

    List<Q5WorkDetails> selectHoursByProjectName(HoursModel hoursModel);

    List<Q5WorkDetails> selectHoursByMonth(HoursModel hoursModel);

    List<Q5WorkDetails> getHoursByYearAndWorkNameAndProject(HoursModel hoursModel);

    List<Q5WorkDetails> selectHoursByMonthAndProject(HoursModel hours);

    Double selectAllHours2(HoursModel hours);

    List<Q5WorkDetails> selectQ5WorkDetailsByEmployeeNumber(Q5WorkDetails workDetails);

    List<Map<String,String>> selectQ5WorkDetailsByEmployeeNumberList(@Param("workDetailsList") List<String> workDetailsList);



}
