package com.joe.datanode.server;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * DataNode与NameNode集群通信的组件
 */
public class NameNodeOfferService {

    //与active的NameNod的对象
    private NameNodeServiceActor activeNameNodeServiceActor;
    //与standby的NameNod的对象
    private NameNodeServiceActor standbyNameNodeServiceActor;
    //nameNode通信对象集合
    private CopyOnWriteArrayList<NameNodeServiceActor> nameNodeServiceActors;


    public NameNodeOfferService(){
        activeNameNodeServiceActor = new NameNodeServiceActor();
        standbyNameNodeServiceActor = new NameNodeServiceActor();
        nameNodeServiceActors = new CopyOnWriteArrayList<NameNodeServiceActor>();
        nameNodeServiceActors.add(activeNameNodeServiceActor);
        nameNodeServiceActors.add(standbyNameNodeServiceActor);
    }

    /**
     * 通信主键
     */
    public void start(){
        register();
    }

    /**
     * 向NameNode集群进行注册
     */
    private void register(){
        try {
            System.out.println("将要进行各个NN节点的注册");
            CountDownLatch countDownLatch = new CountDownLatch(2);
            activeNameNodeServiceActor.register(countDownLatch);
            standbyNameNodeServiceActor.register(countDownLatch);
            //阻塞等待各个NameNode节点都注册完成
            countDownLatch.await();
            System.out.println("所有NN节点都注册完成...");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 对对应的nameNodeActore工具进行删除
     * @param nameNodeServiceActor
     */
    public void shutDown(NameNodeServiceActor nameNodeServiceActor){
        this.nameNodeServiceActors.remove(nameNodeServiceActor);
    }

    /**
     * 迭代nameNodeServiceActor
     */
    public void iteratorNameNodeServiceActor(){
        Iterator<NameNodeServiceActor> iterator = this.nameNodeServiceActors.iterator();
        while (iterator.hasNext()){
            iterator.next();
        }
    }

//    public

    private void heartBeat(){
        this.activeNameNodeServiceActor.heartBeat();
        this.standbyNameNodeServiceActor.heartBeat();
    }


}
