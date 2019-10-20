package com.joe.namenode.server;

/**
 * hdfs操作记录到磁盘文件
 */
public class FsEditlog {

    public boolean logEdit(String log){
        System.out.println("记录当前日志为："+log);
        return true;
    }

}
