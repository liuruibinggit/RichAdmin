package cn.richinfo.richadmin.Controller.Financial;

import cn.richinfo.richadmin.Entity.project.ResultVo;
import cn.richinfo.richadmin.ExceptionHandle.ResultCode;
import cn.richinfo.richadmin.Service.finance.BusinessIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LiuRuibing on 2019/4/9 0009.
 */
@Controller
@RequestMapping(value = "/businessIncome")
public class BusinessIncomeController {

    @Autowired
    private BusinessIncomeService businessIncomeService;
    /**
     * 计算业务净收入本月差额
     */
    @RequestMapping(value = "/addBusinessIncomeMonthMargin")
    @ResponseBody
    public ResultVo addBusinessIncomeMonthMargin(){
        businessIncomeService.addBusinessIncomeMonthMargin();
        return new ResultVo(ResultCode.SUCCESS);
    }
}
