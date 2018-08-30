package com.littlezheng.util;

public class AjaxResult {

    private String code;
    private String message;
    private Object data;

    public AjaxResult() {
        super();
    }

    public AjaxResult(String code) {
        super();
        this.code = code;
    }

    public AjaxResult(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public AjaxResult(String code, String message, Object data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AjaxResult [code=" + code + ", message=" + message + ", data=" + data + "]";
    }

}
