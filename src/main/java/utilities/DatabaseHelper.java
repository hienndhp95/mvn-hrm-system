package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper {

    public static Connection conn;

    public static Connection getPostgreSQLConnection() {
        String hostName = "";
        String database = "";
        String userName = "";
        String password = "";
        return getPostgreSQLConnection(hostName, database, userName, password);
    }

    public static Connection getPostgreSQLConnection(String hostName, String database, String userName, String password) {
        Connection conn = null;
        try {
            String connectionURL = "jdbc:postgresql://" + hostName + ":5432/" + database;
            conn = DriverManager.getConnection(connectionURL, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void connectToDatabasePostgreSql() {
        conn = getPostgreSQLConnection();
    }

    public static List<Map<String, ?>> selectQuery(String query, String ColumnName, String expectedValue) {
        List<Map<String, ?>> results = new ArrayList<Map<String, ?>>();
        try {
            if (conn != null) {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(query);
                ResultSetMetaData md = rs.getMetaData();
                int columns = md.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<String, Object>();
                    for (int i = 1; i <= columns; i++) {
                        row.put(md.getColumnLabel(i).toUpperCase(), rs.getObject(i));
                    }
                    results.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;

    }
}
