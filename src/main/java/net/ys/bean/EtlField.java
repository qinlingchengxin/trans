package net.ys.bean;

import java.io.Serializable;

/**
 * User: NMY
 * Date: 17-12-7
 */
public class EtlField implements Serializable {

    private String id;

    private String entityId;

    private String srcFieldName;

    private String desFieldName;

    private long createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getSrcFieldName() {
        return srcFieldName;
    }

    public void setSrcFieldName(String srcFieldName) {
        this.srcFieldName = srcFieldName;
    }

    public String getDesFieldName() {
        return desFieldName;
    }

    public void setDesFieldName(String desFieldName) {
        this.desFieldName = desFieldName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
