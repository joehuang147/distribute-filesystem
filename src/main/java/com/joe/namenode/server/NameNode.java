package com.joe.namenode.server;

/**
 * hdfs核心启动类
 */
public class NameNode extends Thread{

    //控制进程是否运行，必须volatile
    private volatile boolean shutRun = false;
    //文件目录树操作对象
    private FsNameSystem fsNameSystem;
    //接收rpc调用对象
    private NameNodeRpcServer nameNodeRpcServer;

    public NameNode(){
        shutRun = true;
    }

    /**
     * 初始化核心管理组件
     */
    public void init(){
        this.fsNameSystem = new FsNameSystem();
        this.nameNodeRpcServer = new NameNodeRpcServer(fsNameSystem);
    }

    @Override
    public void run() {
        while (shutRun){
            try {
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 对外重要的main方法，hdfs进行启动和初始化
     * @param args
     */
    public static void main(String[] args){
        NameNode nameNode = new NameNode();
        nameNode.init();
        nameNode.start();
    }

}
