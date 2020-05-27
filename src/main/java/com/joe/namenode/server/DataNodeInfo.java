package com.joe.namenode.server;

/**
 * Created by joehuang on 2020/5/26.
 */
public class DataNodeInfo {

    private String ip;
    private String hostname;
    private long lastHeartbeatTime;

    public DataNodeInfo(String ip, String hostname,long lastHeartbeatTime) {
        this.ip = ip;
        this.hostname = hostname;
        this.lastHeartbeatTime = lastHeartbeatTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public long getLastHeartbeatTime() {
        return lastHeartbeatTime;
    }

    public void setLastHeartbeatTime(long lastHeartbeatTime) {
        this.lastHeartbeatTime = lastHeartbeatTime;
    }
}
