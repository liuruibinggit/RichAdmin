package cn.richinfo.richadmin.common.utils;

import java.math.BigDecimal;

/**
 * Created by LiuRuibing on 2019/4/8 0008.
 */
public class StringHelp {
    public static double String2Double(String s){
        if(s== null || "".equals(s)){
            return 0.00;
        }else{
            double db = Double.parseDouble(s);
            BigDecimal bg = new BigDecimal(db);
            double f1 = bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            return f1;
        }
    }
}
