package cn.richinfo.richadmin.Entity.project;

import cn.richinfo.richadmin.ExceptionHandle.ResultCode;

/**
 * Created by Administrator on 2019/4/2 0002.
 * 返回结果VO
 */
public class ResultVo<T> {

    /**
     * 结果码
     */
    private String code;

    /**
     * 结果码描述
     */
    private String msg;

    private Object data;

    public ResultVo(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultVo(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVo() {

    }

    public ResultVo(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    /**
     * ResultVo, 并返回
     * @param resultCode
     * @return
     */
    public static ResultVo of(ResultCode resultCode) {
        return new ResultVo(resultCode);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
