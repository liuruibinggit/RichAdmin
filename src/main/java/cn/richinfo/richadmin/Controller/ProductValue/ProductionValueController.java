package cn.richinfo.richadmin.Controller.ProductValue;

import cn.richinfo.richadmin.Entity.project.ResultVo;
import cn.richinfo.richadmin.ExceptionHandle.ResultCode;
import cn.richinfo.richadmin.Service.productValue.ProjectLibraryService;
import cn.richinfo.richadmin.common.ConstantClassField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by: LiuRuibing
 * @date:2019/4/10
 * @desc:产值计算相关
 */
@RestController
@RequestMapping(value = "/productValue")
public class ProductionValueController {

    @Autowired
    private ProjectLibraryService projectLibraryService;

    /**
     * 获取基础信息库的任务（开启定时获取即可）
     * 可以手动调用和定时调用
     * @return
     */
    @RequestMapping(value = "/getInfoToProjectLiarary")
    @Scheduled(cron = ConstantClassField.UPDATEPROJECTLIBRARYDATACRON)
    public ResultVo updateProjectLibraryData(){
        projectLibraryService.addProjectLibraryInfo();
        return  new ResultVo("0","获取基础信息库的任务结束。。。。。。");
    }
}
