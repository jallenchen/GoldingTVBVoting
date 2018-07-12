package com.golding.tvbvotingsystem.bean;

public class EventBusCMD {
    int cmd ;

    public EventBusCMD(int cmd){
        this.cmd = cmd;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }
}
