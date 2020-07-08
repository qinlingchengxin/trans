package net.ys.utils;

import net.ys.bean.*;
import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: NMY
 * Date: 18-3-20
 */
public class DBUtil {

    public static Connection getConnectionMySql(String ip, int port, String dbName, String userName, String pwd) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
            return DriverManager.getConnection(url, userName, pwd);
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return null;
    }

    public static Connection getConnectionKingBase(String ip, int port, String dbName, String userName, String pwd) {
        try {
            Class.forName("com.kingbase.Driver");
            String url = "jdbc:kingbase://" + ip + ":" + port + "/" + dbName;
            return DriverManager.getConnection(url, userName, pwd);
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return null;
    }

    public static Connection getConnectionOracle(String ip, int port, String dbName, String userName, String pwd) {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            String url = "jdbc:oracle:thin:@" + ip + ":" + port + "/" + dbName;
            return DriverManager.getConnection(url, userName, pwd);
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return null;
    }

    public static Connection getConnectionMSSQL(String ip, int port, String dbName, String userName, String pwd) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://" + ip + ":" + port + ";DatabaseName=" + dbName;
            return DriverManager.getConnection(url, userName, pwd);
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return null;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LogUtil.error(e);
        }
    }

    public static long getDataCount(Connection connection, String sql, String lastTransTime, String now) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        long dataCount = 0;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, lastTransTime);
            statement.setString(2, now);
            rs = statement.executeQuery();
            while (rs.next()) {
                dataCount = rs.getLong("C");
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
            }
        }
        return dataCount;
    }

    public static List<Map<String, Object>> getData(Connection connection, String sql, String lastTransTime, String now, int startPos, int endPos) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, lastTransTime);
            statement.setString(2, now);
            statement.setInt(3, startPos);
            statement.setInt(4, endPos);
            rs = statement.executeQuery();
            Map<String, Object> map;
            ResultSetMetaData metaData;
            int columnCount;
            Object o = null;
            String columnType;
            Clob clob;
            while (rs.next()) {
                map = new LinkedHashMap<String, Object>();
                metaData = rs.getMetaData();
                columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    columnType = metaData.getColumnTypeName(i);
                    if ("CLOB".equals(columnType)) {
                        clob = rs.getClob(i);
                        if (clob != null) {
                            o = clob.getSubString((long) 1, (int) clob.length());
                        }
                    } else {
                        o = rs.getObject(i);
                    }
                    map.put(metaData.getColumnName(i), o);
                }
                data.add(map);
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
            }
        }
        return data;
    }

    public static List<DbTable> getTablesMySql(String ip, int port, String dbName, String userName, String pwd) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
            connection = DriverManager.getConnection(url, userName, pwd);
            statement = connection.prepareStatement("SELECT TABLE_NAME, TABLE_COMMENT FROM information_schema.`TABLES` WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_NAME NOT LIKE 'sys_etl_%' AND TABLE_SCHEMA = ?");
            statement.setString(1, dbName);
            rs = statement.executeQuery();
            List<DbTable> tables = new ArrayList<DbTable>();
            String tableName;
            String tableComment;
            while (rs.next()) {
                tableName = rs.getString("TABLE_NAME");
                tableComment = rs.getString("TABLE_COMMENT");
                if (StringUtils.isBlank(tableComment)) {
                    tableComment = tableName;
                }
                tables.add(new DbTable(tableName, tableComment));
            }
            rs.close();
            statement.close();
            connection.close();
            return tables;
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
        return null;
    }

    public static List<DbTable> getTablesOracle(String ip, int port, String dbName, String userName, String pwd) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            String url = "jdbc:oracle:thin:@" + ip + ":" + port + "/" + dbName;
            connection = DriverManager.getConnection(url, userName, pwd);
            statement = connection.prepareStatement("SELECT TABLE_NAME, COMMENTS FROM user_tab_comments WHERE TABLE_TYPE = 'TABLE'");
            rs = statement.executeQuery();
            List<DbTable> tables = new ArrayList<DbTable>();
            String tableName;
            String tableComment;
            while (rs.next()) {
                tableName = rs.getString("TABLE_NAME");
                tableComment = rs.getString("COMMENTS");
                if (StringUtils.isBlank(tableComment)) {
                    tableComment = tableName;
                }
                tables.add(new DbTable(tableName, tableComment));
            }
            rs.close();
            statement.close();
            connection.close();
            return tables;
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
        return null;
    }

    public static List<DbTable> getTablesMSSQL(String ip, int port, String dbName, String userName, String pwd) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://" + ip + ":" + port + ";DatabaseName=" + dbName;
            connection = DriverManager.getConnection(url, userName, pwd);
            statement = connection.prepareStatement("SELECT CONVERT(nvarchar(50),so.[NAME]) AS TABLE_NAME, CONVERT(nvarchar(200),ds.[VALUE]) AS COMMENTS FROM sysobjects so LEFT JOIN sys.extended_properties ds ON ds.major_id = so.id WHERE so.xtype = 'U'");
            rs = statement.executeQuery();
            List<DbTable> tables = new ArrayList<DbTable>();
            String tableName;
            String tableComment;
            while (rs.next()) {
                tableName = rs.getString("TABLE_NAME");
                tableComment = rs.getString("COMMENTS");
                if (StringUtils.isBlank(tableComment)) {
                    tableComment = tableName;
                }
                tables.add(new DbTable(tableName, tableComment));
            }
            rs.close();
            statement.close();
            connection.close();
            return tables;
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
        return null;
    }

    public static List<DbField> getFieldsMySql(String ip, int port, String dbName, EtlAllTable table, String userName, String pwd) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
            connection = DriverManager.getConnection(url, userName, pwd);
            statement = connection.prepareStatement("SELECT COLUMN_NAME, COLUMN_COMMENT, IF (COLUMN_KEY = 'PRI', 1, 0) AS PRI_KEY FROM information_schema.`COLUMNS` WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?");
            statement.setString(1, dbName);
            statement.setString(2, table.getName());
            rs = statement.executeQuery();
            List<DbField> fields = new ArrayList<DbField>();
            String columnName;
            String columnComment;
            int priKey;
            while (rs.next()) {
                columnName = rs.getString("COLUMN_NAME");
                columnComment = rs.getString("COLUMN_COMMENT");
                priKey = rs.getInt("PRI_KEY");
                if (StringUtils.isBlank(columnComment)) {
                    columnComment = columnName;
                }
                fields.add(new DbField(table.getId(), columnName, columnComment, priKey));
            }
            rs.close();
            statement.close();
            connection.close();
            return fields;
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
        return null;
    }

    public static List<DbField> getAllFieldsMySql(String ip, int port, String dbName, String userName, String pwd) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
            connection = DriverManager.getConnection(url, userName, pwd);
            statement = connection.prepareStatement("SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT, IF (COLUMN_KEY = 'PRI', 1, 0) AS PRI_KEY FROM information_schema.`COLUMNS` WHERE TABLE_SCHEMA = ? ORDER BY TABLE_NAME");
            statement.setString(1, dbName);
            rs = statement.executeQuery();
            List<DbField> fields = new ArrayList<DbField>();
            String tableName;
            String columnName;
            String columnType;
            String columnComment;
            int priKey;
            while (rs.next()) {
                tableName = rs.getString("TABLE_NAME");
                columnName = rs.getString("COLUMN_NAME");
                columnType = rs.getString("DATA_TYPE");
                columnComment = rs.getString("COLUMN_COMMENT");
                priKey = rs.getInt("PRI_KEY");
                if (StringUtils.isBlank(columnComment)) {
                    columnComment = columnName;
                }
                fields.add(new DbField(tableName, columnName, columnType, columnComment, priKey));
            }
            rs.close();
            statement.close();
            connection.close();
            return fields;
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
        return null;
    }

    public static List<DbField> getFieldsOracle(String ip, int port, String dbName, EtlAllTable table, String userName, String pwd) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            String url = "jdbc:oracle:thin:@" + ip + ":" + port + "/" + dbName;
            connection = DriverManager.getConnection(url, userName, pwd);
            statement = connection.prepareStatement("SELECT cc.COLUMN_NAME, cc.COMMENTS, CASE WHEN t1.COLUMN_NAME IS NULL THEN 0 ELSE 1 END AS PRI_KEY FROM user_col_comments cc LEFT JOIN ( SELECT cu.COLUMN_NAME, au.TABLE_NAME FROM user_cons_columns cu, user_constraints au WHERE cu.constraint_name = au.constraint_name AND au.constraint_type = 'P' ) t1 ON t1.TABLE_NAME = cc.TABLE_NAME AND t1.COLUMN_NAME = cc.COLUMN_NAME WHERE cc.TABLE_NAME = ?");
            statement.setString(1, table.getName());
            rs = statement.executeQuery();
            List<DbField> fields = new ArrayList<DbField>();
            String columnName;
            String columnComment;
            int priKey;
            while (rs.next()) {
                columnName = rs.getString("COLUMN_NAME");
                columnComment = rs.getString("COMMENTS");
                priKey = rs.getInt("PRI_KEY");
                if (StringUtils.isBlank(columnComment)) {
                    columnComment = columnName;
                }
                fields.add(new DbField(table.getId(), columnName, columnComment, priKey));
            }
            rs.close();
            statement.close();
            connection.close();
            return fields;
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
        return null;
    }

    public static List<DbField> getFieldsMSSQL(String ip, int port, String dbName, EtlAllTable table, String userName, String pwd) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://" + ip + ":" + port + ";DatabaseName=" + dbName;
            connection = DriverManager.getConnection(url, userName, pwd);
            statement = connection.prepareStatement("SELECT c. NAME AS COLUMN_NAME, CAST(ep.[VALUE] AS VARCHAR(100)) AS COMMENTS, ISNULL(i.is_primary_key, 0) AS PRI_KEY FROM sys. COLUMNS c LEFT JOIN sys.extended_properties ep ON ep.major_id = c.object_id AND ep.minor_id = c.column_id LEFT OUTER JOIN ( sys.index_columns ic INNER JOIN sys.indexes i ON ic.object_id = i.object_id AND i.is_primary_key = 1 AND ic.index_id = i.index_id ) ON ic.object_id = c.object_id AND ic.column_id = c.column_id WHERE c.object_id = OBJECT_ID (?)");
            statement.setString(1, table.getName());
            rs = statement.executeQuery();
            List<DbField> fields = new ArrayList<DbField>();
            String columnName;
            String columnComment;
            int priKey;
            while (rs.next()) {
                columnName = rs.getString("COLUMN_NAME");
                columnComment = rs.getString("COMMENTS");
                priKey = rs.getInt("PRI_KEY");
                if (StringUtils.isBlank(columnComment)) {
                    columnComment = columnName;
                }
                fields.add(new DbField(table.getId(), columnName, columnComment, priKey));
            }
            rs.close();
            statement.close();
            connection.close();
            return fields;
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
        return null;
    }

    public static boolean testConnMySql(EtlDataSource etlDataSource) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + etlDataSource.getDbIp() + ":" + etlDataSource.getDbPort() + "/" + etlDataSource.getDbName();
            connection = DriverManager.getConnection(url, etlDataSource.getDbUsername(), etlDataSource.getDbPwd());
            connection.close();
            return true;
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
        return false;
    }

    public static boolean testConnOracle(EtlDataSource etlDataSource) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            String url = "jdbc:oracle:thin:@" + etlDataSource.getDbIp() + ":" + etlDataSource.getDbPort() + "/" + etlDataSource.getDbName();
            connection = DriverManager.getConnection(url, etlDataSource.getDbUsername(), etlDataSource.getDbPwd());
            connection.close();
            return true;
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
        return false;
    }

    public static boolean testConnMSSQL(EtlDataSource etlDataSource) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://" + etlDataSource.getDbIp() + ":" + etlDataSource.getDbPort() + ";DatabaseName=" + etlDataSource.getDbName();
            connection = DriverManager.getConnection(url, etlDataSource.getDbUsername(), etlDataSource.getDbPwd());
            connection.close();
            return true;
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
        return false;
    }

    public static boolean addDataStepMysql(Connection connection, String sql, List<Map<String, Object>> data, EtlEntity entity) {
        PreparedStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            for (Map<String, Object> map : data) {
                int i = 1;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if ("ORACLE___RW".equals(entry.getKey())) {
                        continue;
                    }
                    statement.setObject(i++, entry.getValue());
                }

                statement.setObject(i++, map.get(entity.getDesPrimaryKey()));
                statement.setObject(i++, map.get("SYS__CREATE_OR_UPDATE_TIME"));

                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if ("ORACLE___RW".equals(entry.getKey())) {
                        continue;
                    }
                    statement.setObject(i++, entry.getValue());
                }
                statement.addBatch();
            }

            statement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
            statement.close();
            return true;
        } catch (Exception e) {
            LogUtil.debug(e.getMessage());
            LogUtil.error(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
            }
        }
        return false;
    }

    public static boolean addDataStepOracle(Connection connection, String sql, List<Map<String, Object>> data) {
        PreparedStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            for (Map<String, Object> map : data) {
                int i = 1;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if ("ORACLE___RW".equals(entry.getKey())) {
                        continue;
                    }
                    statement.setObject(i++, entry.getValue());
                }
                statement.addBatch();
            }

            statement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
            statement.close();
            return true;
        } catch (Exception e) {
            LogUtil.debug(e.getMessage());
            LogUtil.error(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
            }
        }
        return false;
    }

    public static boolean testConnKingBase(EtlDataSource etlDataSource) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            Class.forName("com.kingbase.Driver");
            String url = "jdbc:kingbase://" + etlDataSource.getDbIp() + ":" + etlDataSource.getDbPort() + "/" + etlDataSource.getDbName();
            connection = DriverManager.getConnection(url, etlDataSource.getDbUsername(), etlDataSource.getDbPwd());
            connection.close();
            return true;
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
        return false;
    }

    public static List<DbTable> getTablesKingBase(EtlDataSource dataSource) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            Class.forName("com.kingbase.Driver");
            String url = "jdbc:kingbase://" + dataSource.getDbIp() + ":" + dataSource.getDbPort() + "/" + dataSource.getDbName();
            connection = DriverManager.getConnection(url, dataSource.getDbUsername(), dataSource.getDbPwd());
            statement = connection.prepareStatement("SELECT \"table_name\" AS \"TABLE_NAME\", \"table_name\" AS \"TABLE_COMMENT\" FROM \"information_schema\".\"TABLES\" WHERE \"table_type\" = 'BASE TABLE' AND \"table_schema\" = ?");
            statement.setString(1, dataSource.getDbSchema());
            rs = statement.executeQuery();
            List<DbTable> tables = new ArrayList<DbTable>();
            String tableName;
            String tableComment;
            while (rs.next()) {
                tableName = rs.getString("TABLE_NAME");
                tableComment = rs.getString("TABLE_COMMENT");
                if (StringUtils.isBlank(tableComment)) {
                    tableComment = tableName;
                }
                tables.add(new DbTable(tableName, tableComment));
            }
            rs.close();
            statement.close();
            connection.close();
            return tables;
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
        return null;
    }

    public static List<DbField> getFieldsKingBase(EtlDataSource dataSource, EtlAllTable table) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            Class.forName("com.kingbase.Driver");
            String url = "jdbc:kingbase://" + dataSource.getDbIp() + ":" + dataSource.getDbPort() + "/" + dataSource.getDbName();
            connection = DriverManager.getConnection(url, dataSource.getDbUsername(), dataSource.getDbPwd());

            statement = connection.prepareStatement("SELECT \"column_name\" AS COLUMN_NAME, \"column_name\" AS COLUMN_COMMENT, 0 AS PRI_KEY FROM \"information_schema\".\"COLUMNS\" WHERE \"table_schema\" = ? AND \"table_name\" = ?");
            statement.setString(1, dataSource.getDbSchema());
            statement.setString(2, table.getName());
            rs = statement.executeQuery();
            List<DbField> fields = new ArrayList<DbField>();
            String columnName;
            String columnComment;
            int priKey;
            while (rs.next()) {
                columnName = rs.getString("COLUMN_NAME");
                columnComment = rs.getString("COLUMN_COMMENT");
                priKey = rs.getInt("PRI_KEY");
                if (StringUtils.isBlank(columnComment)) {
                    columnComment = columnName;
                }
                fields.add(new DbField(table.getId(), columnName, columnComment, priKey));
            }
            rs.close();
            statement.close();
            connection.close();
            return fields;
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
        return null;
    }

    public static boolean addDataStepKingBase(Connection connection, String sql, List<Map<String, Object>> data, EtlEntity entity) {
        PreparedStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);

            //插入数据
            for (Map<String, Object> map : data) {

                int i = 1;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if ("ORACLE___RW".equals(entry.getKey())) {
                        continue;
                    }
                    statement.setObject(i++, entry.getValue());
                }

                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if ("ORACLE___RW".equals(entry.getKey())) {
                        continue;
                    }
                    statement.setObject(i++, entry.getValue());
                }

                statement.setObject(i++, map.get("SYS__CREATE_OR_UPDATE_TIME"));

                statement.addBatch();
            }

            statement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
            statement.close();
            return true;
        } catch (Exception e) {
            LogUtil.debug(e.getMessage());
            LogUtil.error(e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
            }
        }
        return false;
    }
}
