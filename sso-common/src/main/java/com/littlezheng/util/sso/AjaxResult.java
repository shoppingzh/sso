package com.littlezheng.util.sso;

public class AjaxResult {

    private String code;
    private String message;

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

    @Override
    public String toString() {
        return "AjaxResult [code=" + code + ", message=" + message + "]";
    }

}
