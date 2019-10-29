package com.joe.namenode.server;

import java.util.LinkedList;
import java.util.List;

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
     * 日志记录
     * @param content
     * @return
     */
    public boolean logEdit(String content){
        //必须加锁
        synchronized (doubleBuffer){
            txid++;
            EditLog editLog = new EditLog(txid, content);
            doubleBuffer.write(editLog);
        }
        return true;
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
        private List<EditLog> currentBuffer = new LinkedList<EditLog>();
        /**
         * 进行日志落地的列表
         */
        private List<EditLog> readyBuffer = new LinkedList<EditLog>();

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
                List<EditLog> tmp = currentBuffer;
                currentBuffer = readyBuffer;
                readyBuffer = tmp;
            }
        }

        /**
         * 写日志到本地
         */
        public void flush(){
            for(EditLog editLog : readyBuffer){
                System.out.println(String.format("将edit写入文件，txid:%s,content:%s", editLog.getTxid(), editLog.getContent()));
            }
        }


    }



}
