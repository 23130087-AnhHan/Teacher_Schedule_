package util;

import java.sql.*;

public class JDBCUtils {

    private static final String URL = "jdbc:mysql://localhost:3306/teacher_schedule_ga?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    
    private static boolean driverLoaded = false;

    // THÊM LẠI static block - BẮT BUỘC! 
    static {
        try {
            Class.forName("com.mysql.cj.jdbc. Driver");
            driverLoaded = true;
        } catch (ClassNotFoundException e) {
            try {
                DriverManager.registerDriver(new com.mysql.cj.jdbc. Driver());
                driverLoaded = true;
            } catch (SQLException ex) {
                // KHÔNG in gì ở đây để tránh vòng lặp
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (! conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err. println("⚠️ Lỗi khi đóng Connection: " + e.getMessage());
            }
        }
    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System. err.println("⚠️ Lỗi khi đóng Statement: " + e.getMessage());
            }
        }
    }

    public static void closePreparedStatement(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("⚠️ Lỗi khi đóng PreparedStatement: " + e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs. close();
            } catch (SQLException e) {
                System.err.println("⚠️ Lỗi khi đóng ResultSet: " + e.getMessage());
            }
        }
    }

    public static void closeAll(Connection conn, Statement stmt, ResultSet rs) {
        closeResultSet(rs);
        closeStatement(stmt);
        closeConnection(conn);
    }

    public static void closeAll(Connection conn, PreparedStatement pstmt) {
        closePreparedStatement(pstmt);
        closeConnection(conn);
    }

    public static void closeAll(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        closeResultSet(rs);
        closePreparedStatement(pstmt);
        closeConnection(conn);
    }

    public static void testConnection() {
        // In driver status CHỈ 1 LẦN
        if (driverLoaded) {
            System.out.println("✅ MySQL Driver loaded successfully!");
        } else {
            System.err.println("❌ MySQL Driver NOT loaded!");
        }
        
        Connection conn = null;
        try {
            conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out. println("✅ Kết nối database thành công!");
                DatabaseMetaData metaData = conn.getMetaData();
                System.out.println("   📌 Database: " + metaData.getDatabaseProductName());
                System.out.println("   📌 Version: " + metaData.getDatabaseProductVersion());
                System.out.println("   📌 URL: " + metaData.getURL());
                System.out.println("   📌 User: " + metaData.getUserName());
                System.out.println("   📌 Driver: " + metaData.getDriverName());
                System.out.println("   📌 Driver version: " + metaData.getDriverVersion());
            }
        } catch (SQLException e) {
            System.err.println("❌ Kết nối database thất bại!");
            System.err.println("   ❗ Error:  " + e.getMessage());
            System.err.println("   ❗ SQLState: " + e.getSQLState());
            System.err.println("   ❗ Error Code: " + e.getErrorCode());
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
                System.out.println("⚠️ Transaction đã được rollback");
            } catch (SQLException e) {
                System.err.println("❌ Lỗi khi rollback:  " + e.getMessage());
            }
        }
    }

    public static void commit(Connection conn) {
        if (conn != null) {
            try {
                conn.commit();
            } catch (SQLException e) {
                System.err.println("❌ Lỗi khi commit: " + e.getMessage());
            }
        }
    }

    public static void setAutoCommit(Connection conn, boolean autoCommit) {
        if (conn != null) {
            try {
                conn.setAutoCommit(autoCommit);
            } catch (SQLException e) {
                System.err.println("❌ Lỗi khi set auto-commit: " + e.getMessage());
            }
        }
    }
}