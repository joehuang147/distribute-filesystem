package com.joe.namenode.server;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by joehuang on 2020/5/26.
 */
public class DataNodeManager {

    private Map<String,DataNodeInfo> datanodes = new ConcurrentHashMap<String, DataNodeInfo>();

    public DataNodeManager(){
        new DataNodeAliveMonitor().start();
    }

    public void register(String ip,String hostname){
        DataNodeInfo dataNodeInfo = new DataNodeInfo(ip, hostname,System.currentTimeMillis());
        datanodes.put(ip + "--" + hostname, dataNodeInfo);
    }

    public void heartbeat(String ip,String hostname){
        DataNodeInfo dataNodeInfo = datanodes.get(ip + "--" + hostname);
        if(dataNodeInfo!=null){
            dataNodeInfo.setLastHeartbeatTime(System.currentTimeMillis());
        }
   }

    /**
     *
     */
   class DataNodeAliveMonitor extends Thread{
       @Override
       public void run() {
           try{
               Set<String> removeKeys = new HashSet<String>();
               datanodes.entrySet().stream().forEach(entry -> {
                   if(System.currentTimeMillis() - entry.getValue().getLastHeartbeatTime() > 90 * 1000L){
                       removeKeys.add(entry.getKey());
                   }
               });
               removeKeys.stream().forEach(key -> {
                   datanodes.remove(key);
               });
               Thread.sleep(3000L);
           }catch (Exception e){
           }
       }
   }

}
