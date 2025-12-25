package dao;

import model.ConstraintViolation;
import util.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho bảng constraint_violations
 */
public class ConstraintViolationDAO {

    /**
     * Lấy tất cả violations
     */
    public List<ConstraintViolation> getAllViolations() {
        List<ConstraintViolation> violations = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM constraint_violations ORDER BY severity DESC, created_at DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                violations.add(extractViolationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return violations;
    }

    /**
     * Lấy violations theo log ID
     */
    public List<ConstraintViolation> getViolationsByLogId(int logId) {
        List<ConstraintViolation> violations = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM constraint_violations WHERE log_id = ?  ORDER BY severity DESC";
            pstmt = conn. prepareStatement(sql);
            pstmt.setInt(1, logId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                violations.add(extractViolationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return violations;
    }

    /**
     * Lấy violations theo loại
     */
    public List<ConstraintViolation> getViolationsByType(ConstraintViolation.ViolationType violationType) {
        List<ConstraintViolation> violations = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM constraint_violations WHERE violation_type = ? ORDER BY created_at DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt. setString(1, violationType. name());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                violations.add(extractViolationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return violations;
    }

    /**
     * Lấy critical violations
     */
    public List<ConstraintViolation> getCriticalViolations(int logId) {
        List<ConstraintViolation> violations = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM constraint_violations WHERE log_id = ?  AND severity = 'CRITICAL' ORDER BY created_at DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, logId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                violations.add(extractViolationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return violations;
    }

    /**
     * Thêm violation mới
     */
    public boolean insertViolation(ConstraintViolation violation) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "INSERT INTO constraint_violations (log_id, schedule_id, violation_type, severity, description) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, violation.getLogId());
            
            if (violation.getScheduleId() != null) {
                pstmt.setInt(2, violation.getScheduleId());
            } else {
                pstmt. setNull(2, Types.INTEGER);
            }
            
            pstmt.setString(3, violation.getViolationType().name());
            pstmt. setString(4, violation.getSeverity().name());
            pstmt. setString(5, violation.getDescription());

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    violation.setViolationId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Thêm nhiều violations cùng lúc (batch insert)
     */
    public boolean insertViolations(List<ConstraintViolation> violations) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);

            String sql = "INSERT INTO constraint_violations (log_id, schedule_id, violation_type, severity, description) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn. prepareStatement(sql);

            for (ConstraintViolation violation : violations) {
                pstmt.setInt(1, violation.getLogId());
                
                if (violation.getScheduleId() != null) {
                    pstmt.setInt(2, violation.getScheduleId());
                } else {
                    pstmt.setNull(2, Types.INTEGER);
                }
                
                pstmt.setString(3, violation. getViolationType().name());
                pstmt.setString(4, violation.getSeverity().name());
                pstmt. setString(5, violation.getDescription());
                
                pstmt.addBatch();
            }

            int[] results = pstmt.executeBatch();
            conn.commit();

            return results.length == violations.size();

        } catch (SQLException e) {
            JDBCUtils.rollback(conn);
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Xóa violation
     */
    public boolean deleteViolation(int violationId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "DELETE FROM constraint_violations WHERE violation_id = ?";
            pstmt = conn. prepareStatement(sql);
            pstmt.setInt(1, violationId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Xóa tất cả violations của một log
     */
    public boolean deleteViolationsByLogId(int logId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "DELETE FROM constraint_violations WHERE log_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, logId);

            return pstmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Đếm số violations theo loại
     */
    public int countViolationsByType(int logId, ConstraintViolation.ViolationType violationType) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils. getConnection();
            String sql = "SELECT COUNT(*) FROM constraint_violations WHERE log_id = ? AND violation_type = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, logId);
            pstmt. setString(2, violationType. name());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return 0;
    }

    /**
     * Extract ConstraintViolation object từ ResultSet
     */
    private ConstraintViolation extractViolationFromResultSet(ResultSet rs) throws SQLException {
        ConstraintViolation violation = new ConstraintViolation();
        violation.setViolationId(rs.getInt("violation_id"));
        violation.setLogId(rs.getInt("log_id"));
        
        int scheduleId = rs.getInt("schedule_id");
        if (! rs.wasNull()) {
            violation.setScheduleId(scheduleId);
        }
        
        violation.setViolationType(ConstraintViolation.ViolationType.valueOf(rs.getString("violation_type")));
        violation.setSeverity(ConstraintViolation.Severity.valueOf(rs.getString("severity")));
        violation.setDescription(rs.getString("description"));
        violation.setCreatedAt(rs. getTimestamp("created_at").toLocalDateTime());
        return violation;
    }
}