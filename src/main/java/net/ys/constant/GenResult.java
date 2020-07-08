package net.ys.constant;

import java.util.LinkedHashMap;
import java.util.Map;

public enum GenResult {
    SUCCESS(1000, "request success!"),

    FAILED(1001, "request failed!"),

    PARAMS_ERROR(1002, "parameter error!"),

    TABLE_NAME_INVALID(1003, "table name invalid!"),

    NO_MAP_FIELD(1004, "no map field!"),

    EXEC_ING(1005, "executing, please refresh!"),

    CAN_NOT_ETL_TRANS(1006, "same table can not use etl trans!"),

    NO_PRI_KEY(1007, "no primary key!"),

    UNKNOWN_ERROR(9999, "unknown error!"),;

    public int msgCode;
    public String message;

    private GenResult(int msgCode, String message) {
        this.msgCode = msgCode;
        this.message = message;
    }

    public Map<String, Object> genResult() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("code", msgCode);
        map.put("msg", message);
        return map;
    }

    public Map<String, Object> genResult(Object data) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("code", msgCode);
        map.put("msg", message);
        map.put("data", data);
        return map;
    }
}
