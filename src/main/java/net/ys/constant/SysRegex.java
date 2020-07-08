package net.ys.constant;

/**
 * 系统正则
 * User: NMY
 * Date: 17-6-20
 */
public enum SysRegex {
    TABLE_FIELD_NAME("[a-zA-Z]{1}\\w+", "表名或字段名"),
    IP("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)($|(?!\\.$)\\.)){4}$", "IP地址"),;

    public String regex;
    public String desc;

    private SysRegex(String regex, String desc) {
        this.regex = regex;
        this.desc = desc;
    }
}
