package net.ys.bean;

import java.io.Serializable;

/**
 * User: NMY
 * Date: 17-12-7
 */
public class EtlProject implements Serializable {

    private String id;

    private String prjName;

    private String srcDbId;

    private String srcDbName;

    private String desDbId;

    private String desDbName;

    private int centerDbType;

    private String centerDbIp;

    private String centerDbName;

    private String centerDbPort;

    private String centerDbUsername;

    private String centerDbPwd;

    private int busDbType;

    private String busDbIp;

    private String busDbName;

    private String busDbPort;

    private String busDbUsername;

    private String busDbPwd;

    private long createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrjName() {
        return prjName;
    }

    public void setPrjName(String prjName) {
        this.prjName = prjName;
    }

    public String getSrcDbId() {
        return srcDbId;
    }

    public void setSrcDbId(String srcDbId) {
        this.srcDbId = srcDbId;
    }

    public String getSrcDbName() {
        return srcDbName;
    }

    public void setSrcDbName(String srcDbName) {
        this.srcDbName = srcDbName;
    }

    public String getDesDbId() {
        return desDbId;
    }

    public void setDesDbId(String desDbId) {
        this.desDbId = desDbId;
    }

    public String getDesDbName() {
        return desDbName;
    }

    public void setDesDbName(String desDbName) {
        this.desDbName = desDbName;
    }

    public String getCenterDbIp() {
        return centerDbIp;
    }

    public void setCenterDbIp(String centerDbIp) {
        this.centerDbIp = centerDbIp;
    }

    public String getCenterDbName() {
        return centerDbName;
    }

    public void setCenterDbName(String centerDbName) {
        this.centerDbName = centerDbName;
    }

    public String getCenterDbPort() {
        return centerDbPort;
    }

    public void setCenterDbPort(String centerDbPort) {
        this.centerDbPort = centerDbPort;
    }

    public String getCenterDbUsername() {
        return centerDbUsername;
    }

    public void setCenterDbUsername(String centerDbUsername) {
        this.centerDbUsername = centerDbUsername;
    }

    public String getCenterDbPwd() {
        return centerDbPwd;
    }

    public void setCenterDbPwd(String centerDbPwd) {
        this.centerDbPwd = centerDbPwd;
    }

    public String getBusDbIp() {
        return busDbIp;
    }

    public void setBusDbIp(String busDbIp) {
        this.busDbIp = busDbIp;
    }

    public String getBusDbName() {
        return busDbName;
    }

    public void setBusDbName(String busDbName) {
        this.busDbName = busDbName;
    }

    public String getBusDbPort() {
        return busDbPort;
    }

    public void setBusDbPort(String busDbPort) {
        this.busDbPort = busDbPort;
    }

    public String getBusDbUsername() {
        return busDbUsername;
    }

    public void setBusDbUsername(String busDbUsername) {
        this.busDbUsername = busDbUsername;
    }

    public String getBusDbPwd() {
        return busDbPwd;
    }

    public void setBusDbPwd(String busDbPwd) {
        this.busDbPwd = busDbPwd;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getCenterDbType() {
        return centerDbType;
    }

    public void setCenterDbType(int centerDbType) {
        this.centerDbType = centerDbType;
    }

    public int getBusDbType() {
        return busDbType;
    }

    public void setBusDbType(int busDbType) {
        this.busDbType = busDbType;
    }
}
