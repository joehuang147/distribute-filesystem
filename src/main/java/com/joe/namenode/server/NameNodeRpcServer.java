package com.joe.namenode.server;

/**
 *
 */
public class NameNodeRpcServer {

    private FsNameSystem fsNameSystem;
    private DataNodeManager dataNodeManager;

    public NameNodeRpcServer(FsNameSystem fsNameSystem,DataNodeManager dataNodeManager){
        this.fsNameSystem = fsNameSystem;
        this.dataNodeManager = dataNodeManager;
    }

    public boolean mkdir(String path){
        this.fsNameSystem.mkdir(path);
        return true;
    }

    public void start(){
        System.out.println("rpc 服务启动！");
    }

    /**
     * 接收datanode的注册请求
     * @param ip
     * @param hostName
     */
    public void register(String ip,String hostName){
        dataNodeManager.register(ip,hostName);
    }

    /**
     * 接收datanode的心跳
     * @param ip
     * @param hostName
     */
    public void heartbeat(String ip,String hostName){
        dataNodeManager.heartbeat(ip,hostName);
    }

}
