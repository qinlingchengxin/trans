package net.ys.constant;

public enum DbType {

    MY_SQL(0), ORACLE(1), MS_SQL(2), KING_BASE(3);

    public int type;

    DbType(int type) {
        this.type = type;
    }
}
