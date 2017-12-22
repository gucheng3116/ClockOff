package com.gucheng.clockoff;

/**
 * Created by liuwei on 2017/12/22.
 */

public class MessageEvent {
    private String mMsg;
    public MessageEvent(String msg) {
        mMsg = msg;
    }
    public String getMsg() {
        return mMsg;
    }
    public void setMsg(String msg) {
        mMsg = msg;
    }
}
