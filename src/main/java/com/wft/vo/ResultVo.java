package com.wft.vo;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * 返回信息
 *
 * @author hupan
 */
public class ResultVo<T> implements Serializable {

    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 返回代码
     */
    private Integer code;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 返回数据
     */
    private T data;

    /**
     * 成功状态码
     */
    private static int SUCCESS_CODE = 0;
    /**
     * 失败状态码
     */
    private static int FAIL_CODE = -1;

    private ResultVo() {
        this.code = -1;
        this.msg = "";
    }

    private ResultVo(boolean success, Integer code, String msg) {
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    private ResultVo(boolean success, Integer code, String msg, T data) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private ResultVo<T> clear() {
        this.code = -1;
        this.msg = "";
        this.data = null;

        return this;
    }

    public static <O> ResultVo<O> success(String msg) {
        return new ResultVo<O>(true, SUCCESS_CODE, msg);
    }

    public static <O> ResultVo<O> success(String msg, O data) {
        return new ResultVo<O>(true, SUCCESS_CODE, msg, data);
    }

    public static ResultVo fail(String msg) {
        return new ResultVo(false, FAIL_CODE, msg);
    }

    public static <O> ResultVo<O> fail(String msg, O data) {
        return new ResultVo<O>(false, FAIL_CODE, msg, data);
    }

    public static <O> ResultVo build(boolean success, Integer code, String msg, O data) {
        return new ResultVo<O>(success, code, msg, data);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{\"code\":" + this.code + ",\"msg\":\"" + this.msg + "\",\"data\":" + JSON.toJSON(this.data) + "}";
    }

}