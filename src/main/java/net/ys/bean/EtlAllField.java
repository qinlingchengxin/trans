package net.ys.bean;

import java.io.Serializable;

/**
 * 实体字段记录表
 */
public class EtlAllField implements Serializable {

    private String id;    //主键

    private String tableId;    //表名id

    private String name;    //字段名称

    private int priKey;    //是否为主键字段 0-否/1-是

    private String comment;//注释

    private long createTime;    //创建时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriKey() {
        return priKey;
    }

    public void setPriKey(int priKey) {
        this.priKey = priKey;
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