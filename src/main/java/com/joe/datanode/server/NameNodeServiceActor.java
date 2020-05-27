package com.joe.datanode.server;

import java.util.concurrent.CountDownLatch;

/**
 * 与各个NameNode节点通信的组件
 */
public class NameNodeServiceActor {

    /**
     * 注册方法
     * @param countDownLatch
     */
    public void register(CountDownLatch countDownLatch){
        RegisterThread registerThread = new RegisterThread(countDownLatch);
        registerThread.start();
    }

    public void heartBeat(){
        new HeartBeatTherad().start();
    }

    /**
     * 注册线程类
     */
    class RegisterThread extends Thread{

        //阻塞并发组件
        CountDownLatch countDownLatch ;

        public RegisterThread(CountDownLatch countDownLatch){
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try{
                System.out.println("准备向NameNode发送rpc注册...");
                String ip = "127.0.0.1";
                String hostName = "dfs-data-01";

                Thread.sleep(1000L);
                System.out.println("完成注册...");
                //发送注册完成的信号
                countDownLatch.countDown();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    class HeartBeatTherad extends Thread{
        @Override
        public void run() {
            while (true){
                try {
                    String ip = "127.0.0.1";
                    String hostName = "dfs-data-01";
                    Thread.sleep(1000L);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


}
