package com.joe.namenode.server;

/**
 * 负责管理元数据的核心组件
 */
public class FsNameSystem {

    private FsDirectory fsDirectory;
    private FsEditlog fsEditlog;

    public FsNameSystem(){
        fsDirectory = new FsDirectory();
        fsEditlog = new FsEditlog();
    }

    /**
     * hdfs创建文件夹
     * @param path
     * @return
     */
    public boolean mkdir(String path){
        fsDirectory.mkdir(path);
        fsEditlog.logEdit("创建了文件目录"+path);
        return true;
    }

}
