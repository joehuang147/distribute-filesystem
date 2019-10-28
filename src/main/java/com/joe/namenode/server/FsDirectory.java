package com.joe.namenode.server;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理hdfs内存文件目录树
 */
public class FsDirectory {

    /**
     * 根部的文件目录树
     */
    private INodeDirectory iNodeTree;

    public FsDirectory(){
        this.iNodeTree = new INodeDirectory("/");
    }

    /**
     * 创建文件夹
     * @param path 文件夹路径
     * @return  是否创建成功
     */
    public boolean mkdir(String path){
        //增加同步锁，对内存文件目录树
        synchronized (iNodeTree) {
            String[] items = path.split("/");
            INodeDirectory parent = iNodeTree;
            for(String item:items){
                //查找文件夹
                INodeDirectory childDir = parent.findDir(item);
                if (childDir == null) {
                    //说明没有匹配的文件夹
                    childDir = new INodeDirectory(item);
                    parent.addChild(childDir);
                }
                parent = childDir;
            }
            return true;
        }
    }


    /**
     * 元数据节点接口
     */
    interface INode {
    }

    /**
     * 元数据节点文件夹
     */
    class INodeDirectory implements INode {
        private String path;
        private List<INode> children;

        public INodeDirectory(String path){
            this.path = path;
            children = new ArrayList<INode>();
        }

        public INodeDirectory findDir(String dirName){
            List<INode> children = this.getChildren();
            for(INode node:children){
                if(node instanceof INodeDirectory){
                    INodeDirectory dir = (INodeDirectory) node;
                    if (!dir.getPath().equals(dirName)) {
                        continue;
                    }else{
                        return dir;
                    }
                }
            }
            return null;
        }

        public void addChild(INode child){
            this.children.add(child);
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public List<INode> getChildren() {
            return children;
        }

        public void setChildren(List<INode> children) {
            this.children = children;
        }
    }

    /**
     * 元数据节点文件
     */
    class INodeFile implements INode {
        final int type = 1;
        private String name;

        public INodeFile(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
