package com.joe.datanode.server;

public class DataNode {

    //是否要运行的标志位
    private volatile boolean shouldRun = false;
    //对NameNode进行通信的对象
    private NameNodeOfferService nameNodeGroupOfferService;

    public DataNode(){
    }

    private void initialize(){
        shouldRun = true;
        //调用通信组件执行方法
        nameNodeGroupOfferService = new NameNodeOfferService();
        nameNodeGroupOfferService.run();
    }

    /**
     * DataNode运行
     */
    public void run(){
        while (shouldRun){
            try{
                Thread.sleep(1000L);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        DataNode dataNode = new DataNode();
        dataNode.initialize();
        dataNode.run();
    }

}
