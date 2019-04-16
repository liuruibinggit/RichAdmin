package cn.richinfo.richadmin.common.utils;

import java.math.BigDecimal;

/**
 * Created by LiuRuibing on 2019/4/4 0004.
 */
public class DoubleUtil {
    public static double doubleToTwo(double db){
        BigDecimal bg = new BigDecimal(db);
        double f1 = bg.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }
}
