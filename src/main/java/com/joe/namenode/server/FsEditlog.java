package com.joe.namenode.server;

import java.util.LinkedList;

/**
 * 负责将日志写到文件的核心组件
 */
public class FsEditlog {

    /**
     * 日志唯一序号
     */
    private long txid = 0L;
    /**
     * 双缓双缓冲区
     */
    private DoubleBuffer doubleBuffer = new DoubleBuffer();
    /**
     * 是否有线程正在写磁盘
     */
    private boolean isSyncDisk = false;
    /**
     * 是否有线程正在等待写磁盘
     */
    private boolean isWaittingDisk = false;
    /**
     * 写入磁盘的最大序号
     */
    private long maxTxid2Disk = 0L;
    /**
     * 线程的对象变量
     * 当前线程记录日志的序号
     */
    private ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

    /**
     * 日志记录
     * @param content
     * @return
     */
    public void logEdit(String content){
        //必须加锁
        synchronized (doubleBuffer){
            txid++;
            EditLog editLog = new EditLog(txid, content);
            doubleBuffer.write(editLog);
        }

    }

    private void sync2log(){
        synchronized (this){
            //有线程写数据到磁盘
            if(isSyncDisk){
                //写数据的最大序号，大于自身序号，无需写到磁盘
                if(maxTxid2Disk >= threadLocal.get()){
                    return;
                }
                //有线程在等待同步，当前线程无需进行写磁盘
                if(isWaittingDisk){
                    return;
                }
                isWaittingDisk = true;
                while (isSyncDisk){
                    try{
                        //wait和sleep的区别是，wait会释放锁，sleep仍然持有锁
                        //当前应该只有一个线程进入
                        wait(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                isWaittingDisk = false;
            }
            isSyncDisk = true;
            doubleBuffer.sync2Ready();
            maxTxid2Disk = doubleBuffer.getMaxSyncTxid();
        }
        doubleBuffer.flush();
        synchronized (this){
            isSyncDisk = false;
            //写磁盘完成，唤醒所有线程
            notifyAll();
        }
    }


    /**
     * 日志对象
     */
    class EditLog{
        private long txid;
        private String content;
        public EditLog(long txid, String content) {
            this.txid = txid;
            this.content = content;
        }
        public long getTxid() {
            return txid;
        }
        public void setTxid(long txid) {
            this.txid = txid;
        }
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }
    }


    /**
     * 双缓冲类
     */
    class DoubleBuffer{
        /**
         * 接受写入请求的列表
         */
        private LinkedList<EditLog> currentBuffer = new LinkedList<EditLog>();
        /**
         * 进行日志落地的列表
         */
        private LinkedList<EditLog> readyBuffer = new LinkedList<EditLog>();

        public DoubleBuffer(){
        }

        public void write(EditLog editLog){
            currentBuffer.add(editLog);
        }

        /**
         * 交换缓冲
         */
        public void sync2Ready(){
            synchronized (this) {
                LinkedList<EditLog> tmp = currentBuffer;
                currentBuffer = readyBuffer;
                readyBuffer = tmp;
            }
        }
        /**
         * 获取ready缓存的最大序号
         */
        public long getMaxSyncTxid(){
            return readyBuffer.getLast().getTxid();
        }

        /**
         * 写日志到本地
         */
        public void flush(){
            for(EditLog editLog : readyBuffer){
                System.out.println(String.format("将edit写入文件，txid:%s,content:%s", editLog.getTxid(), editLog.getContent()));
            }
            readyBuffer.clear();
        }


    }



}