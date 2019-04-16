package cn.richinfo.richadmin.ExceptionHandle;

import lombok.Data;

/**
 * Created by LiuRuibing on 2019/4/9 0009.
 */
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS("0", "success"),
    /**
     * 未知错误
     */
    UNKNOWN_ERROR("0x10001", "unkonwn error"),

    /**
     * 用户名错误或不存在
     */
    USERNAME_ERROR("0x10002", "username error or does not exist"),

    /**
     * 密码错误
     */
    PASSWORD_ERROR("0x10003", "password error"),

    /**
     * 用户名不能为空
     */
    USERNAME_EMPTY("0x10004", "username can not be empty"),

    /**
     * 用户名不能为空
     */
    ART_EXCEPTION("0x10005", "ArithmeticException");

    /**
     * 结果码
     */
    private String code;

    /**
     * 结果码描述
     */
    private String msg;
    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
