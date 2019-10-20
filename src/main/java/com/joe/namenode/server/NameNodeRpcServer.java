package com.joe.namenode.server;

/**
 *
 */
public class NameNodeRpcServer {

    private FsNameSystem fsNameSystem;

    public NameNodeRpcServer(FsNameSystem fsNameSystem){
        this.fsNameSystem = fsNameSystem;
    }

    public boolean mkdir(String path){
        this.fsNameSystem.mkdir(path);
        return true;
    }

    public void start(){
        System.out.println("rpc 服务启动！");
    }

}
