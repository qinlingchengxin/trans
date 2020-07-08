package net.ys.bean;

import java.io.Serializable;

/**
 * etl数据源对应的所有数据表
 */
public class EtlAllTable implements Serializable {

    private String id;    //主键

    private String dsId;    //数据源id

    private String name;    //表名

    private String comment;    //注释

    private long createTime;    //创建时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDsId() {
        return dsId;
    }

    public void setDsId(String dsId) {
        this.dsId = dsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}