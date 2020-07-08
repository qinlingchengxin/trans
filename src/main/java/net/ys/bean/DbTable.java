package net.ys.bean;

/**
 * User: NMY
 * Date: 17-12-7
 */
public class DbTable {

    private String tableName;//表名

    private String comment;//注释

    public DbTable(String tableName, String comment) {
        this.tableName = tableName;
        this.comment = comment;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
